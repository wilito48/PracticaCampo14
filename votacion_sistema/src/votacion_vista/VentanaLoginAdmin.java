package votacion_vista;

import votacion_util.ConexionDB;
import votacion_modelo.Usuario;
import votacion_controlador.ControladorLoginAdmin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Vista de la ventana de login para administradores.
 * Permite al administrador autenticarse para acceder al panel de administración.
 * Se comunica con el controlador para la lógica de negocio.
 * @author Sistema de Votación
 * @version 2.0
 */
public class VentanaLoginAdmin extends JFrame {
    /** Campo de texto para el email del admin */
    private JTextField txtEmail;
    /** Campo de texto para la contraseña del admin */
    private JPasswordField txtPassword;
    /** Botón para iniciar sesión */
    private JButton btnLogin;
    /** Controlador para la lógica de login (inyectado o instanciado en el constructor) */
    private ControladorLoginAdmin controlador;

    /**
     * Constructor de la ventana de login de administrador.
     */
    public VentanaLoginAdmin() {
        setTitle("Login Administrador");
        setSize(420, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Fondo gradiente
        setContentPane(new PanelGradiente());

        controlador = new ControladorLoginAdmin();
        inicializarComponentes();
        configurarLayout();
        agregarEventos();
    }

    /**
     * Inicializa los componentes visuales de la ventana.
     */
    private void inicializarComponentes() {
        txtEmail = new JTextField(20);
        txtPassword = new JPasswordField(20);
        btnLogin = new JButton("Iniciar Sesión");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnLogin.setBackground(new Color(52, 152, 219));
        btnLogin.setForeground(Color.WHITE); // Mejor contraste
        btnLogin.setFocusPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.setBorder(BorderFactory.createEmptyBorder(12, 24, 12, 24)); // Más espacio a los lados
        btnLogin.setPreferredSize(new Dimension(180, 44)); // Opcional, para ancho fijo
    }

    /**
     * Configura el layout y la disposición de los componentes.
     */
    private void configurarLayout() {
        JPanel panelCentral = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Sombra
                g2.setColor(new Color(0,0,0,30));
                g2.fillRoundRect(8, 8, getWidth()-16, getHeight()-16, 30, 30);
                // Fondo blanco ligeramente azulado
                g2.setColor(new Color(245, 250, 255));
                g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 25, 25);
            }
        };
        panelCentral.setOpaque(false);
        panelCentral.setLayout(new GridBagLayout());
        panelCentral.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        panelCentral.setPreferredSize(new Dimension(340, 320));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 10, 0);
        gbc.anchor = GridBagConstraints.CENTER;

        // Icono de usuario grande (ajusta la ruta si es necesario)
        JLabel lblIcon = new JLabel(new ImageIcon(getClass().getResource("/admin.png")));
        panelCentral.add(lblIcon, gbc);

        // Título
        gbc.gridy = 1;
        JLabel lblTitulo = new JLabel("Acceso Administrador");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(Color.BLACK); // Negro
        panelCentral.add(lblTitulo, gbc);

        // Email label
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 0, 2, 0);
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lblEmail.setHorizontalAlignment(SwingConstants.LEFT);
        lblEmail.setForeground(Color.BLACK); // Negro
        panelCentral.add(lblEmail, gbc);

        // Email field
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 10, 0);
        txtEmail.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtEmail.setPreferredSize(new Dimension(220, 36));
        txtEmail.setMinimumSize(new Dimension(220, 36));
        txtEmail.setMaximumSize(new Dimension(220, 36));
        txtEmail.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1, true),
            BorderFactory.createEmptyBorder(7, 12, 7, 12)
        ));
        panelCentral.add(txtEmail, gbc);

        // Password label
        gbc.gridy = 4;
        gbc.insets = new Insets(5, 0, 2, 0);
        JLabel lblPass = new JLabel("Contraseña:");
        lblPass.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lblPass.setHorizontalAlignment(SwingConstants.LEFT);
        lblPass.setForeground(Color.BLACK); // Negro
        panelCentral.add(lblPass, gbc);

        // Password field
        gbc.gridy = 5;
        gbc.insets = new Insets(0, 0, 15, 0);
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtPassword.setPreferredSize(new Dimension(220, 36));
        txtPassword.setMinimumSize(new Dimension(220, 36));
        txtPassword.setMaximumSize(new Dimension(220, 36));
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1, true),
            BorderFactory.createEmptyBorder(7, 12, 7, 12)
        ));
        panelCentral.add(txtPassword, gbc);

        // Botón de iniciar sesión
        gbc.gridy = 6;
        gbc.insets = new Insets(0, 0, 0, 0);
        panelCentral.add(btnLogin, gbc);

        // Centrado vertical y horizontal
        setLayout(new GridBagLayout());
        add(panelCentral);
    }

    /**
     * Agrega el listener al botón de login.
     * Valida los campos, llama al controlador y muestra mensajes según el resultado.
     */
    private void agregarEventos() {
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = txtEmail.getText().trim();
                String password = new String(txtPassword.getPassword());
                ControladorLoginAdmin.ResultadoLogin resultado = controlador.loginAdmin(email, password);
                if (resultado.exito) {
                    JOptionPane.showMessageDialog(VentanaLoginAdmin.this,
                            "<html><span style='color:black;'>" + resultado.mensaje + "</span></html>",
                            "Acceso Concedido", JOptionPane.INFORMATION_MESSAGE);
                    new PanelAdmin(resultado.usuario).setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(VentanaLoginAdmin.this,
                            "<html><span style='color:black;'>" + resultado.mensaje + "</span></html>",
                            resultado.mensaje.equals("Por favor complete todos los campos.") ? "Campos Vacíos" : "Acceso Denegado",
                            resultado.mensaje.equals("Por favor complete todos los campos.") ? JOptionPane.WARNING_MESSAGE : JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    /**
     * Clase interna para el fondo gradiente de la ventana.
     */
    class PanelGradiente extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            int w = getWidth();
            int h = getHeight();
            Color color1 = new Color(236, 240, 241);
            Color color2 = new Color(189, 195, 199);
            GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, w, h);
        }
    }
}