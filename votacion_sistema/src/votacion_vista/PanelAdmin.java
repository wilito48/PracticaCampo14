package votacion_vista;

import votacion_modelo.Candidato;
import votacion_modelo.Usuario;
import votacion_util.ConexionDB;
import votacion_controlador.ControladorPanelAdmin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.Timer;
import javax.swing.JFileChooser;
import javax.swing.table.*;

/**
 * Vista principal del panel de administración.
 * Permite al administrador gestionar candidatos, usuarios, controlar la votación y exportar resultados.
 * Se comunica con el controlador para la lógica de negocio.
 * @author Sistema de Votación
 * @version 2.0
 */
public class PanelAdmin extends JFrame {
    /** Usuario administrador autenticado */
    private Usuario admin;
    // Campos de registro de candidato
    private JTextField txtNumero, txtNombre, txtPartido;
    // Botones de acciones principales
    private JButton btnRegistrarCandidato, btnIniciarVotacion, btnFinalizarVotacion, btnVerUsuarios, btnVerCandidatos, btnReiniciarVotacion, btnExportarResultados, btnCerrarSesion;
    // Áreas de texto para mostrar información
    private JTextArea txtResultados, txtUsuarios, txtCandidatos;
    // Tabla para mostrar resultados visuales
    private JTable tablaResultados;
    private DefaultTableModel modeloTablaResultados;
    // Controlador para la lógica de negocio
    private ControladorPanelAdmin controlador;

    /**
     * Constructor del panel de administración.
     * @param admin Usuario administrador autenticado
     */
    public PanelAdmin(Usuario admin) {
        this.admin = admin;
        setTitle("Panel de Administrador");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        controlador = new ControladorPanelAdmin();
        inicializarComponentes();
        configurarLayout();
        agregarEventos();
        // Timer para refrescar resultados cada 2 segundos
        Timer timerResultados = new Timer(2000, e -> mostrarResultados());
        timerResultados.start();
    }

    /**
     * Inicializa los componentes visuales del panel.
     */
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
        btnReiniciarVotacion = new JButton("Reiniciar Votación");
        btnExportarResultados = new JButton("Exportar Resultados");

