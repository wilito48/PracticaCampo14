package votacion_controlador;

import votacion_modelo.Candidato;
import votacion_modelo.SistemaVotacion;
import votacion_excepciones.VotacionException;
import votacion_util.Utilidades;

import java.util.List;

/**
 * Controlador que maneja la lógica de negocio entre la vista y el modelo
 * @author Sistema de Votación
 * @version 1.0
 */
public class ControladorVotacion {
    
    private SistemaVotacion sistemaVotacion;
    
    /**
     * Constructor del controlador
     */
    public ControladorVotacion() {
        this.sistemaVotacion = new SistemaVotacion();
    }
    
    /**
     * Registra un nuevo candidato
     * @param numero Número del candidato
     * @param nombre Nombre del candidato
     * @param partido Partido del candidato
     * @throws VotacionException Si hay error en el registro
     */
    public void registrarCandidato(int numero, String nombre, String partido) throws VotacionException {
        try {
            // Validaciones
            if (numero <= 0) {
                throw new VotacionException("El número del candidato debe ser positivo");
            }
            
            if (!Utilidades.noEstaVacia(nombre)) {
                throw new VotacionException("El nombre del candidato es obligatorio");
            }
            
            if (!Utilidades.noEstaVacia(partido)) {
                throw new VotacionException("El partido del candidato es obligatorio");
            }
            
            // Limpiar y capitalizar datos
            nombre = Utilidades.capitalizar(Utilidades.limpiarCadena(nombre));
            partido = Utilidades.capitalizar(Utilidades.limpiarCadena(partido));
            
            // Registrar candidato
            boolean registrado = sistemaVotacion.registrarCandidato(numero, nombre, partido);
            
            if (!registrado) {
                throw new VotacionException("Ya existe un candidato con el número " + numero);
            }
            
        } catch (VotacionException e) {
            throw e;
        } catch (Exception e) {
            throw new VotacionException("Error al registrar candidato: " + e.getMessage(), e);
        }
    }
    
    /**
     * Inicia el proceso de votación
     * @throws VotacionException Si hay error al iniciar la votación
     */
    public void iniciarVotacion() throws VotacionException {
        try {
            if (sistemaVotacion.getCandidatos().isEmpty()) {
                throw new VotacionException("Debe registrar al menos un candidato antes de iniciar la votación");
            }
            
            sistemaVotacion.iniciarVotacion();
            
        } catch (VotacionException e) {
            throw e;
        } catch (Exception e) {
            throw new VotacionException("Error al iniciar la votación: " + e.getMessage(), e);
        }
    }
    
    /**
     * Finaliza el proceso de votación
     * @throws VotacionException Si hay error al finalizar la votación
     */
    public void finalizarVotacion() throws VotacionException {
        try {
            sistemaVotacion.finalizarVotacion();
        } catch (Exception e) {
            throw new VotacionException("Error al finalizar la votación: " + e.getMessage(), e);
        }
    }
    
    /**
     * Registra un voto para un candidato
     * @param numeroCandidato Número del candidato
     * @throws VotacionException Si hay error al registrar el voto
     */
    public void registrarVoto(int numeroCandidato) throws VotacionException {
        try {
            if (!sistemaVotacion.isVotacionActiva()) {
                throw new VotacionException("La votación no está activa");
            }
            
            if (numeroCandidato <= 0) {
                throw new VotacionException("El número del candidato debe ser positivo");
            }
            
            boolean votoRegistrado = sistemaVotacion.registrarVoto(numeroCandidato);
            
            if (!votoRegistrado) {
                throw new VotacionException("No existe un candidato con el número " + numeroCandidato);
            }
            
        } catch (VotacionException e) {
            throw e;
        } catch (Exception e) {
            throw new VotacionException("Error al registrar voto: " + e.getMessage(), e);
        }
    }
    
    /**
     * Obtiene la lista de candidatos
     * @return Lista de candidatos
     */
    public List<Candidato> obtenerCandidatos() {
        return sistemaVotacion.getCandidatos();
    }
    
