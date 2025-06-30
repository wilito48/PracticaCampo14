package votacion_modelo;

import java.util.ArrayList;
import java.util.List;



/**
 * Clase principal que maneja toda la lógica del sistema de votación
 * @author Sistema de Votación
 * @version 1.0
 */
public class SistemaVotacion {
    private List<Candidato> candidatos;
    private int totalVotos;
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
     * Inicia el proceso de votación
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
     * @return true si el voto se registró correctamente, false si el candidato no existe
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
     * @return Lista de candidatos
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
        if (candidato == null) {
            return -1;
        }
        return candidato.calcularPorcentaje(totalVotos);
    }
    
    /**
     * Obtiene estadísticas completas de la votación
     * @return String con las estadísticas formateadas
     */
    public String obtenerEstadisticas() {
        StringBuilder estadisticas = new StringBuilder();
        estadisticas.append("=== ESTADÍSTICAS DE LA VOTACIÓN ===\n");
        estadisticas.append("Total de votos emitidos: ").append(totalVotos).append("\n");
        estadisticas.append("Total de candidatos: ").append(candidatos.size()).append("\n\n");
        
        for (Candidato candidato : candidatos) {
            double porcentaje = candidato.calcularPorcentaje(totalVotos);
            estadisticas.append(String.format("Candidato #%d: %s (%s)\n", 
                candidato.getNumero(), candidato.getNombre(), candidato.getPartido()));
            estadisticas.append(String.format("  Votos: %d (%.2f%%)\n", 
                candidato.getVotos(), porcentaje));
        }
        
        return estadisticas.toString();
    }
} 