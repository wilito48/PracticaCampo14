package votacion_vista;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.TitledBorder;

/**
 * Ventana para seleccionar el tipo de acceso (Admin o Usuario)
 * @author Sistema de Votación
 * @version 1.0
 */
public class VentanaSeleccionRol extends JFrame {
    
    /**
     * Constructor de la ventana
     */
    public VentanaSeleccionRol() {
        inicializarVentana();
        inicializarComponentes();
        configurarLayout();
        agregarEventos();
    }
    
    /**
     * Inicializa la configuración básica de la ventana
     */
    private void inicializarVentana() {
        setTitle("Sistema de Votación - Selección de Acceso");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Configurar icono
        try {
            setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icono.png")));
        } catch (Exception e) {
            // Si no se encuentra el icono, continuar sin él
        }
    }
    
    /**
     * Inicializa los componentes de la interfaz
     */
    private void inicializarComponentes() {
        // Los componentes se crean en configurarLayout()
    }
    
    /**
     * Configura el layout de la ventana
     */
    private void configurarLayout() {
        setLayout(new BorderLayout());
        
        // Panel superior con título
        JPanel panelTitulo = crearPanelTitulo();
        add(panelTitulo, BorderLayout.NORTH);
        
        // Panel central con botones
        JPanel panelCentral = crearPanelCentral();
        add(panelCentral, BorderLayout.CENTER);
        
        // Panel inferior con información
        JPanel panelInferior = crearPanelInferior();
        add(panelInferior, BorderLayout.SOUTH);
    }
    
    /**
     * Crea el panel del título
     */
    private JPanel crearPanelTitulo() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(52, 73, 94));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel lblTitulo = new JLabel("SISTEMA DE VOTACIÓN ELECTRÓNICA");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel lblSubtitulo = new JLabel("Seleccione su tipo de acceso");
        lblSubtitulo.setFont(new Font("Arial", Font.ITALIC, 14));
        lblSubtitulo.setForeground(new Color(189, 195, 199));
        lblSubtitulo.setHorizontalAlignment(SwingConstants.CENTER);
        
        panel.setLayout(new BorderLayout());
        panel.add(lblTitulo, BorderLayout.CENTER);
        panel.add(lblSubtitulo, BorderLayout.SOUTH);
        
        return panel;
    }
    
    /**
     * Crea el panel central con botones
     */
    private JPanel crearPanelCentral() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        
        // Botón de Administrador
        JButton btnAdmin = new JButton("ACCESO DE ADMINISTRADOR");
        btnAdmin.setPreferredSize(new Dimension(300, 80));
        btnAdmin.setFont(new Font("Arial", Font.BOLD, 16));
        btnAdmin.setBackground(new Color(231, 76, 60));
        btnAdmin.setForeground(Color.WHITE);
        btnAdmin.setFocusPainted(false);
        
        // Panel para el botón de admin
        JPanel panelAdmin = new JPanel(new BorderLayout());
        panelAdmin.setBorder(BorderFactory.createTitledBorder(
        	    BorderFactory.createLineBorder(new Color(231, 76, 60), 2),
        	    "ADMINISTRADOR",
        	    TitledBorder.CENTER,
        	    TitledBorder.TOP,
        	    new Font("Arial", Font.BOLD, 12),
        	    new Color(231, 76, 60)
        	));
        
        JLabel lblAdminDesc = new JLabel("<html><center>• Registrar candidatos<br>• Gestionar votaciones<br>• Ver resultados detallados<br>• Administrar usuarios</center></html>");
        lblAdminDesc.setHorizontalAlignment(SwingConstants.CENTER);
        lblAdminDesc.setFont(new Font("Arial", Font.PLAIN, 12));
        
        panelAdmin.add(btnAdmin, BorderLayout.NORTH);
        panelAdmin.add(lblAdminDesc, BorderLayout.CENTER);
        
        // Botón de Usuario
        JButton btnUsuario = new JButton("ACCESO DE USUARIO");
        btnUsuario.setPreferredSize(new Dimension(300, 80));
        btnUsuario.setFont(new Font("Arial", Font.BOLD, 16));
        btnUsuario.setBackground(new Color(46, 204, 113));
        btnUsuario.setForeground(Color.WHITE);
        btnUsuario.setFocusPainted(false);
        
        // Panel para el botón de usuario
        JPanel panelUsuario = new JPanel(new BorderLayout());
        panelUsuario.setBorder(BorderFactory.createTitledBorder(
        	    BorderFactory.createLineBorder(new Color(46, 204, 113), 2),
        	    "USUARIO",
        	    TitledBorder.CENTER,
        	    TitledBorder.TOP,
        	    new Font("Arial", Font.BOLD, 12),
        	    new Color(46, 204, 113)
        	));
        
        JLabel lblUsuarioDesc = new JLabel("<html><center>• Registrarse en el sistema<br>• Iniciar sesión<br>• Votar por candidatos<br>• Ver resultados básicos</center></html>");
        lblUsuarioDesc.setHorizontalAlignment(SwingConstants.CENTER);
        lblUsuarioDesc.setFont(new Font("Arial", Font.PLAIN, 12));
        
        panelUsuario.add(btnUsuario, BorderLayout.NORTH);
        panelUsuario.add(lblUsuarioDesc, BorderLayout.CENTER);
        
        // Agregar al panel principal
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(panelAdmin, gbc);
        
        gbc.gridy = 1;
        panel.add(panelUsuario, gbc);
        
        // Agregar eventos
        btnAdmin.addActionListener(e -> abrirVentanaAdmin());
        btnUsuario.addActionListener(e -> abrirVentanaUsuario());
        
        return panel;
    }
    
    /**
     * Crea el panel inferior con información
     */
    private JPanel crearPanelInferior() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(236, 240, 241));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        JLabel lblInfo = new JLabel("© 2024 Sistema de Votación Electrónica - Versión 2.0");
        lblInfo.setFont(new Font("Arial", Font.PLAIN, 10));
        lblInfo.setForeground(new Color(127, 140, 141));
        
        panel.add(lblInfo);
        
        return panel;
    }
    
    /**
     * Agrega los eventos a los componentes
     */
    private void agregarEventos() {
        // Los eventos se agregaron en crearPanelCentral()
    }
    
    /**
     * Abre la ventana de administrador
     */
    private void abrirVentanaAdmin() {
        this.setVisible(false);
        SwingUtilities.invokeLater(() -> {
            VentanaLoginAdmin ventanaAdmin = new VentanaLoginAdmin();
            ventanaAdmin.setVisible(true);
        });
    }
    
    /**
     * Abre la ventana de usuario
     */
    private void abrirVentanaUsuario() {
        this.setVisible(false);
        SwingUtilities.invokeLater(() -> {
            VentanaLoginUsuario ventanaUsuario = new VentanaLoginUsuario();
            ventanaUsuario.setVisible(true);
        });
    }
}