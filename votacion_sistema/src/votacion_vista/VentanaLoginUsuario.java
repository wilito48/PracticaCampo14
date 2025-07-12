package votacion_vista;

import votacion_util.ConexionDB;
import votacion_modelo.Usuario;
import votacion_controlador.ControladorLoginUsuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.text.*;

/**
 * Vista de la ventana de login y registro para usuarios.
 * Permite a los usuarios autenticarse o registrarse en el sistema.
 * Se comunica con el controlador para la lógica de negocio.
 * @author Sistema de Votación
 * @version 2.0
 */
public class VentanaLoginUsuario extends JFrame {
    // Campos para login
    private JTextField txtEmailLogin;
    private JPasswordField txtPasswordLogin;
    private JButton btnLogin;

    // Campos para registro
    private JTextField txtNombre, txtApellido, txtDni, txtEmailRegistro;
    private JPasswordField txtPasswordRegistro, txtPasswordConfirm;
    private JButton btnRegistrar;

    private JPanel panelContenedor;
    private CardLayout cardLayout;
    private static final String LOGIN = "login";
    private static final String REGISTRO = "registro";

    private ControladorLoginUsuario controlador;

    /**
     * Constructor de la ventana de login/registro de usuario.
     */
    public VentanaLoginUsuario() {
        setTitle("Login Usuario");
        setSize(420, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        controlador = new ControladorLoginUsuario();
        inicializarComponentes();
        configurarLayout();
        agregarEventos();
    }

    /**
     * Inicializa los componentes visuales de la ventana.
     */
    private void inicializarComponentes() {
        // Login
        txtEmailLogin = new JTextField(20);
        txtPasswordLogin = new JPasswordField(20);
        btnLogin = new JButton("Iniciar Sesión");

        // Registro
        txtNombre = new JTextField(20);
        txtApellido = new JTextField(20);
        txtDni = new JTextField(20);
        // Solo números y máximo 8 dígitos para DNI
        ((AbstractDocument) txtDni.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string == null) return;
                StringBuilder sb = new StringBuilder();
                for (char c : string.toCharArray()) {
                    if (Character.isDigit(c)) sb.append(c);
                }
                int newLength = fb.getDocument().getLength() + sb.length();
                if (newLength <= 8) {
                    super.insertString(fb, offset, sb.toString(), attr);
                }
            }
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text == null) return;
                StringBuilder sb = new StringBuilder();
                for (char c : text.toCharArray()) {
                    if (Character.isDigit(c)) sb.append(c);
                }
                int newLength = fb.getDocument().getLength() - length + sb.length();
                if (newLength <= 8) {
                    super.replace(fb, offset, length, sb.toString(), attrs);
                }
            }
        });
        txtEmailRegistro = new JTextField(20);
        txtPasswordRegistro = new JPasswordField(20);
        txtPasswordConfirm = new JPasswordField(20);
        btnRegistrar = new JButton("Registrarse");

        // Panel contenedor con CardLayout
        cardLayout = new CardLayout();
        panelContenedor = new JPanel(cardLayout);
    }

    /**
     * Configura el layout y la disposición de los paneles y componentes.
     */
    private void configurarLayout() {
        // Panel de login moderno
        JPanel panelLoginCentral = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Sombra
                g2.setColor(new Color(0,0,0,30));
                g2.fillRoundRect(8, 8, getWidth()-16, getHeight()-16, 30, 30);
                // Fondo blanco
                g2.setColor(new Color(255, 255, 255));
                g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 25, 25);
            }
        };
        panelLoginCentral.setOpaque(false);
        panelLoginCentral.setLayout(new GridBagLayout());
        panelLoginCentral.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        panelLoginCentral.setPreferredSize(new Dimension(340, 320));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 10, 0);
        gbc.anchor = GridBagConstraints.CENTER;

        // Icono de usuario más pequeño
        JLabel lblIcon = new JLabel();
        lblIcon.setHorizontalAlignment(SwingConstants.CENTER);
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/user.png"));
            Image img = icon.getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            lblIcon.setIcon(new ImageIcon(img));
        } catch (Exception ex) {
            lblIcon.setText("\uD83D\uDC64");
            lblIcon.setFont(new Font("Segoe UI", Font.PLAIN, 36));
        }
        lblIcon.setBorder(BorderFactory.createEmptyBorder(0,0,2,0));
        panelLoginCentral.add(lblIcon, gbc);

        // Título
        gbc.gridy = 1;
        JLabel lblTitulo = new JLabel("Acceso Usuario");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(44, 62, 80));
        panelLoginCentral.add(lblTitulo, gbc);

        // Email label
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 0, 2, 0);
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lblEmail.setHorizontalAlignment(SwingConstants.LEFT);
        lblEmail.setForeground(new Color(44, 62, 80));
        panelLoginCentral.add(lblEmail, gbc);

        // Email field
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 10, 0);
        txtEmailLogin.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtEmailLogin.setPreferredSize(new Dimension(220, 36));
        txtEmailLogin.setMinimumSize(new Dimension(220, 36));
        txtEmailLogin.setMaximumSize(new Dimension(220, 36));
        txtEmailLogin.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1, true),
            BorderFactory.createEmptyBorder(7, 12, 7, 12)
        ));
        panelLoginCentral.add(txtEmailLogin, gbc);

        // Password label
        gbc.gridy = 4;
        gbc.insets = new Insets(5, 0, 2, 0);
        JLabel lblPass = new JLabel("Contraseña:");
        lblPass.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lblPass.setHorizontalAlignment(SwingConstants.LEFT);
        lblPass.setForeground(new Color(44, 62, 80));
        panelLoginCentral.add(lblPass, gbc);

        // Password field
        gbc.gridy = 5;
        gbc.insets = new Insets(0, 0, 15, 0);
        txtPasswordLogin.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtPasswordLogin.setPreferredSize(new Dimension(220, 36));
        txtPasswordLogin.setMinimumSize(new Dimension(220, 36));
        txtPasswordLogin.setMaximumSize(new Dimension(220, 36));
        txtPasswordLogin.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1, true),
            BorderFactory.createEmptyBorder(7, 12, 7, 12)
        ));
        panelLoginCentral.add(txtPasswordLogin, gbc);

        // Botón de iniciar sesión
        gbc.gridy = 6;
        gbc.insets = new Insets(0, 0, 0, 0);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnLogin.setBackground(new Color(52, 152, 219));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.setBorder(BorderFactory.createEmptyBorder(12, 24, 12, 24));
        btnLogin.setPreferredSize(new Dimension(180, 44));
        panelLoginCentral.add(btnLogin, gbc);

        // Enlace para ir a registro (panel horizontal)
        gbc.gridy = 7;
        gbc.insets = new Insets(6, 0, 0, 0);
        JPanel panelEnlace = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panelEnlace.setOpaque(false);
        JLabel lblTexto = new JLabel("¿No tienes cuenta? ");
        lblTexto.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JLabel lblIrRegistro = new JLabel("Regístrate");
        lblIrRegistro.setForeground(new Color(0, 102, 204));
        lblIrRegistro.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblIrRegistro.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblIrRegistro.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
        // Subrayado manual
        lblIrRegistro.setText("<html><u>Regístrate</u></html>");
        panelEnlace.add(lblTexto);
        panelEnlace.add(lblIrRegistro);
        panelLoginCentral.add(panelEnlace, gbc);

        // Panel de registro (diseño anterior, sencillo)
        JPanel panelRegistro = new JPanel(new GridBagLayout());
        GridBagConstraints gbcReg = new GridBagConstraints();
        gbcReg.insets = new Insets(6, 6, 6, 6);

        gbcReg.gridx = 0; gbcReg.gridy = 0; gbcReg.anchor = GridBagConstraints.EAST;
        panelRegistro.add(new JLabel("Nombre:"), gbcReg);
        gbcReg.gridx = 1; gbcReg.anchor = GridBagConstraints.WEST;
        panelRegistro.add(txtNombre, gbcReg);

        gbcReg.gridx = 0; gbcReg.gridy = 1; gbcReg.anchor = GridBagConstraints.EAST;
        panelRegistro.add(new JLabel("Apellido:"), gbcReg);
        gbcReg.gridx = 1; gbcReg.anchor = GridBagConstraints.WEST;
        panelRegistro.add(txtApellido, gbcReg);

        gbcReg.gridx = 0; gbcReg.gridy = 2; gbcReg.anchor = GridBagConstraints.EAST;
        panelRegistro.add(new JLabel("DNI:"), gbcReg);
        gbcReg.gridx = 1; gbcReg.anchor = GridBagConstraints.WEST;
        panelRegistro.add(txtDni, gbcReg);

        gbcReg.gridx = 0; gbcReg.gridy = 3; gbcReg.anchor = GridBagConstraints.EAST;
        panelRegistro.add(new JLabel("Email:"), gbcReg);
        gbcReg.gridx = 1; gbcReg.anchor = GridBagConstraints.WEST;
        panelRegistro.add(txtEmailRegistro, gbcReg);

        gbcReg.gridx = 0; gbcReg.gridy = 4; gbcReg.anchor = GridBagConstraints.EAST;
        panelRegistro.add(new JLabel("Contraseña:"), gbcReg);
        gbcReg.gridx = 1; gbcReg.anchor = GridBagConstraints.WEST;
        panelRegistro.add(txtPasswordRegistro, gbcReg);

        gbcReg.gridx = 0; gbcReg.gridy = 5; gbcReg.anchor = GridBagConstraints.EAST;
        panelRegistro.add(new JLabel("Confirmar Contraseña:"), gbcReg);
        gbcReg.gridx = 1; gbcReg.anchor = GridBagConstraints.WEST;
        panelRegistro.add(txtPasswordConfirm, gbcReg);

        gbcReg.gridx = 0; gbcReg.gridy = 6; gbcReg.gridwidth = 2; gbcReg.anchor = GridBagConstraints.CENTER;
        panelRegistro.add(btnRegistrar, gbcReg);

        gbcReg.gridy = 7; gbcReg.gridwidth = 2;
        JLabel lblIrLogin = new JLabel("¿Ya tienes cuenta? Inicia sesión");
        lblIrLogin.setForeground(new Color(41, 128, 185));
        lblIrLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panelRegistro.add(lblIrLogin, gbcReg);

        // Añadir paneles al contenedor
        panelContenedor.add(panelLoginCentral, LOGIN);
        panelContenedor.add(panelRegistro, REGISTRO);
        add(panelContenedor, BorderLayout.CENTER);

        // Eventos para cambiar de formulario
        lblIrRegistro.addMouseListener(new java.awt.event.MouseAdapter() {
            Color azulNormal = new Color(0, 102, 204);
            Color azulOscuro = new Color(0, 51, 153);
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cardLayout.show(panelContenedor, REGISTRO);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lblIrRegistro.setForeground(azulOscuro);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                lblIrRegistro.setForeground(azulNormal);
            }
        });
        lblIrLogin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cardLayout.show(panelContenedor, LOGIN);
            }
        });
    }

    /**
     * Agrega los listeners y acciones a los botones de login y registro.
     * Valida los campos, llama al controlador y muestra mensajes según el resultado.
     */
    private void agregarEventos() {
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = txtEmailLogin.getText().trim();
                String password = new String(txtPasswordLogin.getPassword());
                ControladorLoginUsuario.ResultadoLogin resultado = controlador.loginUsuario(email, password);
                if (resultado.exito) {
                    JOptionPane.showMessageDialog(VentanaLoginUsuario.this,
                            resultado.mensaje,
                            "Acceso Concedido", JOptionPane.INFORMATION_MESSAGE);
                    new PanelVotacionUsuario(resultado.usuario).setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(VentanaLoginUsuario.this,
                            resultado.mensaje,
                            "Acceso Denegado", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre = txtNombre.getText().trim();
                String apellido = txtApellido.getText().trim();
                String dni = txtDni.getText().trim();
                String email = txtEmailRegistro.getText().trim();
                String password = new String(txtPasswordRegistro.getPassword());
                String passwordConfirm = new String(txtPasswordConfirm.getPassword());
                ControladorLoginUsuario.ResultadoRegistro resultado = controlador.registrarUsuario(nombre, apellido, dni, email, password, passwordConfirm);
                if (resultado.exito) {
                    JOptionPane.showMessageDialog(VentanaLoginUsuario.this,
                            resultado.mensaje,
                            "Registro Exitoso", JOptionPane.INFORMATION_MESSAGE);
                    cardLayout.show(panelContenedor, LOGIN);
                } else {
                    JOptionPane.showMessageDialog(VentanaLoginUsuario.this,
                            resultado.mensaje,
                            "Error de Registro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}