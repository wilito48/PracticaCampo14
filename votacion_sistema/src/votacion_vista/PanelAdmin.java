package votacion_vista;

import votacion_modelo.Candidato;
import votacion_modelo.Usuario;
import votacion_util.ConexionDB;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.Timer;
import javax.swing.JFileChooser;
import javax.swing.table.*;

public class PanelAdmin extends JFrame {
    private Usuario admin;
    private JTextField txtNumero, txtNombre, txtPartido;
    private JButton btnRegistrarCandidato, btnIniciarVotacion, btnFinalizarVotacion, btnVerUsuarios, btnVerCandidatos, btnReiniciarVotacion, btnExportarResultados, btnCerrarSesion;
    private JTextArea txtResultados, txtUsuarios, txtCandidatos;
    private JTable tablaResultados;
    private DefaultTableModel modeloTablaResultados;

    public PanelAdmin(Usuario admin) {
        this.admin = admin;
        setTitle("Panel de Administrador");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        inicializarComponentes();
        configurarLayout();
        agregarEventos();

        // Timer para refrescar resultados cada 2 segundos
        Timer timerResultados = new Timer(2000, e -> mostrarResultados());
        timerResultados.start();
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
                    // Validación: no permitir candidatos con el mismo número o nombre
                    List<Candidato> candidatosExistentes = ConexionDB.obtenerCandidatos();
                    for (Candidato c : candidatosExistentes) {
                        if (c.getNumero() == numero) {
                            JOptionPane.showMessageDialog(PanelAdmin.this, "Ya existe un candidato con ese número.", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        if (c.getNombre().equalsIgnoreCase(nombre)) {
                            JOptionPane.showMessageDialog(PanelAdmin.this, "Ya existe un candidato con ese nombre.", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                    Candidato candidato = new Candidato(numero, nombre, partido);
                    if (ConexionDB.registrarCandidato(candidato)) {
                        JOptionPane.showMessageDialog(PanelAdmin.this, "Candidato registrado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        txtNumero.setText("");
                        txtNombre.setText("");
                        txtPartido.setText("");
                        mostrarResultados();
                    } else {
                        JOptionPane.showMessageDialog(PanelAdmin.this, "Error al registrar candidato.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(PanelAdmin.this, "Número inválido.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnIniciarVotacion.addActionListener(e -> {
            List<Candidato> candidatosExistentes = ConexionDB.obtenerCandidatos();
            if (candidatosExistentes.isEmpty()) {
                JOptionPane.showMessageDialog(PanelAdmin.this, "Debe registrar al menos un candidato antes de iniciar la votación.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
            JOptionPane.showMessageDialog(PanelAdmin.this, "¡Votación iniciada! Los usuarios ya pueden votar.", "Votación", JOptionPane.INFORMATION_MESSAGE);
            mostrarResultados();
        });

        btnFinalizarVotacion.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(PanelAdmin.this, "¿Está seguro que desea finalizar la votación?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                btnFinalizarVotacion.setEnabled(false);
                mostrarResultados();
                JOptionPane.showMessageDialog(PanelAdmin.this, "¡Votación finalizada!", "Votación", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        btnReiniciarVotacion.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(PanelAdmin.this, "¿Está seguro que desea reiniciar la votación? Esto borrará todos los votos actuales.", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                ConexionDB.reiniciarVotos();
                txtResultados.setText("RESULTADOS DE LA VOTACIÓN\n=========================\n\nVotación reiniciada.\n\nTotal de votos emitidos: 0\n");
                JOptionPane.showMessageDialog(PanelAdmin.this, "¡Nueva votación iniciada! Todos los usuarios pueden votar de nuevo.", "Votación", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        btnVerUsuarios.addActionListener(e -> {
            mostrarUsuarios();
        });

        btnVerCandidatos.addActionListener(e -> {
            mostrarCandidatos();
        });

        btnExportarResultados.addActionListener(e -> {
            String[] opciones = {".txt (Visual)", ".csv (Excel)", "Cancelar"};
            int seleccion = JOptionPane.showOptionDialog(PanelAdmin.this, "¿En qué formato desea exportar los resultados?", "Exportar Resultados",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);
            if (seleccion == 2 || seleccion == JOptionPane.CLOSED_OPTION) return;
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Guardar Resultados");
            if (seleccion == 0) fileChooser.setSelectedFile(new java.io.File("resultados.txt"));
            else if (seleccion == 1) fileChooser.setSelectedFile(new java.io.File("resultados.csv"));
            int userSelection = fileChooser.showSaveDialog(PanelAdmin.this);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                java.io.File fileToSave = fileChooser.getSelectedFile();
                try (java.io.FileWriter fw = new java.io.FileWriter(fileToSave)) {
                    if (seleccion == 0) {
                        // Exportar como .txt visual
                        StringBuilder txt = new StringBuilder();
                        txt.append("RESULTADOS DE LA VOTACIÓN\n=========================\n\n");
                        // Encabezado tabla
                        txt.append(String.format("%-4s %-15s %-15s %-7s %-10s\n", "N°", "Nombre", "Partido", "Votos", "Porcentaje"));
                        for (int i = 0; i < modeloTablaResultados.getRowCount(); i++) {
                            txt.append(String.format("%-4s %-15s %-15s %-7s %-9.2f%%\n",
                                modeloTablaResultados.getValueAt(i, 0),
                                modeloTablaResultados.getValueAt(i, 1),
                                modeloTablaResultados.getValueAt(i, 2),
                                modeloTablaResultados.getValueAt(i, 3),
                                (double)modeloTablaResultados.getValueAt(i, 4)));
                        }
                        txt.append("\n").append(txtResultados.getText());
                        fw.write(txt.toString());
                    } else if (seleccion == 1) {
                        // Exportar como .csv
                        StringBuilder csv = new StringBuilder();
                        csv.append("Numero,Nombre,Partido,Votos,Porcentaje\n");
                        for (int i = 0; i < modeloTablaResultados.getRowCount(); i++) {
                            csv.append(String.format("%s,%s,%s,%s,%.2f%%\n",
                                modeloTablaResultados.getValueAt(i, 0),
                                modeloTablaResultados.getValueAt(i, 1),
                                modeloTablaResultados.getValueAt(i, 2),
                                modeloTablaResultados.getValueAt(i, 3),
                                (double)modeloTablaResultados.getValueAt(i, 4)));
                        }
                        fw.write(csv.toString());
                    }
                    JOptionPane.showMessageDialog(PanelAdmin.this, "Resultados exportados correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(PanelAdmin.this, "Error al exportar resultados: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnCerrarSesion.addActionListener(e -> {
            dispose();
            new VentanaSeleccionRol().setVisible(true);
        });
    }

    private void mostrarResultados() {
        List<Candidato> candidatos = ConexionDB.obtenerCandidatos();
        StringBuilder sb = new StringBuilder();
        sb.append("RESULTADOS DE LA VOTACIÓN\n");
        sb.append("=========================\n\n");
        int totalVotos = 0;
        for (Candidato c : candidatos) {
            totalVotos += c.getVotos();
        }
        // Actualizar tabla
        modeloTablaResultados.setRowCount(0);
        int maxVotos = -1;
        for (Candidato c : candidatos) {
            if (c.getVotos() > maxVotos) maxVotos = c.getVotos();
        }
        for (Candidato c : candidatos) {
            double porcentaje = totalVotos > 0 ? (c.getVotos() * 100.0 / totalVotos) : 0.0;
            modeloTablaResultados.addRow(new Object[]{c.getNumero(), c.getNombre(), c.getPartido(), c.getVotos(), porcentaje});
        }
        sb.append("\nTotal de votos emitidos: ").append(totalVotos).append("\n\n");

        // Mostrar ganador o empate solo si la votación está finalizada (botón Finalizar Votación)
        if (!btnFinalizarVotacion.isEnabled()) {
            // Buscar candidatos con máximo de votos
            List<Candidato> ganadores = new java.util.ArrayList<>();
            for (Candidato c : candidatos) {
                if (c.getVotos() == maxVotos && maxVotos > 0) {
                    ganadores.add(c);
                }
            }
            if (ganadores.size() == 1) {
                Candidato ganador = ganadores.get(0);
                double porcentajeGanador = totalVotos > 0 ? (ganador.getVotos() * 100.0 / totalVotos) : 0.0;
                sb.append("\nGANADOR: ").append(ganador.getNombre()).append(" (").append(ganador.getPartido()).append(") con ")
                  .append(ganador.getVotos()).append(" votos (" + String.format("%.2f", porcentajeGanador) + "%)\n");
            } else if (ganadores.size() > 1) {
                sb.append("\nEMPATE entre: ");
                for (int i = 0; i < ganadores.size(); i++) {
                    Candidato emp = ganadores.get(i);
                    double porcentajeEmp = totalVotos > 0 ? (emp.getVotos() * 100.0 / totalVotos) : 0.0;
                    sb.append(emp.getNombre()).append(" (").append(emp.getVotos()).append(" votos, ")
                      .append(String.format("%.2f", porcentajeEmp)).append("%)");
                    if (i < ganadores.size() - 1) sb.append(", ");
                }
                sb.append("\n");
            } else {
                sb.append("\nNo hay votos registrados.\n");
            }
        }
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