package votacion_util;


import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Clase de utilidades para el sistema de votación
 * @author Sistema de Votación
 * @version 1.0
 */
public class Utilidades {
    
    /**
     * Formato de fecha para el sistema
     */
    private static final SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    
    /**
     * Genera un timestamp actual formateado
     * @return String con la fecha y hora actual
     */
    public static String obtenerTimestamp() {
        return formatoFecha.format(new Date());
    }
    
    /**
     * Valida si una cadena es un número entero válido
     * @param cadena Cadena a validar
     * @return true si es un número válido, false en caso contrario
     */
    public static boolean esNumeroValido(String cadena) {
        if (cadena == null || cadena.trim().isEmpty()) {
            return false;
        }
        
        try {
            Integer.parseInt(cadena.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * Valida si una cadena no está vacía
     * @param cadena Cadena a validar
     * @return true si no está vacía, false en caso contrario
     */
    public static boolean noEstaVacia(String cadena) {
        return cadena != null && !cadena.trim().isEmpty();
    }
    
    /**
     * Formatea un porcentaje con 2 decimales
     * @param valor Valor a formatear
     * @return String formateado
     */
    public static String formatearPorcentaje(double valor) {
        return String.format("%.2f%%", valor);
    }
    
    /**
     * Genera un mensaje de log con timestamp
     * @param mensaje Mensaje a registrar
     * @return String con timestamp y mensaje
     */
    public static String generarLog(String mensaje) {
        return "[" + obtenerTimestamp() + "] " + mensaje;
    }
    
    /**
     * Valida que un número esté en un rango específico
     * @param numero Número a validar
     * @param minimo Valor mínimo del rango
     * @param maximo Valor máximo del rango
     * @return true si está en el rango, false en caso contrario
     */
    public static boolean estaEnRango(int numero, int minimo, int maximo) {
        return numero >= minimo && numero <= maximo;
    }
    
    /**
     * Limpia una cadena de caracteres especiales
     * @param cadena Cadena a limpiar
     * @return Cadena limpia
     */
    public static String limpiarCadena(String cadena) {
        if (cadena == null) {
            return "";
        }
        return cadena.trim().replaceAll("[^a-zA-Z0-9\\sáéíóúÁÉÍÓÚñÑ]", "");
    }
    
    /**
     * Capitaliza la primera letra de cada palabra
     * @param cadena Cadena a capitalizar
     * @return Cadena capitalizada
     */
    public static String capitalizar(String cadena) {
        if (cadena == null || cadena.trim().isEmpty()) {
            return cadena;
        }
        
        String[] palabras = cadena.trim().toLowerCase().split("\\s+");
        StringBuilder resultado = new StringBuilder();
        
        for (int i = 0; i < palabras.length; i++) {
            if (palabras[i].length() > 0) {
                resultado.append(Character.toUpperCase(palabras[i].charAt(0)));
                resultado.append(palabras[i].substring(1));
            }
            
            if (i < palabras.length - 1) {
                resultado.append(" ");
            }
        }
        
        return resultado.toString();
    }
    
    /**
     * Genera un ID único basado en timestamp
     * @return String con ID único
     */
    public static String generarIdUnico() {
        return "ID_" + System.currentTimeMillis();
    }
    
    /**
     * Valida formato de correo electrónico básico
     * @param email Email a validar
     * @return true si el formato es válido, false en caso contrario
     */
    public static boolean esEmailValido(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        
        String patron = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.trim().matches(patron);
    }
    
    /**
     * Convierte un número a formato ordinal
     * @param numero Número a convertir
     * @return String con formato ordinal
     */
    public static String aFormatoOrdinal(int numero) {
        if (numero <= 0) {
            return String.valueOf(numero);
        }
        
        String[] sufijos = {"º", "º", "º", "º", "º", "º", "º", "º", "º", "º"};
        int ultimoDigito = numero % 10;
        
        if (numero >= 11 && numero <= 13) {
            return numero + "º";
        }
        
        switch (ultimoDigito) {
            case 1:
                return numero + "º";
            case 2:
                return numero + "º";
            case 3:
                return numero + "º";
            default:
                return numero + "º";
        }
    }
} 