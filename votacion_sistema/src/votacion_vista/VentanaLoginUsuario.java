package votacion_vista;

import votacion_util.ConexionDB;
import votacion_modelo.Usuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaLoginUsuario extends JFrame {
    // Campos para login
    private JTextField txtEmailLogin;
    private JPasswordField txtPasswordLogin;
    private JButton btnLogin;

    // Campos para registro
    private JTextField txtNombre, txtApellido, txtDni, txtEmailRegistro;
    private JPasswordField txtPasswordRegistro, txtPasswordConfirm;
    private JButton btnRegistrar;

    private JTabbedPane tabbedPane;

    public VentanaLoginUsuario() {
        setTitle("Login Usuario");
        setSize(420, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        inicializarComponentes();
        configurarLayout();
        agregarEventos();
    }

    private void inicializarComponentes() {
        // Login
        txtEmailLogin = new JTextField(20);
        txtPasswordLogin = new JPasswordField(20);
        btnLogin = new JButton("Iniciar Sesión");

        // Registro
        txtNombre = new JTextField(20);
        txtApellido = new JTextField(20);
        txtDni = new JTextField(20);
        txtEmailRegistro = new JTextField(20);
        txtPasswordRegistro = new JPasswordField(20);
        txtPasswordConfirm = new JPasswordField(20);
        btnRegistrar = new JButton("Registrarse");

        tabbedPane = new JTabbedPane();
    }

    private void configurarLayout() {
        // Panel de login
        JPanel panelLogin = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);

        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        panelLogin.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        panelLogin.add(txtEmailLogin, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST;
        panelLogin.add(new JLabel("Contraseña:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        panelLogin.add(txtPasswordLogin, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        panelLogin.add(btnLogin, gbc);

        // Panel de registro
        JPanel panelRegistro = new JPanel(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);

        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        panelRegistro.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        panelRegistro.add(txtNombre, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST;
        panelRegistro.add(new JLabel("Apellido:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        panelRegistro.add(txtApellido, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.EAST;
        panelRegistro.add(new JLabel("DNI:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        panelRegistro.add(txtDni, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.anchor = GridBagConstraints.EAST;
        panelRegistro.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        panelRegistro.add(txtEmailRegistro, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.anchor = GridBagConstraints.EAST;
        panelRegistro.add(new JLabel("Contraseña:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        panelRegistro.add(txtPasswordRegistro, gbc);

        gbc.gridx = 0; gbc.gridy = 5; gbc.anchor = GridBagConstraints.EAST;
        panelRegistro.add(new JLabel("Confirmar Contraseña:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        panelRegistro.add(txtPasswordConfirm, gbc);

        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        panelRegistro.add(btnRegistrar, gbc);

        tabbedPane.addTab("Iniciar Sesión", panelLogin);
        tabbedPane.addTab("Registrarse", panelRegistro);

        add(tabbedPane, BorderLayout.CENTER);
    }

    private void agregarEventos() {
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = txtEmailLogin.getText().trim();
                String password = new String(txtPasswordLogin.getPassword());

                if (email.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(VentanaLoginUsuario.this,
                            "Por favor complete todos los campos.",
                            "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                Usuario usuario = ConexionDB.autenticarUsuario(email, password);
                if (usuario != null && "USUARIO".equals(usuario.getRol())) {
                    JOptionPane.showMessageDialog(VentanaLoginUsuario.this,
                            "¡Bienvenido!",
                            "Acceso Concedido", JOptionPane.INFORMATION_MESSAGE);
                    new PanelVotacionUsuario(usuario).setVisible(true);
                    dispose(); // Cierra la ventana de login
                } else {
                    JOptionPane.showMessageDialog(VentanaLoginUsuario.this,
                            "Credenciales incorrectas o no es usuario.",
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

                if (nombre.isEmpty() || apellido.isEmpty() || dni.isEmpty() ||
                        email.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty()) {
                    JOptionPane.showMessageDialog(VentanaLoginUsuario.this,
                            "Por favor complete todos los campos.",
                            "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (!password.equals(passwordConfirm)) {
                    JOptionPane.showMessageDialog(VentanaLoginUsuario.this,
                            "Las contraseñas no coinciden.",
                            "Error de Validación", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (ConexionDB.emailExiste(email)) {
                    JOptionPane.showMessageDialog(VentanaLoginUsuario.this,
                            "El email ya está registrado.",
                            "Error de Registro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (ConexionDB.dniExiste(dni)) {
                    JOptionPane.showMessageDialog(VentanaLoginUsuario.this,
                            "El DNI ya está registrado.",
                            "Error de Registro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Usuario nuevoUsuario = new Usuario(nombre, apellido, email, password, dni, "USUARIO");
                if (ConexionDB.registrarUsuario(nuevoUsuario)) {
                    JOptionPane.showMessageDialog(VentanaLoginUsuario.this,
                            "Usuario registrado exitosamente. Ahora puede iniciar sesión.",
                            "Registro Exitoso", JOptionPane.INFORMATION_MESSAGE);
                    tabbedPane.setSelectedIndex(0);
                } else {
                    JOptionPane.showMessageDialog(VentanaLoginUsuario.this,
                            "Error al registrar el usuario.",
                            "Error de Registro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}