package votacion_modelo;

/**
 * Clase que representa un usuario del sistema de votación
 * @author Sistema de Votación
 * @version 1.0
 */
public class Usuario {
    private int id;
    private String nombre;
    private String apellido;
    private String email;
    private String password;
    private String dni;
    private boolean yaVoto;
    private String rol; // "ADMIN" o "USUARIO"
    
    /**
     * Constructor completo para un usuario
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
     * Constructor para registro de nuevos usuarios
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
    
    // Getters y Setters existentes...
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getApellido() {
        return apellido;
    }
    
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getDni() {
        return dni;
    }
    
    public void setDni(String dni) {
        this.dni = dni;
    }
    
    public boolean isYaVoto() {
        return yaVoto;
    }
    
    public void setYaVoto(boolean yaVoto) {
        this.yaVoto = yaVoto;
    }
    
    public String getRol() {
        return rol;
    }
    
    public void setRol(String rol) {
        this.rol = rol;
    }
    
    /**
     * Obtiene el nombre completo del usuario
     * @return Nombre completo
     */
    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }
    
    /**
     * Verifica si el usuario es administrador
     * @return true si es administrador, false en caso contrario
     */
    public boolean esAdmin() {
        return "ADMIN".equals(rol);
    }
    
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