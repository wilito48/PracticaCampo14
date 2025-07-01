package votacion_modelo;

/**
 * Clase que representa un usuario del sistema de votación
 * @author Sistema de Votación
 * @version 2.0
 */
public class Usuario {
    /** Identificador único del usuario en la base de datos */
    private int id;
    /** Nombre del usuario */
    private String nombre;
    /** Apellido del usuario */
    private String apellido;
    /** Email del usuario (debe ser único) */
    private String email;
    /** Contraseña del usuario (en texto plano, idealmente debería estar hasheada) */
    private String password;
    /** DNI del usuario (debe ser único) */
    private String dni;
    /** Indica si el usuario ya ha votado en la elección actual */
    private boolean yaVoto;
    /** Rol del usuario: puede ser "ADMIN" o "USUARIO" */
    private String rol;
    
    /**
     * Constructor completo para un usuario (usado al cargar desde la base de datos)
     * @param id ID único
     * @param nombre Nombre
     * @param apellido Apellido
     * @param email Email
     * @param password Contraseña
     * @param dni DNI
     * @param rol Rol ("ADMIN" o "USUARIO")
     */
    public Usuario(int id, String nombre, String apellido, String email, String password, String dni, String rol) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.password = password;
        this.dni = dni;
        this.yaVoto = false;
        this.rol = rol;
    }
    
    /**
     * Constructor para registro de nuevos usuarios (sin ID, que lo asigna la base de datos)
     * @param nombre Nombre
     * @param apellido Apellido
     * @param email Email
     * @param password Contraseña
     * @param dni DNI
     * @param rol Rol ("ADMIN" o "USUARIO")
     */
    public Usuario(String nombre, String apellido, String email, String password, String dni, String rol) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.password = password;
        this.dni = dni;
        this.yaVoto = false;
        this.rol = rol;
    }
    
    // ===== Getters y Setters =====
    /** @return ID del usuario */
    public int getId() {
        return id;
    }
    /** @param id Nuevo ID del usuario */
    public void setId(int id) {
        this.id = id;
    }
    /** @return Nombre del usuario */
    public String getNombre() {
        return nombre;
    }
    /** @param nombre Nuevo nombre */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    /** @return Apellido del usuario */
    public String getApellido() {
        return apellido;
    }
    /** @param apellido Nuevo apellido */
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
    /** @return Email del usuario */
    public String getEmail() {
        return email;
    }
    /** @param email Nuevo email */
    public void setEmail(String email) {
        this.email = email;
    }
    /** @return Contraseña del usuario */
    public String getPassword() {
        return password;
    }
    /** @param password Nueva contraseña */
    public void setPassword(String password) {
        this.password = password;
    }
    /** @return DNI del usuario */
    public String getDni() {
        return dni;
    }
    /** @param dni Nuevo DNI */
    public void setDni(String dni) {
        this.dni = dni;
    }
    /** @return true si el usuario ya votó, false si no */
    public boolean isYaVoto() {
        return yaVoto;
    }
    /** @param yaVoto Nuevo estado de votación */
    public void setYaVoto(boolean yaVoto) {
        this.yaVoto = yaVoto;
    }
    /** @return Rol del usuario ("ADMIN" o "USUARIO") */
    public String getRol() {
        return rol;
    }
    /** @param rol Nuevo rol */
    public void setRol(String rol) {
        this.rol = rol;
    }
    
    /**
     * Devuelve el nombre completo del usuario
     * @return Nombre y apellido concatenados
     */
    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }
    
    /**
     * Verifica si el usuario es administrador
     * @return true si el rol es "ADMIN", false en caso contrario
     */
    public boolean esAdmin() {
        return "ADMIN".equals(rol);
    }
    
    /**
     * Representación en texto del usuario (útil para depuración)
     */
    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", email='" + email + '\'' +
                ", dni='" + dni + '\'' +
                ", yaVoto=" + yaVoto +
                ", rol='" + rol + '\'' +
                '}';
    }
}