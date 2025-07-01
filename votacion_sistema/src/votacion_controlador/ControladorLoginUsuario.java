package votacion_controlador;

import votacion_modelo.Usuario;
import votacion_util.ConexionDB;

public class ControladorLoginUsuario {
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

    public static class ResultadoRegistro {
        public final boolean exito;
        public final String mensaje;
        public ResultadoRegistro(boolean exito, String mensaje) {
            this.exito = exito;
            this.mensaje = mensaje;
        }
    }

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