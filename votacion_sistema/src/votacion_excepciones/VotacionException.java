package votacion_excepciones;

/**
 * Excepción personalizada para el sistema de votación
 * @author Sistema de Votación
 * @version 1.0
 */
public class VotacionException extends Exception {
    
    /**
     * Constructor por defecto
     */
    public VotacionException() {
        super("Error en el sistema de votación");
    }
    
    /**
     * Constructor con mensaje
     * @param mensaje Mensaje de error
     */
    public VotacionException(String mensaje) {
        super(mensaje);
    }
    
    /**
     * Constructor con mensaje y causa
     * @param mensaje Mensaje de error
     * @param causa Causa de la excepción
     */
    public VotacionException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
    
    /**
     * Constructor con causa
     * @param causa Causa de la excepción
     */
    public VotacionException(Throwable causa) {
        super("Error en el sistema de votación", causa);
    }
} 