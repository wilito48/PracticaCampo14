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
    private JTextArea txtResultados;

    public PanelVotacionUsuario(Usuario usuario) {
        this.usuario = usuario;
        setTitle("Panel de Usuario - Votación");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        inicializarComponentes();
        configurarLayout();
        agregarEventos();
        cargarCandidatos();
        mostrarResultados();
    }

    private void inicializarComponentes() {
        comboCandidatos = new JComboBox<>();
        btnVotar = new JButton("Votar");
        txtResultados = new JTextArea(10, 40);
        txtResultados.setEditable(false);
    }

    private void configurarLayout() {
        JPanel panelVoto = new JPanel();
        panelVoto.setBorder(BorderFactory.createTitledBorder("Votar por un candidato"));
        panelVoto.add(new JLabel("Candidato:"));
        panelVoto.add(comboCandidatos);
        panelVoto.add(btnVotar);

        JPanel panelResultados = new JPanel();
        panelResultados.setBorder(BorderFactory.createTitledBorder("Resultados"));
        panelResultados.setLayout(new BorderLayout());
        panelResultados.add(new JScrollPane(txtResultados), BorderLayout.CENTER);

        setLayout(new BorderLayout());
        add(panelVoto, BorderLayout.NORTH);
        add(panelResultados, BorderLayout.CENTER);
    }

    private void agregarEventos() {
        btnVotar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (usuario.isYaVoto()) {
                    JOptionPane.showMessageDialog(PanelVotacionUsuario.this, "Ya has votado.", "Voto Duplicado", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                Candidato candidato = (Candidato) comboCandidatos.getSelectedItem();
                if (candidato == null) {
                    JOptionPane.showMessageDialog(PanelVotacionUsuario.this, "Seleccione un candidato.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int candidatoId = ConexionDB.obtenerIdCandidato(candidato.getNumero());
                if (ConexionDB.registrarVoto(candidatoId, usuario.getId())) {
                    usuario.setYaVoto(true);
                    JOptionPane.showMessageDialog(PanelVotacionUsuario.this, "¡Voto registrado exitosamente!", "Voto", JOptionPane.INFORMATION_MESSAGE);
                    mostrarResultados();
                } else {
                    JOptionPane.showMessageDialog(PanelVotacionUsuario.this, "Error al registrar el voto.", "Error", JOptionPane.ERROR_MESSAGE);
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
}