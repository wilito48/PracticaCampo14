package votacion_controlador;

import votacion_modelo.Usuario;
import votacion_util.ConexionDB;

public class ControladorLoginAdmin {
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