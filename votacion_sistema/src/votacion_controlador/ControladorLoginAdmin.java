package votacion_controlador;

import votacion_modelo.Usuario;
import votacion_util.ConexionDB;

/**
 * Controlador encargado de la lógica de login de administradores.
 * Separa la lógica de negocio de la vista (VentanaLoginAdmin).
 * @author Sistema de Votación
 * @version 2.0
 */
public class ControladorLoginAdmin {
    /**
     * Clase interna para encapsular el resultado del login de admin.
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
     * Lógica de autenticación de administrador.
     * Valida campos, consulta la base de datos y verifica el rol.
     * @param email Email ingresado
     * @param password Contraseña ingresada
     * @return ResultadoLogin con éxito, mensaje y usuario autenticado (si aplica)
     */
    public ResultadoLogin loginAdmin(String email, String password) {
        if (email == null || email.trim().isEmpty() || password == null || password.isEmpty()) {
            return new ResultadoLogin(false, "Por favor complete todos los campos.", null);
        }
        Usuario usuario = ConexionDB.autenticarUsuario(email, password);
        if (usuario != null && "ADMIN".equals(usuario.getRol())) {
            return new ResultadoLogin(true, "¡Bienvenido, Administrador!", usuario);
        } else {
            return new ResultadoLogin(false, "Credenciales incorrectas o no es administrador.", null);
        }
    }
} 