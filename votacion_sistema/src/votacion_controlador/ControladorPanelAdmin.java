package votacion_controlador;

import votacion_modelo.Candidato;
import votacion_modelo.Usuario;
import votacion_util.ConexionDB;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ControladorPanelAdmin {
    public static class ResultadoOperacion {
        public final boolean exito;
        public final String mensaje;
        public ResultadoOperacion(boolean exito, String mensaje) {
            this.exito = exito;
            this.mensaje = mensaje;
        }
    }

    public ResultadoOperacion registrarCandidato(int numero, String nombre, String partido) {
        if (nombre == null || nombre.trim().isEmpty() || partido == null || partido.trim().isEmpty()) {
            return new ResultadoOperacion(false, "Complete todos los campos.");
        }
        List<Candidato> candidatosExistentes = ConexionDB.obtenerCandidatos();
        for (Candidato c : candidatosExistentes) {
            if (c.getNumero() == numero) {
                return new ResultadoOperacion(false, "Ya existe un candidato con ese número.");
            }
            if (c.getNombre().equalsIgnoreCase(nombre)) {
                return new ResultadoOperacion(false, "Ya existe un candidato con ese nombre.");
            }
        }
        Candidato candidato = new Candidato(numero, nombre, partido);
        if (ConexionDB.registrarCandidato(candidato)) {
            return new ResultadoOperacion(true, "Candidato registrado correctamente.");
        } else {
            return new ResultadoOperacion(false, "Error al registrar candidato.");
        }
    }

    public ResultadoOperacion iniciarVotacion() {
        List<Candidato> candidatosExistentes = ConexionDB.obtenerCandidatos();
        if (candidatosExistentes.isEmpty()) {
            return new ResultadoOperacion(false, "Debe registrar al menos un candidato antes de iniciar la votación.");
        }
        return new ResultadoOperacion(true, "¡Votación iniciada! Los usuarios ya pueden votar.");
    }

    public ResultadoOperacion finalizarVotacion() {
        return new ResultadoOperacion(true, "¡Votación finalizada!");
    }

    public ResultadoOperacion reiniciarVotacion() {
        ConexionDB.reiniciarVotos();
        return new ResultadoOperacion(true, "¡Nueva votación iniciada! Todos los usuarios pueden votar de nuevo.");
    }

    public ResultadoOperacion exportarResultados(String contenido, File archivo) {
        try (FileWriter writer = new FileWriter(archivo)) {
            writer.write(contenido);
            return new ResultadoOperacion(true, "Resultados exportados correctamente.");
        } catch (IOException e) {
            return new ResultadoOperacion(false, "Error al exportar resultados: " + e.getMessage());
        }
    }

    public List<Candidato> obtenerCandidatos() {
        return ConexionDB.obtenerCandidatos();
    }

    public List<Usuario> obtenerUsuarios() {
        return ConexionDB.obtenerUsuarios();
    }
} 