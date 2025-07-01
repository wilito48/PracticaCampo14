package votacion_vista;

import votacion_modelo.Candidato;
import votacion_modelo.Usuario;
import votacion_util.ConexionDB;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class PanelVotacionUsuario extends JFrame {
    private Usuario usuario;
    private JComboBox<Candidato> comboCandidatos;
    private JButton btnVotar;

    public PanelVotacionUsuario(Usuario usuario) {
        this.usuario = usuario;
        setTitle("Panel de Usuario - Votación");
        setSize(520, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Fondo gradiente
        setContentPane(new PanelGradiente());

        inicializarComponentes();
        configurarLayout();
        agregarEventos();
        cargarCandidatos();
    }

    private void inicializarComponentes() {
        comboCandidatos = new JComboBox<>();
        comboCandidatos.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        comboCandidatos.setPreferredSize(new Dimension(320, 36));
        comboCandidatos.setMaximumSize(new Dimension(320, 36));
        comboCandidatos.setMinimumSize(new Dimension(320, 36));
        comboCandidatos.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(52, 152, 219), 1, true),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        comboCandidatos.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Candidato) {
                    Candidato c = (Candidato) value;
                    label.setText("#" + c.getNumero() + " - " + c.getNombre() + " (" + c.getPartido() + ")");
                }
                return label;
            }
        });

        btnVotar = new JButton("VOTAR");
        btnVotar.setFont(new Font("Segoe UI", Font.BOLD, 17));
        btnVotar.setBackground(new Color(46, 204, 113));
        btnVotar.setForeground(Color.WHITE);
        btnVotar.setFocusPainted(false);
        btnVotar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnVotar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(39, 174, 96), 1, true),
            BorderFactory.createEmptyBorder(10, 35, 10, 35)
        ));
        btnVotar.setPreferredSize(new Dimension(160, 44));
        btnVotar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnVotar.setBackground(new Color(39, 174, 96));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnVotar.setBackground(new Color(46, 204, 113));
            }
        });
    }

    private void configurarLayout() {
        JPanel panelCentral = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Sombra
                g2.setColor(new Color(0,0,0,30));
                g2.fillRoundRect(8, 8, getWidth()-16, getHeight()-16, 28, 28);
                // Fondo blanco
                g2.setColor(new Color(255, 255, 255));
                g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 22, 22);
            }
        };
        panelCentral.setOpaque(false);
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
        panelCentral.setBorder(BorderFactory.createEmptyBorder(24, 32, 24, 32));
        panelCentral.setMaximumSize(new Dimension(420, 380));
        panelCentral.setPreferredSize(new Dimension(420, 380));

        // Icono de votación
        JLabel lblIcon = new JLabel();
        lblIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/icono_votacion.png"));
            Image img = icon.getImage().getScaledInstance(56, 56, Image.SCALE_SMOOTH);
            lblIcon.setIcon(new ImageIcon(img));
        } catch (Exception ex) {
            lblIcon.setText("🗳️");
            lblIcon.setFont(new Font("Segoe UI", Font.PLAIN, 38));
        }
        lblIcon.setBorder(BorderFactory.createEmptyBorder(0,0,6,0));
        panelCentral.add(lblIcon);

        // Título
        JLabel lblTitulo = new JLabel("VOTACIÓN ELECTRÓNICA");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(44, 62, 80));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelCentral.add(lblTitulo);

        // Usuario
        JLabel lblUsuario = new JLabel("Bienvenido: " + usuario.getNombre() + " " + usuario.getApellido());
        lblUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lblUsuario.setForeground(new Color(52, 73, 94));
        lblUsuario.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblUsuario.setBorder(BorderFactory.createEmptyBorder(8,0,10,0));
        panelCentral.add(lblUsuario);

        // Label candidato
        JLabel lblCandidato = new JLabel("Seleccione su candidato:");
        lblCandidato.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblCandidato.setForeground(new Color(44, 62, 80));
        lblCandidato.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblCandidato.setBorder(BorderFactory.createEmptyBorder(10,0,6,0));
        panelCentral.add(lblCandidato);

        // Combo candidatos
        comboCandidatos.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelCentral.add(comboCandidatos);

        // Botón votar
        btnVotar.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelCentral.add(Box.createRigidArea(new Dimension(0, 18)));
        panelCentral.add(btnVotar);

        // Centrado vertical y horizontal
        setLayout(new GridBagLayout());
        add(panelCentral);
    }

    private void agregarEventos() {
        btnVotar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (usuario.isYaVoto()) {
                    JOptionPane.showMessageDialog(PanelVotacionUsuario.this, 
                        "Ya has votado.", "Voto Duplicado", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                Candidato candidato = (Candidato) comboCandidatos.getSelectedItem();
                if (candidato == null) {
                    JOptionPane.showMessageDialog(PanelVotacionUsuario.this, 
                        "Seleccione un candidato.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int candidatoId = ConexionDB.obtenerIdCandidato(candidato.getNumero());
                if (ConexionDB.registrarVoto(candidatoId, usuario.getId())) {
                    usuario.setYaVoto(true);
                    JOptionPane.showMessageDialog(PanelVotacionUsuario.this, 
                        "¡Voto registrado exitosamente!", "Voto", JOptionPane.INFORMATION_MESSAGE);
                    btnVotar.setEnabled(false);
                    btnVotar.setText("Ya votaste");
                    btnVotar.setBackground(new Color(149, 165, 166));
                } else {
                    JOptionPane.showMessageDialog(PanelVotacionUsuario.this, 
                        "Error al registrar el voto.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void cargarCandidatos() {
        comboCandidatos.removeAllItems();
        List<Candidato> candidatos = ConexionDB.obtenerCandidatos();
        for (Candidato c : candidatos) {
            comboCandidatos.addItem(c);
        }
    }

    // Panel con fondo gradiente
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