        // Cargar y escalar el icono de logout
        ImageIcon iconoLogout = null;
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/logout.png"));
            Image img = icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
            iconoLogout = new ImageIcon(img);
        } catch (Exception ex) {
            // Si no se encuentra el icono, el botón solo tendrá texto
        }
        if (iconoLogout != null) {
            btnCerrarSesion = new JButton("Cerrar Sesión", iconoLogout);
        } else {
            btnCerrarSesion = new JButton("Cerrar Sesión");
        }
        btnCerrarSesion.setFocusPainted(false);
        btnCerrarSesion.setBackground(new Color(0, 120, 200));
        btnCerrarSesion.setForeground(Color.WHITE);
        btnCerrarSesion.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnCerrarSesion.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 90, 160), 2, true),
            BorderFactory.createEmptyBorder(8, 18, 8, 18)
        ));
        btnCerrarSesion.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrarSesion.setHorizontalTextPosition(SwingConstants.RIGHT);
        btnCerrarSesion.setIconTextGap(8);
        btnCerrarSesion.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCerrarSesion.setBackground(new Color(0, 90, 160));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCerrarSesion.setBackground(new Color(0, 120, 200));
            }
        });

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

        // Tabla de resultados
        modeloTablaResultados = new DefaultTableModel(new Object[]{"N°", "Nombre", "Partido", "Votos", "Porcentaje"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaResultados = new JTable(modeloTablaResultados) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                int votos = Integer.parseInt(getValueAt(row, 3).toString());
                int maxVotos = 0;
                for (int i = 0; i < getRowCount(); i++) {
                    int v = Integer.parseInt(getValueAt(i, 3).toString());
                    if (v > maxVotos) maxVotos = v;
                }
                if (votos == maxVotos && maxVotos > 0) {
                    c.setBackground(new Color(200, 255, 200)); // Verde claro para ganador
                } else {
                    c.setBackground(Color.WHITE);
                }
                if (isCellSelected(row, column)) {
                    c.setBackground(new Color(184, 207, 229));
                }
                return c;
            }
        };
        tablaResultados.setRowHeight(28);
        tablaResultados.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tablaResultados.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        // Barra de progreso en columna porcentaje
        tablaResultados.getColumnModel().getColumn(4).setCellRenderer(new ProgressBarRenderer());
    }

    /**
     * Configura el layout y la disposición de los paneles y componentes.
     */
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
        headerPanel.add(btnCerrarSesion, BorderLayout.EAST);
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
        panelVotacion.add(btnReiniciarVotacion);

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
        // Tabla de resultados debajo del área de texto
        JScrollPane scrollTabla = new JScrollPane(tablaResultados);
        scrollTabla.setPreferredSize(new Dimension(0, 140));
        panelResultados.add(scrollTabla, BorderLayout.NORTH);
        JPanel panelBotonExportar = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panelBotonExportar.setOpaque(false);
        panelBotonExportar.add(btnExportarResultados);
        panelResultados.add(panelBotonExportar, BorderLayout.SOUTH);

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

        // Botón moderno para reiniciar
        btnReiniciarVotacion.setFocusPainted(false);
        btnReiniciarVotacion.setBackground(new Color(0, 120, 200));
        btnReiniciarVotacion.setForeground(Color.WHITE);
        btnReiniciarVotacion.setFont(new Font("Arial", Font.BOLD, 13));
        btnReiniciarVotacion.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        btnReiniciarVotacion.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnReiniciarVotacion.setBackground(new Color(0, 90, 160));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnReiniciarVotacion.setBackground(new Color(0, 120, 200));
            }
        });

        // Botón moderno para exportar
        btnExportarResultados.setFocusPainted(false);
        btnExportarResultados.setBackground(new Color(0, 120, 200));
        btnExportarResultados.setForeground(Color.WHITE);
        btnExportarResultados.setFont(new Font("Arial", Font.BOLD, 13));
        btnExportarResultados.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        btnExportarResultados.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnExportarResultados.setBackground(new Color(0, 90, 160));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnExportarResultados.setBackground(new Color(0, 120, 200));
            }
        });
    }

    /**
     * Agrega los listeners y acciones a los botones principales.
     * Cada acción delega la lógica al controlador y actualiza la UI según el resultado.
     */
    private void agregarEventos() {
        btnRegistrarCandidato.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int numero = Integer.parseInt(txtNumero.getText().trim());
                    String nombre = txtNombre.getText().trim();
                    String partido = txtPartido.getText().trim();
                    ControladorPanelAdmin.ResultadoOperacion resultado = controlador.registrarCandidato(numero, nombre, partido);
                    if (resultado.exito) {
                        JOptionPane.showMessageDialog(PanelAdmin.this, resultado.mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        txtNumero.setText("");
                        txtNombre.setText("");
                        txtPartido.setText("");
                        mostrarResultados();
                    } else {
                        JOptionPane.showMessageDialog(PanelAdmin.this, resultado.mensaje, "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(PanelAdmin.this, "Número inválido.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnIniciarVotacion.addActionListener(e -> {
            ControladorPanelAdmin.ResultadoOperacion resultado = controlador.iniciarVotacion();
            if (resultado.exito) {
                JOptionPane.showMessageDialog(PanelAdmin.this, resultado.mensaje, "Votación", JOptionPane.INFORMATION_MESSAGE);
                mostrarResultados();
            } else {
                JOptionPane.showMessageDialog(PanelAdmin.this, resultado.mensaje, "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnFinalizarVotacion.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(PanelAdmin.this, "¿Está seguro que desea finalizar la votación?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                ControladorPanelAdmin.ResultadoOperacion resultado = controlador.finalizarVotacion();
                btnFinalizarVotacion.setEnabled(false);
                mostrarResultados();
                JOptionPane.showMessageDialog(PanelAdmin.this, resultado.mensaje, "Votación", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        btnReiniciarVotacion.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(PanelAdmin.this, "¿Está seguro que desea reiniciar la votación? Esto borrará todos los votos actuales.", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                ControladorPanelAdmin.ResultadoOperacion resultado = controlador.reiniciarVotacion();
                txtResultados.setText("RESULTADOS DE LA VOTACIÓN\n=========================\n\nVotación reiniciada.\n\nTotal de votos emitidos: 0\n");
                JOptionPane.showMessageDialog(PanelAdmin.this, resultado.mensaje, "Votación", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        btnVerUsuarios.addActionListener(e -> {
            mostrarUsuarios();
        });

        btnVerCandidatos.addActionListener(e -> {
            mostrarCandidatos();
        });

        btnExportarResultados.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int seleccion = fileChooser.showSaveDialog(PanelAdmin.this);
            if (seleccion == JFileChooser.APPROVE_OPTION) {
                java.io.File archivo = fileChooser.getSelectedFile();
                String contenido = txtResultados.getText();
                ControladorPanelAdmin.ResultadoOperacion resultado = controlador.exportarResultados(contenido, archivo);
                if (resultado.exito) {
                    JOptionPane.showMessageDialog(PanelAdmin.this, resultado.mensaje, "Exportación", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(PanelAdmin.this, resultado.mensaje, "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnCerrarSesion.addActionListener(e -> {
            dispose();
            new VentanaSeleccionRol().setVisible(true);
        });
    }

    /**
     * Muestra los resultados de la votación en la tabla y área de texto.
     */
    private void mostrarResultados() {
        List<Candidato> candidatos = ConexionDB.obtenerCandidatos();
        StringBuilder sb = new StringBuilder();
        sb.append("========================================\n");
        sb.append("         RESULTADOS DE LA VOTACIÓN\n");
        sb.append("========================================\n\n");
        int totalVotos = 0;
        for (Candidato c : candidatos) {
            totalVotos += c.getVotos();
        }
        // Actualizar tabla visual
        modeloTablaResultados.setRowCount(0);
        int maxVotos = -1;
        for (Candidato c : candidatos) {
            if (c.getVotos() > maxVotos) maxVotos = c.getVotos();
        }
        int i = 1;
        for (Candidato c : candidatos) {
            double porcentaje = totalVotos > 0 ? (c.getVotos() * 100.0 / totalVotos) : 0.0;
            modeloTablaResultados.addRow(new Object[]{c.getNumero(), c.getNombre(), c.getPartido(), c.getVotos(), porcentaje});
        }
        // Construir string para exportar
        sb.append(String.format("Total de votos emitidos: %d\n\n", totalVotos));
        sb.append("----------------------------------------\n");
        sb.append(String.format("| %-3s | %-16s | %-8s | %-5s | %-6s |\n", "N°", "Candidato", "Partido", "Votos", "%"));
        sb.append("----------------------------------------\n");
        i = 1;
        for (Candidato c : candidatos) {
            double porcentaje = totalVotos > 0 ? (c.getVotos() * 100.0 / totalVotos) : 0.0;
            sb.append(String.format("| %-3d | %-16s | %-8s | %-5d | %5.2f |\n", i++, c.getNombre(), c.getPartido(), c.getVotos(), porcentaje));
        }
        sb.append("----------------------------------------\n\n");
        // Mostrar ganador o empate solo si la votación está finalizada (botón Finalizar Votación)
        if (!btnFinalizarVotacion.isEnabled()) {
            List<Candidato> ganadores = new java.util.ArrayList<>();
            for (Candidato c : candidatos) {
                if (c.getVotos() == maxVotos && maxVotos > 0) {
                    ganadores.add(c);
                }
            }
            if (ganadores.size() == 1) {
                Candidato ganador = ganadores.get(0);
                double porcentajeGanador = totalVotos > 0 ? (ganador.getVotos() * 100.0 / totalVotos) : 0.0;
                sb.append(String.format("GANADOR: %s (%s) con %d votos (%.2f%%)\n", ganador.getNombre(), ganador.getPartido(), ganador.getVotos(), porcentajeGanador));
            } else if (ganadores.size() > 1) {
                sb.append("EMPATE entre: ");
                for (int j = 0; j < ganadores.size(); j++) {
                    Candidato emp = ganadores.get(j);
                    double porcentajeEmp = totalVotos > 0 ? (emp.getVotos() * 100.0 / totalVotos) : 0.0;
                    sb.append(String.format("%s (%d votos, %.2f%%)", emp.getNombre(), emp.getVotos(), porcentajeEmp));
                    if (j < ganadores.size() - 1) sb.append(", ");
                }
                sb.append("\n");
            } else {
                sb.append("No hay votos registrados.\n");
            }
        }
        txtResultados.setText(sb.toString());
    }

    private void mostrarUsuarios() {
        java.util.List<votacion_modelo.Usuario> usuarios = controlador.obtenerUsuarios();
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-4s | %-12s | %-22s | %-10s\n", "N°", "Nombre", "Email", "DNI"));
        sb.append("---------------------------------------------------------------\n");
        int i = 1;
        for (votacion_modelo.Usuario u : usuarios) {
            sb.append(String.format("%-4d | %-12s | %-22s | %-10s\n", i++, u.getNombre(), u.getEmail(), u.getDni()));
        }
        if (usuarios.isEmpty()) {
            sb.append("No hay usuarios registrados.\n");
        }
        txtUsuarios.setText(sb.toString());
    }

    private void mostrarCandidatos() {
        java.util.List<votacion_modelo.Candidato> candidatos = controlador.obtenerCandidatos();
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-4s | %-15s | %-15s\n", "N°", "Nombre", "Partido"));
        sb.append("-----------------------------------------------\n");
        for (votacion_modelo.Candidato c : candidatos) {
            sb.append(String.format("%-4d | %-15s | %-15s\n", c.getNumero(), c.getNombre(), c.getPartido()));
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

// ProgressBarRenderer para la columna de porcentaje
class ProgressBarRenderer extends JProgressBar implements TableCellRenderer {
    public ProgressBarRenderer() {
        super(0, 100);
        setStringPainted(true);
    }
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        double porcentaje = (double) value;
        setValue((int) porcentaje);
        setString(String.format("%.2f%%", porcentaje));
        return this;
    }
}