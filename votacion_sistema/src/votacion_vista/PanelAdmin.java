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
    private JButton btnRegistrarCandidato, btnIniciarVotacion, btnFinalizarVotacion, btnVerUsuarios;
    private JTextArea txtResultados, txtUsuarios;

    public PanelAdmin(Usuario admin) {
        this.admin = admin;
        setTitle("Panel de Administrador");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        inicializarComponentes();
        configurarLayout();
        agregarEventos();
    }

    private void inicializarComponentes() {
        txtNumero = new JTextField(5);
        txtNombre = new JTextField(15);
        txtPartido = new JTextField(15);
        btnRegistrarCandidato = new JButton("Registrar Candidato");
        btnIniciarVotacion = new JButton("Iniciar Votación");
        btnFinalizarVotacion = new JButton("Finalizar Votación");
        btnVerUsuarios = new JButton("Ver Usuarios Registrados");
        txtResultados = new JTextArea(12, 40);
        txtResultados.setEditable(false);
        txtUsuarios = new JTextArea(12, 25);
        txtUsuarios.setEditable(false);
    }

    private void configurarLayout() {
        JPanel panelCandidato = new JPanel();
        panelCandidato.setBorder(BorderFactory.createTitledBorder("Registrar Candidato"));
        panelCandidato.add(new JLabel("Número:"));
        panelCandidato.add(txtNumero);
        panelCandidato.add(new JLabel("Nombre:"));
        panelCandidato.add(txtNombre);
        panelCandidato.add(new JLabel("Partido:"));
        panelCandidato.add(txtPartido);
        panelCandidato.add(btnRegistrarCandidato);

        JPanel panelVotacion = new JPanel();
        panelVotacion.setBorder(BorderFactory.createTitledBorder("Control de Votación"));
        panelVotacion.add(btnIniciarVotacion);
        panelVotacion.add(btnFinalizarVotacion);

        JPanel panelUsuarios = new JPanel(new BorderLayout());
        panelUsuarios.setBorder(BorderFactory.createTitledBorder("Usuarios Registrados"));
        panelUsuarios.add(btnVerUsuarios, BorderLayout.NORTH);
        panelUsuarios.add(new JScrollPane(txtUsuarios), BorderLayout.CENTER);

        JPanel panelResultados = new JPanel(new BorderLayout());
        panelResultados.setBorder(BorderFactory.createTitledBorder("Resultados"));
        panelResultados.add(new JScrollPane(txtResultados), BorderLayout.CENTER);

        JPanel panelTop = new JPanel(new GridLayout(2, 1));
        panelTop.add(panelCandidato);
        panelTop.add(panelVotacion);

        setLayout(new BorderLayout());
        add(panelTop, BorderLayout.NORTH);
        add(panelUsuarios, BorderLayout.WEST);
        add(panelResultados, BorderLayout.CENTER);
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
        // Aquí puedes implementar la consulta de usuarios registrados si lo deseas
        txtUsuarios.setText("Funcionalidad de mostrar usuarios registrada (puedes implementarla con un método en ConexionDB).");
    }
}