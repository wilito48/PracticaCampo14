package votacion_vista;

import votacion_modelo.Candidato;
import votacion_modelo.Usuario;
import votacion_util.ConexionDB;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class PanelAdmin extends JFrame {
    private Usuario admin;
    private JTextField txtNumero, txtNombre, txtPartido;
    private JButton btnRegistrarCandidato, btnIniciarVotacion, btnFinalizarVotacion, btnVerUsuarios, btnVerCandidatos;
    private JTextArea txtResultados, txtUsuarios, txtCandidatos;

    public PanelAdmin(Usuario admin) {
        this.admin = admin;
        setTitle("Panel de Administrador");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        inicializarComponentes();
        configurarLayout();
        agregarEventos();
    }

    private void inicializarComponentes() {
        txtNumero = new JTextField();
        txtNumero.setPreferredSize(new Dimension(60, 25));
        txtNombre = new JTextField();
        txtNombre.setPreferredSize(new Dimension(120, 25));
        txtPartido = new JTextField();
        txtPartido.setPreferredSize(new Dimension(120, 25));

        btnRegistrarCandidato = new JButton("Registrar Candidato");
        btnIniciarVotacion = new JButton("Iniciar Votación");
        btnFinalizarVotacion = new JButton("Finalizar Votación");
        btnVerUsuarios = new JButton("Ver Usuarios");
        btnVerCandidatos = new JButton("Ver Candidatos");

        // Aquí agregas el tamaño preferido de los botones de acciones
        btnVerUsuarios.setPreferredSize(new Dimension(140, 32));
        btnVerCandidatos.setPreferredSize(new Dimension(140, 32));

        txtResultados = new JTextArea(12, 40);
        txtResultados.setEditable(false);
        txtResultados.setFont(new Font("Consolas", Font.PLAIN, 14));
        txtUsuarios = new JTextArea(12, 25);
        txtUsuarios.setEditable(false);
        txtUsuarios.setFont(new Font("Consolas", Font.PLAIN, 14));
        txtCandidatos = new JTextArea(12, 25);
        txtCandidatos.setEditable(false);
        txtCandidatos.setFont(new Font("Consolas", Font.PLAIN, 14));
    }

    private void configurarLayout() {
        GradientPanel mainPanel = new GradientPanel();
        mainPanel.setLayout(new BorderLayout(18, 18));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));

        // ENCABEZADO SUPERIOR
        JPanel headerPanel = new JPanel(new BorderLayout(12, 0));
        headerPanel.setOpaque(false);
        // Icono admin
        JLabel lblIcon = new JLabel();
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/admin.png"));
            Image img = icon.getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            lblIcon.setIcon(new ImageIcon(img));
        } catch (Exception ex) {
            lblIcon.setText("\uD83D\uDC68\u200D\uD83D\uDCBB"); // emoji admin
            lblIcon.setFont(new Font("Segoe UI", Font.PLAIN, 36));
        }
        lblIcon.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
        headerPanel.add(lblIcon, BorderLayout.WEST);
        // Textos
        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        JLabel lblTitulo = new JLabel("Panel de Administración");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitulo.setForeground(new Color(0, 90, 160));
        JLabel lblAdmin = new JLabel(  admin.getNombre() + " (" + admin.getEmail() + ")");
        lblAdmin.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblAdmin.setForeground(new Color(44, 62, 80));
        textPanel.add(lblTitulo);
        textPanel.add(lblAdmin);
        headerPanel.add(textPanel, BorderLayout.CENTER);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0,0,18,0));
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Panel de registro de candidato
        RoundedPanel panelCandidato = new RoundedPanel();
        panelCandidato.setLayout(new GridBagLayout());
        panelCandidato.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEmptyBorder(), "Registrar Candidato", 0, 0, new Font("Arial", Font.BOLD, 16), new Color(0, 90, 160)
        ));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(7, 10, 7, 10);
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;

        JLabel lblNumero = new JLabel("Número:");
        lblNumero.setFont(new Font("Arial", Font.BOLD, 14));
        lblNumero.setForeground(new Color(20, 30, 50));
        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setFont(new Font("Arial", Font.BOLD, 14));
        lblNombre.setForeground(new Color(20, 30, 50));
        JLabel lblPartido = new JLabel("Partido:");
        lblPartido.setFont(new Font("Arial", Font.BOLD, 14));
        lblPartido.setForeground(new Color(20, 30, 50));

        gbc.gridx = 0; panelCandidato.add(lblNumero, gbc);
        gbc.gridx = 1; panelCandidato.add(txtNumero, gbc);
        gbc.gridx = 2; panelCandidato.add(lblNombre, gbc);
        gbc.gridx = 3; panelCandidato.add(txtNombre, gbc);
        gbc.gridx = 4; panelCandidato.add(lblPartido, gbc);
        gbc.gridx = 5; panelCandidato.add(txtPartido, gbc);
        gbc.gridx = 6; panelCandidato.add(btnRegistrarCandidato, gbc);

        // Panel de control de votación
        RoundedPanel panelVotacion = new RoundedPanel();
        panelVotacion.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 12));
        panelVotacion.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEmptyBorder(), "                                                                                 Control de Votación", 0, 0, new Font("Arial", Font.BOLD, 16), new Color(0, 90, 160)
        ));
        panelVotacion.add(btnIniciarVotacion);
        panelVotacion.add(btnFinalizarVotacion);

        // Panel de acciones para usuarios
        JPanel panelAccionesUsuarios = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 5));
        panelAccionesUsuarios.setOpaque(false);
        btnVerUsuarios.setPreferredSize(new Dimension(140, 32));
        panelAccionesUsuarios.add(btnVerUsuarios);

        // Panel de acciones para candidatos
        JPanel panelAccionesCandidatos = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 5));
        panelAccionesCandidatos.setOpaque(false);
        btnVerCandidatos.setPreferredSize(new Dimension(140, 32));
        panelAccionesCandidatos.add(btnVerCandidatos);

        // Panel de usuarios registrados
        RoundedPanel panelUsuarios = new RoundedPanel();
        panelUsuarios.setLayout(new BorderLayout(10, 10));
        panelUsuarios.setPreferredSize(new Dimension(320, 0));
        panelUsuarios.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEmptyBorder(), "Usuarios Registrados", 0, 0, new Font("Arial", Font.BOLD, 16), new Color(0, 90, 160)
        ));
        panelUsuarios.add(panelAccionesUsuarios, BorderLayout.NORTH);
        JScrollPane scrollUsuarios = new JScrollPane(txtUsuarios);
        scrollUsuarios.setBorder(BorderFactory.createEmptyBorder());
        panelUsuarios.add(scrollUsuarios, BorderLayout.CENTER);

        // Panel de candidatos registrados
        RoundedPanel panelCandidatos = new RoundedPanel();
        panelCandidatos.setLayout(new BorderLayout(10, 10));
        panelCandidatos.setPreferredSize(new Dimension(320, 0));
        panelCandidatos.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEmptyBorder(), "Candidatos Registrados", 0, 0, new Font("Arial", Font.BOLD, 16), new Color(0, 90, 160)
        ));
        panelCandidatos.add(panelAccionesCandidatos, BorderLayout.NORTH);
        JScrollPane scrollCandidatos = new JScrollPane(txtCandidatos);
        scrollCandidatos.setBorder(BorderFactory.createEmptyBorder());
        panelCandidatos.add(scrollCandidatos, BorderLayout.CENTER);

        // Panel de resultados
        RoundedPanel panelResultados = new RoundedPanel();
        panelResultados.setLayout(new BorderLayout(10, 10));
        panelResultados.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEmptyBorder(), "Resultados", 0, 0, new Font("Arial", Font.BOLD, 16), new Color(0, 90, 160)
        ));
        JScrollPane scrollResultados = new JScrollPane(txtResultados);
        scrollResultados.setBorder(BorderFactory.createEmptyBorder());
        panelResultados.add(scrollResultados, BorderLayout.CENTER);

        // Panel superior (registro + control)
        JPanel panelTop = new JPanel();
        panelTop.setOpaque(false);
        panelTop.setLayout(new GridLayout(2, 1, 0, 12));
        panelTop.add(panelCandidato);
        panelTop.add(panelVotacion);

        // Panel inferior (usuarios, candidatos, resultados)
        JPanel panelInferior = new JPanel(new GridLayout(1, 3, 12, 0));
        panelInferior.setOpaque(false);
        panelInferior.add(panelUsuarios);
        panelInferior.add(panelCandidatos);
        panelInferior.add(panelResultados);

        mainPanel.add(panelTop, BorderLayout.CENTER);
        mainPanel.add(panelInferior, BorderLayout.SOUTH);

        setContentPane(mainPanel);

        // Botones modernos
        for (JButton btn : new JButton[]{btnRegistrarCandidato, btnIniciarVotacion, btnFinalizarVotacion, btnVerCandidatos, btnVerUsuarios}) {
            btn.setFocusPainted(false);
            btn.setBackground(new Color(0, 120, 200));
            btn.setForeground(Color.WHITE);
            btn.setFont(new Font("Arial", Font.BOLD, 13));
            btn.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
            btn.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    btn.setBackground(new Color(0, 90, 160));
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    btn.setBackground(new Color(0, 120, 200));
                }
            });
        }
    }

    private void agregarEventos() {
        btnRegistrarCandidato.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int numero = Integer.parseInt(txtNumero.getText().trim());
                    String nombre = txtNombre.getText().trim();
                    String partido = txtPartido.getText().trim();
                    if (nombre.isEmpty() || partido.isEmpty()) {
                        JOptionPane.showMessageDialog(PanelAdmin.this, "Complete todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    Candidato candidato = new Candidato(numero, nombre, partido);
                    if (ConexionDB.registrarCandidato(candidato)) {
                        JOptionPane.showMessageDialog(PanelAdmin.this, "Candidato registrado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        txtNumero.setText("");
                        txtNombre.setText("");
                        txtPartido.setText("");
                    } else {
                        JOptionPane.showMessageDialog(PanelAdmin.this, "Error al registrar candidato.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(PanelAdmin.this, "Número inválido.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnIniciarVotacion.addActionListener(e -> {
            ConexionDB.reiniciarVotos();
            JOptionPane.showMessageDialog(PanelAdmin.this, "¡Votación iniciada! Los usuarios ya pueden votar.", "Votación", JOptionPane.INFORMATION_MESSAGE);
        });

        btnFinalizarVotacion.addActionListener(e -> {
            mostrarResultados();
        });

        btnVerUsuarios.addActionListener(e -> {
            mostrarUsuarios();
        });

        btnVerCandidatos.addActionListener(e -> {
            mostrarCandidatos();
        });
    }

    private void mostrarResultados() {
        List<Candidato> candidatos = ConexionDB.obtenerCandidatos();
        StringBuilder sb = new StringBuilder();
        sb.append("RESULTADOS DE LA VOTACIÓN\n");
        sb.append("=========================\n\n");
        int totalVotos = 0;
        for (Candidato c : candidatos) {
            sb.append("Candidato ").append(c.getNumero()).append(": ").append(c.getNombre()).append(" (").append(c.getPartido()).append(")\n");
            sb.append("Votos: ").append(c.getVotos()).append("\n");
            sb.append("------------------------\n");
            totalVotos += c.getVotos();
        }
        sb.append("\nTotal de votos emitidos: ").append(totalVotos).append("\n");
        txtResultados.setText(sb.toString());
    }

    private void mostrarUsuarios() {
        List<Usuario> usuarios = ConexionDB.obtenerUsuarios(); // Debe existir este método
        StringBuilder sb = new StringBuilder();
        sb.append("\nID | Nombre           | Apellido         | Email\n");
        sb.append("================================================================\n");
        for (Usuario u : usuarios) {
            sb.append(String.format("%-2d | %-16s | %-16s | %s\n",
                u.getId(), u.getNombre(), u.getApellido(), u.getEmail()));
        }
        if (usuarios.isEmpty()) {
            sb.append("No hay usuarios registrados.\n");
        }
        txtUsuarios.setText(sb.toString());
    }

    // Mostrar candidatos registrados
    private void mostrarCandidatos() {
        List<Candidato> candidatos = ConexionDB.obtenerCandidatos();
        StringBuilder sb = new StringBuilder();
        sb.append("\nN° | Nombre          | Partido\n");
        sb.append("===============================\n");
        for (Candidato c : candidatos) {
            sb.append(String.format("%-2d | %-15s | %s\n", c.getNumero(), c.getNombre(), c.getPartido()));
        }
        if (candidatos.isEmpty()) {
            sb.append("No hay candidatos registrados.\n");
        }
        txtCandidatos.setText(sb.toString());
    }
}

// Panel con fondo degradado
class GradientPanel extends JPanel {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        int w = getWidth(), h = getHeight();
        Color color1 = new Color(210, 222, 235); // azul-gris suave
        Color color2 = new Color(170, 190, 210); // azul-gris más oscuro
        GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, w, h);
    }
}

// Panel con bordes redondeados y sombra
class RoundedPanel extends JPanel {
    public RoundedPanel() {
        setOpaque(false);
    }
    @Override
    protected void paintComponent(Graphics g) {
        int arc = 25;
        int shadowGap = 4;
        int w = getWidth();
        int h = getHeight();
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Sombra
        g2.setColor(new Color(0, 0, 0, 25));
        g2.fillRoundRect(shadowGap, shadowGap, w - shadowGap * 2, h - shadowGap * 2, arc, arc);

        // Fondo panel (menos blanco, más gris-azulado)
        g2.setColor(new Color(235, 240, 245, 235));
        g2.fillRoundRect(0, 0, w - shadowGap, h - shadowGap, arc, arc);

        super.paintComponent(g);
        g2.dispose();
    }
}