package votacion_vista;

import votacion_util.ConexionDB;
import votacion_modelo.Usuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaLoginAdmin extends JFrame {
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JButton btnLogin;

    public VentanaLoginAdmin() {
        setTitle("Login Administrador");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        inicializarComponentes();
        configurarLayout();
        agregarEventos();
    }

    private void inicializarComponentes() {
        txtEmail = new JTextField(20);
        txtPassword = new JPasswordField(20);
        btnLogin = new JButton("Iniciar Sesión");
    }

    private void configurarLayout() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        panel.add(txtEmail, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Contraseña:"), gbc);
        gbc.gridx = 1;
        panel.add(txtPassword, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        panel.add(btnLogin, gbc);

        add(panel, BorderLayout.CENTER);
    }

    private void agregarEventos() {
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = txtEmail.getText().trim();
                String password = new String(txtPassword.getPassword());

                if (email.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(VentanaLoginAdmin.this,
                            "Por favor complete todos los campos.",
                            "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                Usuario usuario = ConexionDB.autenticarUsuario(email, password);
                if (usuario != null && "ADMIN".equals(usuario.getRol())) {
                    JOptionPane.showMessageDialog(VentanaLoginAdmin.this,
                            "¡Bienvenido, Administrador!",
                            "Acceso Concedido", JOptionPane.INFORMATION_MESSAGE);
                    new PanelAdmin(usuario).setVisible(true);
                    dispose(); // Cierra la ventana de login
                } else {
                    JOptionPane.showMessageDialog(VentanaLoginAdmin.this,
                            "Credenciales incorrectas o no es administrador.",
                            "Acceso Denegado", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}