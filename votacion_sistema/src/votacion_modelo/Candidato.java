package votacion_modelo;

/**
 * Clase que representa un candidato en el sistema de votación
 * @author Sistema de Votación
 * @version 1.0
 */
public class Candidato {
    private int numero;
    private String nombre;
    private String partido;
    private int votos;
    
    /**
     * Constructor por defecto
     */
    public Candidato() {
        this.votos = 0;
    }
    
    /**
     * Constructor con parámetros
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
    
    // Getters y Setters
    public int getNumero() {
        return numero;
    }
    
    public void setNumero(int numero) {
        this.numero = numero;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getPartido() {
        return partido;
    }
    
    public void setPartido(String partido) {
        this.partido = partido;
    }
    
    public int getVotos() {
        return votos;
    }
    
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
     * Calcula el porcentaje de votos del candidato
     * @param totalVotos Total de votos emitidos
     * @return Porcentaje de votos del candidato
     */
    public double calcularPorcentaje(int totalVotos) {
        if (totalVotos == 0) {
            return 0.0;
        }
        return (double) votos / totalVotos * 100;
    }
    
    @Override
    public String toString() {
        return "Candidato #" + numero + " - " + nombre + " (" + partido + ") - Votos: " + votos;
    }
} 