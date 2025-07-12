package votacion_controlador;

import votacion_modelo.Usuario;
import votacion_util.ConexionDB;

/**
 * Controlador encargado de la lógica de login y registro de usuarios.
 * Separa la lógica de negocio de la vista (VentanaLoginUsuario).
 * @author Sistema de Votación
 * @version 2.0
 */
public class ControladorLoginUsuario {
    /**
     * Clase interna para encapsular el resultado del login.
     * Incluye si fue exitoso, un mensaje y el usuario autenticado (si aplica).
     */
    public static class ResultadoLogin {
        public final boolean exito;
        public final String mensaje;
        public final Usuario usuario;
        public ResultadoLogin(boolean exito, String mensaje, Usuario usuario) {
            this.exito = exito;
            this.mensaje = mensaje;
            this.usuario = usuario;
        }
    }

    /**
     * Clase interna para encapsular el resultado del registro de usuario.
     * Incluye si fue exitoso y un mensaje para la vista.
     */
    public static class ResultadoRegistro {
        public final boolean exito;
        public final String mensaje;
        public ResultadoRegistro(boolean exito, String mensaje) {
            this.exito = exito;
            this.mensaje = mensaje;
        }
    }

    /**
     * Lógica de autenticación de usuario.
     * Valida campos, consulta la base de datos y verifica el rol.
     * @param email Email ingresado
     * @param password Contraseña ingresada
     * @return ResultadoLogin con éxito, mensaje y usuario autenticado (si aplica)
     */
    public ResultadoLogin loginUsuario(String email, String password) {
        if (email == null || email.trim().isEmpty() || password == null || password.isEmpty()) {
            return new ResultadoLogin(false, "Por favor complete todos los campos.", null);
        }
        Usuario usuario = ConexionDB.autenticarUsuario(email, password);
        if (usuario != null && "USUARIO".equals(usuario.getRol())) {
            return new ResultadoLogin(true, "¡Bienvenido!", usuario);
        } else {
            return new ResultadoLogin(false, "Credenciales incorrectas o no es usuario.", null);
        }
    }

    /**
     * Lógica de registro de usuario.
     * Valida campos, verifica duplicados y registra en la base de datos.
     * @param nombre Nombre
     * @param apellido Apellido
     * @param dni DNI
     * @param email Email
     * @param password Contraseña
     * @param passwordConfirm Confirmación de contraseña
     * @return ResultadoRegistro con éxito y mensaje para la vista
     */
    public ResultadoRegistro registrarUsuario(String nombre, String apellido, String dni, String email, String password, String passwordConfirm) {
        if (nombre == null || nombre.trim().isEmpty() ||
            apellido == null || apellido.trim().isEmpty() ||
            dni == null || dni.trim().isEmpty() ||
            email == null || email.trim().isEmpty() ||
            password == null || password.isEmpty() ||
            passwordConfirm == null || passwordConfirm.isEmpty()) {
            return new ResultadoRegistro(false, "Por favor complete todos los campos.");
        }
        if (dni.length() != 8) {
            return new ResultadoRegistro(false, "El DNI debe tener exactamente 8 dígitos.");
        }
        if (!password.equals(passwordConfirm)) {
            return new ResultadoRegistro(false, "Las contraseñas no coinciden.");
        }
        if (ConexionDB.emailExiste(email)) {
            return new ResultadoRegistro(false, "El email ya está registrado.");
        }
        if (ConexionDB.dniExiste(dni)) {
            return new ResultadoRegistro(false, "El DNI ya está registrado.");
        }
        Usuario nuevoUsuario = new Usuario(nombre, apellido, email, password, dni, "USUARIO");
        if (ConexionDB.registrarUsuario(nuevoUsuario)) {
            return new ResultadoRegistro(true, "Usuario registrado exitosamente. Ahora puede iniciar sesión.");
        } else {
            return new ResultadoRegistro(false, "Error al registrar el usuario.");
        }
    }
} 