    /**
     * Obtiene un candidato por su número
     * @param numero Número del candidato
     * @return El candidato si existe, null en caso contrario
     */
    public Candidato obtenerCandidatoPorNumero(int numero) {
        return sistemaVotacion.getCandidatoPorNumero(numero);
    }
    
    /**
     * Obtiene el total de votos emitidos
     * @return Total de votos
     */
    public int obtenerTotalVotos() {
        return sistemaVotacion.getTotalVotos();
    }
    
    /**
     * Verifica si la votación está activa
     * @return true si está activa, false en caso contrario
     */
    public boolean isVotacionActiva() {
        return sistemaVotacion.isVotacionActiva();
    }
    
    /**
     * Determina el ganador de la elección
     * @return El candidato ganador, null si hay empate o no hay votos
     */
    public Candidato determinarGanador() {
        return sistemaVotacion.determinarGanador();
    }
    
    /**
     * Verifica si hay empate
     * @return true si hay empate, false en caso contrario
     */
    public boolean hayEmpate() {
        return sistemaVotacion.hayEmpate();
    }
    
    /**
     * Obtiene los candidatos empatados
     * @return Lista de candidatos empatados
     */
    public List<Candidato> obtenerCandidatosEmpatados() {
        return sistemaVotacion.getCandidatosEmpatados();
    }
    
    /**
     * Calcula el porcentaje de votos para un candidato
     * @param numeroCandidato Número del candidato
     * @return Porcentaje de votos, -1 si el candidato no existe
     */
    public double calcularPorcentajeCandidato(int numeroCandidato) {
        return sistemaVotacion.calcularPorcentajeCandidato(numeroCandidato);
    }
    
    /**
     * Obtiene estadísticas completas de la votación
     * @return String con las estadísticas formateadas
     */
    public String obtenerEstadisticas() {
        return sistemaVotacion.obtenerEstadisticas();
    }
    
    /**
     * Valida si se puede iniciar la votación
     * @return true si se puede iniciar, false en caso contrario
     */
    public boolean sePuedeIniciarVotacion() {
        return !sistemaVotacion.getCandidatos().isEmpty() && !sistemaVotacion.isVotacionActiva();
    }
    
    /**
     * Valida si se puede finalizar la votación
     * @return true si se puede finalizar, false en caso contrario
     */
    public boolean sePuedeFinalizarVotacion() {
        return sistemaVotacion.isVotacionActiva();
    }
    
    /**
     * Obtiene información del estado actual del sistema
     * @return String con información del estado
     */
    public String obtenerEstadoSistema() {
        StringBuilder estado = new StringBuilder();
        estado.append("Estado del Sistema de Votación\n");
        estado.append("==============================\n");
        estado.append("Candidatos registrados: ").append(sistemaVotacion.getCandidatos().size()).append("\n");
        estado.append("Total de votos: ").append(sistemaVotacion.getTotalVotos()).append("\n");
        estado.append("Votación activa: ").append(sistemaVotacion.isVotacionActiva() ? "Sí" : "No").append("\n");
        
        if (sistemaVotacion.getTotalVotos() > 0) {
            if (sistemaVotacion.hayEmpate()) {
                estado.append("Resultado: EMPATE\n");
            } else {
                Candidato ganador = sistemaVotacion.determinarGanador();
                if (ganador != null) {
                    estado.append("Ganador: ").append(ganador.getNombre()).append("\n");
                }
            }
        }
        
        return estado.toString();
    }
    
    /**
     * Reinicia el sistema de votación
     * @throws VotacionException Si hay error al reiniciar
     */
    public void reiniciarSistema() throws VotacionException {
        try {
            if (sistemaVotacion.isVotacionActiva()) {
                throw new VotacionException("No se puede reiniciar el sistema mientras la votación está activa");
            }
            
            this.sistemaVotacion = new SistemaVotacion();
            
        } catch (VotacionException e) {
            throw e;
        } catch (Exception e) {
            throw new VotacionException("Error al reiniciar el sistema: " + e.getMessage(), e);
        }
    }
} 