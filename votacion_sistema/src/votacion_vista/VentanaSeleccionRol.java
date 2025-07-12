package votacion_vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.border.TitledBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 * Vista de la ventana de selección de rol.
 * Permite al usuario elegir entre acceso de administrador o usuario.
 * Solo gestiona la navegación a las ventanas de login correspondientes.
 * @author Sistema de Votación
 * @version 2.0
 */
public class VentanaSeleccionRol extends JFrame {
    /**
     * Constructor de la ventana de selección de rol.
     */
    public VentanaSeleccionRol() {
        inicializarVentana();
        inicializarComponentes();
        configurarLayout();
    }

    /**
     * Inicializa las propiedades principales de la ventana.
     */
    private void inicializarVentana() {
        setTitle("Sistema de Votación - Selección de Acceso");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(520, 430);
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
     * Inicializa los componentes visuales de la ventana.
     */
    private void inicializarComponentes() {
        // Los componentes se crean en configurarLayout()
    }

    /**
     * Configura el layout y la disposición de los paneles y componentes.
     */
    private void configurarLayout() {
        setContentPane(new PanelGradiente());
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
     * Crea el panel superior con el título y subtítulo.
     */
    private JPanel crearPanelTitulo() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(25, 20, 10, 20));

        JLabel lblTitulo = new JLabel("SISTEMA DE VOTACIÓN ELECTRÓNICA");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel lblSubtitulo = new JLabel("Seleccione su tipo de acceso");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.ITALIC, 15));
        lblSubtitulo.setForeground(new Color(220, 220, 220));
        lblSubtitulo.setHorizontalAlignment(SwingConstants.CENTER);

        panel.setLayout(new BorderLayout());
        panel.add(lblTitulo, BorderLayout.CENTER);
        panel.add(lblSubtitulo, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel crearPanelCentral() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(20, 40, 20, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);

        // Panel para el botón de admin
        JPanel panelAdmin = crearPanelBoton(
                "ADMINISTRADOR",
                "ACCESO DE ADMINISTRADOR",
                "/admin.png",
                new Color(231, 76, 60),
                "<html><center>• Registrar candidatos<br>• Gestionar votaciones<br>• Ver resultados detallados<br>• Administrar usuarios</center></html>",
                e -> abrirVentanaAdmin()
        );

        // Panel para el botón de usuario
        JPanel panelUsuario = crearPanelBoton(
                "USUARIO",
                "ACCESO DE USUARIO",
                "/user.png",
                new Color(46, 204, 113),
                "<html><center>• Registrarse en el sistema<br>• Iniciar sesión<br>• Votar por candidatos<br>• Ver resultados básicos</center></html>",
                e -> abrirVentanaUsuario()
        );

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(panelAdmin, gbc);

        gbc.gridy = 1;
        
        panel.add(panelUsuario, gbc);

        return panel;
    }

    // MÉTODO MEJORADO PARA QUE LOS CUADROS SE VEAN COMPLETOS Y EL TEXTO LEGIBLE
    private JPanel crearPanelBoton(String titulo, String textoBoton, String iconoPath, Color color, String descripcion, java.awt.event.ActionListener action) {
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Sombra
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(new Color(0, 0, 0, 30));
                g2.fillRoundRect(5, 5, getWidth() - 10, getHeight() - 10, 30, 30);
            }
        };
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createCompoundBorder(
        	    BorderFactory.createTitledBorder(
        	        new LineBorder(color, 2, true),
        	        titulo,
        	        TitledBorder.CENTER,
        	        TitledBorder.TOP,
        	        new Font("Segoe UI", Font.BOLD, 15),
        	        color
        	    ),
        	    new EmptyBorder(0, 20, 0, 20) // top, left, bottom, right
        	));
        panel.setPreferredSize(new Dimension(370, 140)); // Tamaño igual para ambos
        

        // Panel interno para expandir el contenido
        JPanel panelInterno = new JPanel(new BorderLayout());
        panelInterno.setOpaque(false);

     // Botón con icono
        JButton boton = new JButton(textoBoton, new ImageIcon(getClass().getResource(iconoPath)));
        boton.setPreferredSize(new Dimension(340, 60));
        boton.setFont(new Font("Segoe UI", Font.BOLD, 17));
        boton.setBackground(color);
        boton.setForeground(new Color(44, 62, 80)); // Azul oscuro
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setHorizontalAlignment(SwingConstants.LEFT);
        boton.setIconTextGap(30); // Más espacio entre icono y texto
        boton.setBorder(BorderFactory.createCompoundBorder(
            new RoundBorder(color.darker(), 18),
            new EmptyBorder(0, 30, 0, 20) // top, left, bottom, right
        ));

        // Efecto hover
        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                boton.setBackground(color.darker());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                boton.setBackground(color);
            }
        });

        boton.addActionListener(action);

        // Descripción
        JLabel lblDesc = new JLabel(descripcion);
        lblDesc.setHorizontalAlignment(SwingConstants.CENTER);
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblDesc.setForeground(new Color(44, 62, 80));
        lblDesc.setBorder(new EmptyBorder(10, 0, 0, 0));

        // Añadir el botón arriba y la descripción abajo, ambos expandibles
        panelInterno.add(boton, BorderLayout.NORTH);
        panelInterno.add(lblDesc, BorderLayout.CENTER);

        // El panel interno ocupa todo el centro del panel principal
        panel.add(panelInterno, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelInferior() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel lblInfo = new JLabel("© 2025 Sistema de Votación Electrónica - Versión 2.0");
        lblInfo.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblInfo.setForeground(new Color(200, 200, 200));

        panel.add(lblInfo);

        return panel;
    }

    private void abrirVentanaAdmin() {
        this.setVisible(false);
        SwingUtilities.invokeLater(() -> {
            VentanaLoginAdmin ventanaAdmin = new VentanaLoginAdmin();
            ventanaAdmin.setVisible(true);
        });
    }

    private void abrirVentanaUsuario() {
        this.setVisible(false);
        SwingUtilities.invokeLater(() -> {
            VentanaLoginUsuario ventanaUsuario = new VentanaLoginUsuario();
            ventanaUsuario.setVisible(true);
        });
    }

    // Panel con fondo gradiente
    class PanelGradiente extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            int w = getWidth();
            int h = getHeight();
            Color color1 = new Color(52, 73, 94);
            Color color2 = new Color(44, 62, 80);
            GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, w, h);
        }
    }

    // Borde redondeado para los botones
    class RoundBorder extends LineBorder {
        private int radius;
        public RoundBorder(Color color, int radius) {
            super(color, 2, true);
            this.radius = radius;
        }
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.setColor(lineColor);
            g.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }
    }
}