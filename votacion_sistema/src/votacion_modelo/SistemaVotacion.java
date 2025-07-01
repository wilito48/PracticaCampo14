package votacion_modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase principal que maneja toda la lógica del sistema de votación en memoria.
 * Permite registrar candidatos, iniciar/finalizar votación, registrar votos, obtener resultados, etc.
 * @author Sistema de Votación
 * @version 2.0
 */
public class SistemaVotacion {
    /** Lista de candidatos registrados en la elección */
    private List<Candidato> candidatos;
    /** Total de votos emitidos en la elección */
    private int totalVotos;
    /** Indica si la votación está activa */
    private boolean votacionActiva;
    
    /**
     * Constructor que inicializa el sistema de votación
     */
    public SistemaVotacion() {
        this.candidatos = new ArrayList<>();
        this.totalVotos = 0;
        this.votacionActiva = false;
    }
    
    /**
     * Registra un nuevo candidato en el sistema
     * @param numero Número identificador del candidato
     * @param nombre Nombre completo del candidato
     * @param partido Partido político del candidato
     * @return true si se registró correctamente, false si ya existe
     */
    public boolean registrarCandidato(int numero, String nombre, String partido) {
        // Verificar si ya existe un candidato con ese número
        for (Candidato candidato : candidatos) {
            if (candidato.getNumero() == numero) {
                return false;
            }
        }
        Candidato nuevoCandidato = new Candidato(numero, nombre, partido);
        candidatos.add(nuevoCandidato);
        return true;
    }
    
    /**
     * Inicia el proceso de votación. Reinicia los votos de todos los candidatos.
     */
    public void iniciarVotacion() {
        this.votacionActiva = true;
        this.totalVotos = 0;
        // Reiniciar contadores de votos
        for (Candidato candidato : candidatos) {
            candidato.setVotos(0);
        }
    }
    
    /**
     * Finaliza el proceso de votación
     */
    public void finalizarVotacion() {
        this.votacionActiva = false;
    }
    
    /**
     * Registra un voto para un candidato específico
     * @param numeroCandidato Número del candidato por el cual votar
     * @return true si el voto se registró correctamente, false si el candidato no existe o la votación no está activa
     */
    public boolean registrarVoto(int numeroCandidato) {
        if (!votacionActiva) {
            return false;
        }
        for (Candidato candidato : candidatos) {
            if (candidato.getNumero() == numeroCandidato) {
                candidato.agregarVoto();
                totalVotos++;
                return true;
            }
        }
        return false;
    }
    
    /**
     * Obtiene la lista de todos los candidatos
     * @return Lista de candidatos (copia para evitar modificaciones externas)
     */
    public List<Candidato> getCandidatos() {
        return new ArrayList<>(candidatos);
    }
    
    /**
     * Obtiene un candidato específico por su número
     * @param numero Número del candidato
     * @return El candidato si existe, null en caso contrario
     */
    public Candidato getCandidatoPorNumero(int numero) {
        for (Candidato candidato : candidatos) {
            if (candidato.getNumero() == numero) {
                return candidato;
            }
        }
        return null;
    }
    
    /**
     * Obtiene el total de votos emitidos
     * @return Total de votos
     */
    public int getTotalVotos() {
        return totalVotos;
    }
    
    /**
     * Verifica si la votación está activa
     * @return true si la votación está activa, false en caso contrario
     */
    public boolean isVotacionActiva() {
        return votacionActiva;
    }
    
    /**
     * Determina el ganador de la elección
     * @return El candidato ganador, null si hay empate o no hay votos
     */
    public Candidato determinarGanador() {
        if (candidatos.isEmpty() || totalVotos == 0) {
            return null;
        }
        Candidato ganador = candidatos.get(0);
        int maxVotos = ganador.getVotos();
        for (Candidato candidato : candidatos) {
            if (candidato.getVotos() > maxVotos) {
                ganador = candidato;
                maxVotos = candidato.getVotos();
            }
        }
        // Verificar si hay empate
        int candidatosConMaxVotos = 0;
        for (Candidato candidato : candidatos) {
            if (candidato.getVotos() == maxVotos) {
                candidatosConMaxVotos++;
            }
        }
        // Si hay más de un candidato con el máximo de votos, hay empate
        if (candidatosConMaxVotos > 1) {
            return null; // Empate
        }
        return ganador;
    }
    
    /**
     * Verifica si hay empate en la elección
     * @return true si hay empate, false en caso contrario
     */
    public boolean hayEmpate() {
        return determinarGanador() == null && totalVotos > 0;
    }
    
    /**
     * Obtiene los candidatos empatados
     * @return Lista de candidatos empatados
     */
    public List<Candidato> getCandidatosEmpatados() {
        List<Candidato> empatados = new ArrayList<>();
        if (candidatos.isEmpty() || totalVotos == 0) {
            return empatados;
        }
        // Encontrar el máximo número de votos
        int maxVotos = 0;
        for (Candidato candidato : candidatos) {
            if (candidato.getVotos() > maxVotos) {
                maxVotos = candidato.getVotos();
            }
        }
        // Agregar todos los candidatos con el máximo de votos
        for (Candidato candidato : candidatos) {
            if (candidato.getVotos() == maxVotos) {
                empatados.add(candidato);
            }
        }
        return empatados;
    }
    
    /**
     * Calcula el porcentaje de votos para un candidato específico
     * @param numeroCandidato Número del candidato
     * @return Porcentaje de votos, -1 si el candidato no existe
     */
    public double calcularPorcentajeCandidato(int numeroCandidato) {
        Candidato candidato = getCandidatoPorNumero(numeroCandidato);
        if (candidato == null || totalVotos == 0) {
            return -1;
        }
        return (double) candidato.getVotos() / totalVotos * 100;
    }
    
    /**
     * Devuelve una cadena con estadísticas de la votación
     * @return Estadísticas en formato texto
     */
    public String obtenerEstadisticas() {
        StringBuilder sb = new StringBuilder();
        sb.append("ESTADÍSTICAS DE LA VOTACIÓN\n");
        sb.append("===========================\n");
        sb.append("Total de votos emitidos: ").append(totalVotos).append("\n");
        for (Candidato c : candidatos) {
            sb.append("Candidato #").append(c.getNumero()).append(": ")
              .append(c.getNombre()).append(" (" + c.getPartido() + ") - Votos: ")
              .append(c.getVotos()).append(" (" + String.format("%.2f", calcularPorcentajeCandidato(c.getNumero())) + "%)\n");
        }
        return sb.toString();
    }
} 