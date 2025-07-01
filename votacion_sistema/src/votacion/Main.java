package votacion;

import votacion_vista.VentanaSeleccionRol;
import votacion_util.ConexionDB;

import javax.swing.*;
import java.awt.Color;

/**
 * Clase principal del sistema de votación electrónica.
 * Inicializa la aplicación, configura la apariencia, conecta con la base de datos
 * y lanza la ventana de selección de rol para el usuario.
 * Incluye utilidades para mostrar información del sistema y personalizar la interfaz.
 * @author Sistema de Votación
 * @version 2.0
 */
public class Main {
    
    /**
     * Método principal de la aplicación. Configura el look and feel, conecta a la base de datos
     * y lanza la ventana principal de selección de rol.
     * @param args Argumentos de línea de comandos
     */
	public static void main(String[] args) {
	    try {
	        for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
	            if ("Nimbus".equals(info.getName())) {
	                UIManager.setLookAndFeel(info.getClassName());
	                break;
	            }
	        }
	    } catch (Exception e) {
	        // Si Nimbus no está disponible, se usa el predeterminado
	    }

	    // Configura tus colores personalizados si quieres (opcional)
	    configurarEstilosPersonalizados();

	    // Conectar con la base de datos
	    if (!conectarBaseDatos()) {
	        return; // Salir si no se puede conectar
	    }

	    // Ejecutar la aplicación en el Event Dispatch Thread (EDT)
	    SwingUtilities.invokeLater(() -> {
	        try {
	            VentanaSeleccionRol ventanaSeleccionRol = new VentanaSeleccionRol();
	            ventanaSeleccionRol.setVisible(true);
	        } catch (Exception e) {
	            JOptionPane.showMessageDialog(null,
	                "Error al iniciar la aplicación: " + e.getMessage(),
	                "Error de Inicialización",
	                JOptionPane.ERROR_MESSAGE);
	            System.exit(1);
	        }
	    });
	}
    
    /**
     * Intenta conectar con la base de datos MySQL.
     * Muestra mensajes de error claros si la conexión falla.
     * @return true si la conexión fue exitosa, false en caso contrario
     */
    private static boolean conectarBaseDatos() {
        try {
            if (ConexionDB.conectar()) {
                System.out.println("Conexión a la base de datos establecida correctamente");
                return true;
            } else {
                JOptionPane.showMessageDialog(null,
                    "No se pudo conectar con la base de datos.\n\n" +
                    "Verifique que:\n" +
                    "• XAMPP esté ejecutándose\n" +
                    "• MySQL esté activo en XAMPP\n" +
                    "• La base de datos 'sistema_votacion' exista\n\n" +
                    "La aplicación se cerrará.",
                    "Error de Conexión",
                    JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                "Error al conectar con la base de datos: " + e.getMessage() + "\n\n" +
                "Verifique que XAMPP esté ejecutándose y MySQL esté activo.",
                "Error de Conexión",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    /**
     * Configura el look and feel del sistema
     */
    private static void configurarLookAndFeel() {
        try {
            // Intentar usar el look and feel del sistema operativo
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
            // Configurar colores y fuentes personalizados
            configurarEstilosPersonalizados();
            
        } catch (Exception e) {
            // Si falla, usar el look and feel por defecto
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception ex) {
                // Si todo falla, continuar con el look and feel por defecto
                System.err.println("No se pudo configurar el look and feel: " + ex.getMessage());
            }
        }
    }
    
    /**
     * Configura estilos y colores personalizados para la interfaz gráfica.
     * Personaliza botones, paneles, campos de texto, menús y etiquetas.
     */
    private static void configurarEstilosPersonalizados() {
        // Configurar colores de la interfaz
        UIManager.put("Button.background", new java.awt.Color(52, 152, 219));
        UIManager.put("Button.foreground", java.awt.Color.WHITE);
        UIManager.put("Button.focus", new java.awt.Color(41, 128, 185));
        
        // Configurar colores de los paneles
        UIManager.put("Panel.background", new java.awt.Color(236, 240, 241));
        
        // Configurar colores de los campos de texto
        UIManager.put("TextField.background", java.awt.Color.WHITE);
        UIManager.put("TextField.foreground", new java.awt.Color(52, 73, 94));
        UIManager.put("TextField.caretForeground", new java.awt.Color(52, 73, 94));
        
        // Configurar colores de las áreas de texto
        UIManager.put("TextArea.background", java.awt.Color.WHITE);
        UIManager.put("TextArea.foreground", new java.awt.Color(52, 73, 94));
        
        // Configurar colores de las etiquetas
        UIManager.put("Label.foreground", new java.awt.Color(52, 73, 94));
        
        // Configurar colores de los menús
        UIManager.put("MenuBar.background", new java.awt.Color(52, 73, 94));
        UIManager.put("MenuBar.foreground", java.awt.Color.WHITE);
        UIManager.put("Menu.background", new java.awt.Color(52, 73, 94));
        UIManager.put("Menu.foreground", java.awt.Color.WHITE);
        UIManager.put("MenuItem.background", new java.awt.Color(52, 73, 94));
        UIManager.put("MenuItem.foreground", java.awt.Color.WHITE);
    }
    
    /**
     * (Opcional) Verifica y muestra información sobre la versión de Java y el sistema operativo.
     * Útil para depuración y soporte técnico.
     */
    private static void verificarVersionJava() {
        String version = System.getProperty("java.version");
        String vendor = System.getProperty("java.vendor");
        
        System.out.println("Información del Sistema:");
        System.out.println("Java Version: " + version);
        System.out.println("Java Vendor: " + vendor);
        System.out.println("Sistema Operativo: " + System.getProperty("os.name"));
        System.out.println("Arquitectura: " + System.getProperty("os.arch"));
        System.out.println("================================");
    }
    
    /**
     * Muestra información detallada del sistema en un cuadro de diálogo.
     * Incluye versión de Java, SO, usuario, memoria, etc.
     * Útil para soporte y depuración.
     */
    public static void mostrarInformacionSistema() {
        StringBuilder info = new StringBuilder();
        info.append("INFORMACIÓN DEL SISTEMA\n");
        info.append("=======================\n\n");
        info.append("Java Version: ").append(System.getProperty("java.version")).append("\n");
        info.append("Java Vendor: ").append(System.getProperty("java.vendor")).append("\n");
        info.append("Sistema Operativo: ").append(System.getProperty("os.name")).append("\n");
        info.append("Versión del SO: ").append(System.getProperty("os.version")).append("\n");
        info.append("Arquitectura: ").append(System.getProperty("os.arch")).append("\n");
        info.append("Usuario: ").append(System.getProperty("user.name")).append("\n");
        info.append("Directorio de trabajo: ").append(System.getProperty("user.dir")).append("\n");
        info.append("Memoria total: ").append(Runtime.getRuntime().totalMemory() / 1024 / 1024).append(" MB\n");
        info.append("Memoria libre: ").append(Runtime.getRuntime().freeMemory() / 1024 / 1024).append(" MB\n");
        
        JTextArea textArea = new JTextArea(info.toString());
        textArea.setEditable(false);
        textArea.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new java.awt.Dimension(500, 300));
        
        JOptionPane.showMessageDialog(null,
            scrollPane,
            "Información del Sistema",
            JOptionPane.INFORMATION_MESSAGE);
    }
}