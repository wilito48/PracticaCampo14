package votacion_excepciones;

/**
 * Excepción personalizada para el sistema de votación.
 * Permite identificar y manejar errores específicos de la lógica de votación,
 * como validaciones, operaciones inválidas o problemas de negocio.
 * @author Sistema de Votación
 * @version 2.0
 */
public class VotacionException extends Exception {
    
    /**
     * Constructor por defecto. Lanza un mensaje genérico de error de votación.
     */
    public VotacionException() {
        super("Error en el sistema de votación");
    }
    
    /**
     * Constructor con mensaje personalizado.
     * @param mensaje Mensaje de error
     */
    public VotacionException(String mensaje) {
        super(mensaje);
    }
    
    /**
     * Constructor con mensaje y causa de la excepción.
     * @param mensaje Mensaje de error
     * @param causa Causa de la excepción
     */
    public VotacionException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
    
    /**
     * Constructor con causa de la excepción.
     * @param causa Causa de la excepción
     */
    public VotacionException(Throwable causa) {
        super("Error en el sistema de votación", causa);
    }
} 