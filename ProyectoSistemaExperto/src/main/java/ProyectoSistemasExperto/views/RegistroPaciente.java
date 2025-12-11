
package ProyectoSistemasExperto.views;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author EVER URIBE
 */
public class RegistroPaciente extends JFrame {
    private JTextField txtNombre;
    private JSpinner spEdad;
    private List<JCheckBox> checkboxes;

    public RegistroPaciente() {

        setTitle("Registro de Paciente");
        setSize(600, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLayout(new BorderLayout(15, 15));

        // =======================================================
        // TÍTULO
        // =======================================================
        JLabel titulo = new JLabel("Registrar Paciente", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        titulo.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        add(titulo, BorderLayout.NORTH);

        // =======================================================
        // PANEL CENTRAL
        // =======================================================
        JPanel panelCentral = new JPanel(new BorderLayout(10, 10));
        panelCentral.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        add(panelCentral, BorderLayout.CENTER);

        // ---------------------- DATOS DEL PACIENTE ---------------------
        JPanel panelDatos = new JPanel(new GridLayout(2, 2, 10, 10));

        panelDatos.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        panelDatos.add(txtNombre);

        panelDatos.add(new JLabel("Edad:"));
        spEdad = new JSpinner(new SpinnerNumberModel(18, 0, 120, 1));
        panelDatos.add(spEdad);

        panelCentral.add(panelDatos, BorderLayout.NORTH);

        // ---------------------- SÍNTOMAS ---------------------
        JPanel panelSintomas = new JPanel();
        panelSintomas.setLayout(new GridLayout(0, 2, 5, 5));
        panelSintomas.setBorder(BorderFactory.createTitledBorder("Seleccione los síntomas del paciente"));

        String[] sintomas = {
                "fiebre", "tos", "dolor_cabeza", "dolor_muscular",
                "estornudos", "dolor_garganta", "sed", "cansancio",
                "perdida_peso", "perdida_gusto_olfato", "erupcion", "picazon",
                "nausea", "sensibilidad_luz", "ojos_lagrimosos", "aumento_peso",
                "piel_seca", "vomito", "diarrea", "dolor_abdominal"
        };

        checkboxes = new ArrayList<>();

        for (String s : sintomas) {
            JCheckBox cb = new JCheckBox(s);
            checkboxes.add(cb);
            panelSintomas.add(cb);
        }

        JScrollPane scroll = new JScrollPane(panelSintomas);
        scroll.setPreferredSize(new Dimension(400, 350));
        panelCentral.add(scroll, BorderLayout.CENTER);

        // =======================================================
        // BOTONES
        // =======================================================
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton btnRegistrar = new JButton("Registrar");
        JButton btnVolver = new JButton("Volver");

        panelBotones.add(btnRegistrar);
        panelBotones.add(btnVolver);

        add(panelBotones, BorderLayout.SOUTH);

        // =======================================================
        // EVENTOS
        // =======================================================

        btnRegistrar.addActionListener(e -> registrarPaciente());
        btnVolver.addActionListener(e -> dispose());
    }


    private void registrarPaciente() {
        String nombre = txtNombre.getText().trim();
        int edad = (int) spEdad.getValue();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre no puede estar vacío.");
            return;
        }

        List<String> sintomasSeleccionados = new ArrayList<>();
        for (JCheckBox cb : checkboxes) {
            if (cb.isSelected()) {
                sintomasSeleccionados.add(cb.getText());
            }
        }

        System.out.println("Paciente registrado:");
        System.out.println("Nombre: " + nombre);
        System.out.println("Edad: " + edad);
        System.out.println("Síntomas: " + sintomasSeleccionados);

        JOptionPane.showMessageDialog(this,
                "Paciente registrado con éxito (prueba).\nSe muestran datos en consola.");
    }
}
