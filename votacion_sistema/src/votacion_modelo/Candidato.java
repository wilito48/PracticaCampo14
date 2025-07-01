package votacion_modelo;

/**
 * Clase que representa un candidato en el sistema de votación
 * @author Sistema de Votación
 * @version 2.0
 */
public class Candidato {
    /** Número identificador único del candidato */
    private int numero;
    /** Nombre completo del candidato */
    private String nombre;
    /** Partido político al que pertenece el candidato */
    private String partido;
    /** Cantidad de votos recibidos por el candidato */
    private int votos;
    
    /**
     * Constructor por defecto. Inicializa los votos en 0.
     */
    public Candidato() {
        this.votos = 0;
    }
    
    /**
     * Constructor con parámetros para crear un candidato
     * @param numero Número identificador del candidato
     * @param nombre Nombre completo del candidato
     * @param partido Partido político del candidato
     */
    public Candidato(int numero, String nombre, String partido) {
        this.numero = numero;
        this.nombre = nombre;
        this.partido = partido;
        this.votos = 0;
    }
    
    // ===== Getters y Setters =====
    /** @return Número identificador del candidato */
    public int getNumero() {
        return numero;
    }
    /** @param numero Nuevo número identificador */
    public void setNumero(int numero) {
        this.numero = numero;
    }
    /** @return Nombre completo del candidato */
    public String getNombre() {
        return nombre;
    }
    /** @param nombre Nuevo nombre */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    /** @return Partido político del candidato */
    public String getPartido() {
        return partido;
    }
    /** @param partido Nuevo partido */
    public void setPartido(String partido) {
        this.partido = partido;
    }
    /** @return Cantidad de votos recibidos */
    public int getVotos() {
        return votos;
    }
    /** @param votos Nueva cantidad de votos */
    public void setVotos(int votos) {
        this.votos = votos;
    }
    
    /**
     * Incrementa el contador de votos en 1
     */
    public void agregarVoto() {
        this.votos++;
    }
    
    /**
     * Calcula el porcentaje de votos del candidato respecto al total
     * @param totalVotos Total de votos emitidos
     * @return Porcentaje de votos del candidato (0.0 si no hay votos)
     */
    public double calcularPorcentaje(int totalVotos) {
        if (totalVotos == 0) {
            return 0.0;
        }
        return (double) votos / totalVotos * 100;
    }
    
    /**
     * Representación en texto del candidato (útil para depuración y ComboBox)
     */
    @Override
    public String toString() {
        return "Candidato #" + numero + " - " + nombre + " (" + partido + ") - Votos: " + votos;
    }
} 