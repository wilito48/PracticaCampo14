package votacion_util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import votacion_modelo.Usuario;
import votacion_modelo.Candidato;

/**
 * Clase para manejar la conexión y operaciones con la base de datos MySQL
 * @author Sistema de Votación
 * @version 1.0
 */
public class ConexionDB {
    private static final String URL = "jdbc:mysql://localhost:3306/sistema_votacion";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private static Connection conexion;
    
    /**
     * Establece la conexión con la base de datos
     * @return true si la conexión fue exitosa, false en caso contrario
     */
    public static boolean conectar() {
        try {
            // Cargar el driver de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Establecer la conexión
            conexion = DriverManager.getConnection(URL, USER, PASSWORD);
            
            // Crear las tablas si no existen
            crearTablas();
            
            return true;
        } catch (ClassNotFoundException e) {
            System.err.println("Error: No se encontró el driver de MySQL");
            System.err.println("Asegúrate de tener el conector MySQL JDBC en tu proyecto");
            return false;
        } catch (SQLException e) {
            System.err.println("Error al conectar con la base de datos: " + e.getMessage());
            System.err.println("Verifica que XAMPP esté ejecutándose y MySQL esté activo");
            return false;
        }
    }
    
    /**
     * Cierra la conexión con la base de datos
     */
    public static void desconectar() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }
    
    /**
     * Crea las tablas necesarias si no existen
     */
    private static void crearTablas() {
        try {
            Statement stmt = conexion.createStatement();
            
         // En el método crearTablas(), actualiza la tabla usuarios:
            String crearTablaUsuarios = """
                CREATE TABLE IF NOT EXISTS usuarios (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    nombre VARCHAR(50) NOT NULL,
                    apellido VARCHAR(50) NOT NULL,
                    email VARCHAR(100) UNIQUE NOT NULL,
                    password VARCHAR(255) NOT NULL,
                    dni VARCHAR(20) UNIQUE NOT NULL,
                    ya_voto BOOLEAN DEFAULT FALSE,
                    rol VARCHAR(20) DEFAULT 'USUARIO',
                    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
            """;
            
            // Tabla de candidatos
            String crearTablaCandidatos = """
                CREATE TABLE IF NOT EXISTS candidatos (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    numero INT UNIQUE NOT NULL,
                    nombre VARCHAR(100) NOT NULL,
                    partido VARCHAR(100) NOT NULL,
                    votos INT DEFAULT 0,
                    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
            """;
            
            // Tabla de votos (para auditoría)
            String crearTablaVotos = """
                CREATE TABLE IF NOT EXISTS votos (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    usuario_id INT NOT NULL,
                    candidato_id INT NOT NULL,
                    fecha_voto TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
                    FOREIGN KEY (candidato_id) REFERENCES candidatos(id)
                )
            """;
            
            stmt.execute(crearTablaUsuarios);
            stmt.execute(crearTablaCandidatos);
            stmt.execute(crearTablaVotos);
            
            stmt.close();
            
        } catch (SQLException e) {
            System.err.println("Error al crear las tablas: " + e.getMessage());
        }
    }
    
    // ========== MÉTODOS PARA USUARIOS ==========
    
    /**
     * Registra un nuevo usuario en la base de datos
     * @param usuario Usuario a registrar
     * @return true si se registró correctamente, false en caso contrario
     */
    public static boolean registrarUsuario(Usuario usuario) {
        try {
            String sql = "INSERT INTO usuarios (nombre, apellido, email, password, dni, rol) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conexion.prepareStatement(sql);

            pstmt.setString(1, usuario.getNombre());
            pstmt.setString(2, usuario.getApellido());
            pstmt.setString(3, usuario.getEmail());
            pstmt.setString(4, usuario.getPassword());
            pstmt.setString(5, usuario.getDni());
            pstmt.setString(6, usuario.getRol());

            int resultado = pstmt.executeUpdate();
            pstmt.close();

            return resultado > 0;

        } catch (SQLException e) {
            System.err.println("Error al registrar usuario: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Autentica un usuario por email y contraseña
     * @param email Email del usuario
     * @param password Contraseña del usuario
     * @return Usuario autenticado o null si falla la autenticación
     */
    public static Usuario autenticarUsuario(String email, String password) {
        try {
            String sql = "SELECT * FROM usuarios WHERE email = ? AND password = ?";
            PreparedStatement pstmt = conexion.prepareStatement(sql);

            pstmt.setString(1, email);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Usuario usuario = new Usuario(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("apellido"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("dni"),
                    rs.getString("rol")
                );
                usuario.setYaVoto(rs.getBoolean("ya_voto"));

                rs.close();
                pstmt.close();
                return usuario;
            }

            rs.close();
            pstmt.close();
            return null;

        } catch (SQLException e) {
            System.err.println("Error al autenticar usuario: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Verifica si un email ya está registrado
     * @param email Email a verificar
     * @return true si el email ya existe, false en caso contrario
     */
    public static boolean emailExiste(String email) {
        try {
            String sql = "SELECT COUNT(*) FROM usuarios WHERE email = ?";
            PreparedStatement pstmt = conexion.prepareStatement(sql);
            
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                boolean existe = rs.getInt(1) > 0;
                rs.close();
                pstmt.close();
                return existe;
            }
            
            rs.close();
            pstmt.close();
            return false;
            
        } catch (SQLException e) {
            System.err.println("Error al verificar email: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Verifica si un DNI ya está registrado
     * @param dni DNI a verificar
     * @return true si el DNI ya existe, false en caso contrario
     */
    public static boolean dniExiste(String dni) {
        try {
            String sql = "SELECT COUNT(*) FROM usuarios WHERE dni = ?";
            PreparedStatement pstmt = conexion.prepareStatement(sql);
            
            pstmt.setString(1, dni);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                boolean existe = rs.getInt(1) > 0;
                rs.close();
                pstmt.close();
                return existe;
            }
            
            rs.close();
            pstmt.close();
            return false;
            
        } catch (SQLException e) {
            System.err.println("Error al verificar DNI: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Marca que un usuario ya votó
     * @param usuarioId ID del usuario
     * @return true si se actualizó correctamente, false en caso contrario
     */
    public static boolean marcarUsuarioVoto(int usuarioId) {
        try {
            String sql = "UPDATE usuarios SET ya_voto = TRUE WHERE id = ?";
            PreparedStatement pstmt = conexion.prepareStatement(sql);
            
            pstmt.setInt(1, usuarioId);
            
            int resultado = pstmt.executeUpdate();
            pstmt.close();
            
            return resultado > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al marcar voto del usuario: " + e.getMessage());
            return false;
        }
    }
    
    // ========== MÉTODOS PARA CANDIDATOS ==========
    
    /**
     * Registra un nuevo candidato en la base de datos
     * @param candidato Candidato a registrar
     * @return true si se registró correctamente, false en caso contrario
     */
    public static boolean registrarCandidato(Candidato candidato) {
        try {
            String sql = "INSERT INTO candidatos (numero, nombre, partido) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conexion.prepareStatement(sql);
            
            pstmt.setInt(1, candidato.getNumero());
            pstmt.setString(2, candidato.getNombre());
            pstmt.setString(3, candidato.getPartido());
            
            int resultado = pstmt.executeUpdate();
            pstmt.close();
            
            return resultado > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al registrar candidato: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Obtiene todos los candidatos de la base de datos
     * @return Lista de candidatos
     */
    public static List<Candidato> obtenerCandidatos() {
        List<Candidato> candidatos = new ArrayList<>();
        
        try {
            String sql = "SELECT * FROM candidatos ORDER BY numero";
            Statement stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                Candidato candidato = new Candidato(
                    rs.getInt("numero"),
                    rs.getString("nombre"),
                    rs.getString("partido")
                );
                candidato.setVotos(rs.getInt("votos"));
                candidatos.add(candidato);
            }
            
            rs.close();
            stmt.close();
            
        } catch (SQLException e) {
            System.err.println("Error al obtener candidatos: " + e.getMessage());
        }
        
        return candidatos;
    }
    
    /**
     * Registra un voto para un candidato
     * @param candidatoId ID del candidato
     * @param usuarioId ID del usuario que vota
     * @return true si se registró correctamente, false en caso contrario
     */
    public static boolean registrarVoto(int candidatoId, int usuarioId) {
        try {
            // Iniciar transacción
            conexion.setAutoCommit(false);
            
            // Registrar el voto en la tabla de votos
            String sqlVoto = "INSERT INTO votos (usuario_id, candidato_id) VALUES (?, ?)";
            PreparedStatement pstmtVoto = conexion.prepareStatement(sqlVoto);
            pstmtVoto.setInt(1, usuarioId);
            pstmtVoto.setInt(2, candidatoId);
            pstmtVoto.executeUpdate();
            pstmtVoto.close();
            
            // Actualizar contador de votos del candidato
            String sqlActualizar = "UPDATE candidatos SET votos = votos + 1 WHERE id = ?";
            PreparedStatement pstmtActualizar = conexion.prepareStatement(sqlActualizar);
            pstmtActualizar.setInt(1, candidatoId);
            pstmtActualizar.executeUpdate();
            pstmtActualizar.close();
            
            // Marcar que el usuario ya votó
            marcarUsuarioVoto(usuarioId);
            
            // Confirmar transacción
            conexion.commit();
            conexion.setAutoCommit(true);
            
            return true;
            
        } catch (SQLException e) {
            try {
                conexion.rollback();
                conexion.setAutoCommit(true);
            } catch (SQLException ex) {
                System.err.println("Error al hacer rollback: " + ex.getMessage());
            }
            System.err.println("Error al registrar voto: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Obtiene el ID de un candidato por su número
     * @param numero Número del candidato
     * @return ID del candidato o -1 si no existe
     */
    public static int obtenerIdCandidato(int numero) {
        try {
            String sql = "SELECT id FROM candidatos WHERE numero = ?";
            PreparedStatement pstmt = conexion.prepareStatement(sql);
            
            pstmt.setInt(1, numero);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                int id = rs.getInt("id");
                rs.close();
                pstmt.close();
                return id;
            }
            
            rs.close();
            pstmt.close();
            return -1;
            
        } catch (SQLException e) {
            System.err.println("Error al obtener ID del candidato: " + e.getMessage());
            return -1;
        }
    }
    
    /**
     * Reinicia todos los votos (para nueva elección)
     */
    public static void reiniciarVotos() {
        try {
            String sql = "UPDATE candidatos SET votos = 0";
            Statement stmt = conexion.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
            
            // También reiniciar el estado de votación de los usuarios
            String sqlUsuarios = "UPDATE usuarios SET ya_voto = FALSE";
            Statement stmtUsuarios = conexion.createStatement();
            stmtUsuarios.executeUpdate(sqlUsuarios);
            stmtUsuarios.close();
            
        } catch (SQLException e) {
            System.err.println("Error al reiniciar votos: " + e.getMessage());
        }
    }
} 