package ProyectoSistemaExperto.views;

import ProyectoSistemaExperto.DAO.PacienteDAO;
import ProyectoSistemaExperto.models.Paciente;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Ventana para registrar un nuevo paciente en la Base de Datos MySQL.
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
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Cierra solo esta ventana

        // Usamos BorderLayout con espaciado
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

        panelDatos.add(new JLabel("Nombre Completo:"));
        txtNombre = new JTextField();
        panelDatos.add(txtNombre);

        panelDatos.add(new JLabel("Edad:"));
        spEdad = new JSpinner(new SpinnerNumberModel(18, 0, 120, 1)); // Edad mínima 0, máxima 120
        panelDatos.add(spEdad);

        panelCentral.add(panelDatos, BorderLayout.NORTH);

        // ---------------------- SÍNTOMAS ---------------------
        JPanel panelSintomas = new JPanel();
        panelSintomas.setLayout(new GridLayout(0, 2, 5, 5));
        panelSintomas.setBorder(BorderFactory.createTitledBorder("Seleccione los síntomas que presenta"));

        // Lista de síntomas idéntica a tu base de datos (inserts.sql)
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
        scroll.getVerticalScrollBar().setUnitIncrement(16); // Scroll más rápido/suave
        panelCentral.add(scroll, BorderLayout.CENTER);

        // =======================================================
        // BOTONES
        // =======================================================
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton btnRegistrar = new JButton("Registrar Paciente");
        btnRegistrar.setFont(new Font("SansSerif", Font.BOLD, 14));
        
        JButton btnVolver = new JButton("Cancelar");

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
        // 1. Validar nombre vacío
        String nombre = txtNombre.getText().trim();
        int edad = (int) spEdad.getValue();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre es obligatorio.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 2. Recopilar síntomas seleccionados
        List<String> sintomasSeleccionados = new ArrayList<>();
        for (JCheckBox cb : checkboxes) {
            if (cb.isSelected()) {
                sintomasSeleccionados.add(cb.getText());
            }
        }

        // 3. Crear el objeto Paciente
        Paciente paciente = new Paciente(nombre, edad, sintomasSeleccionados);

        // 4. Llamar al DAO para guardar en MySQL
        PacienteDAO dao = new PacienteDAO();
        try {
            int idGenerado = dao.registrar(paciente);

            if (idGenerado != -1) {
                // ÉXITO: Mostrar ID y cerrar ventana
                JOptionPane.showMessageDialog(this,
                        "¡Paciente registrado exitosamente!\n\n" +
                        "• Nombre: " + nombre + "\n" +
                        "• ID Generado: " + idGenerado + "\n" +
                        "• Síntomas registrados: " + sintomasSeleccionados.size(),
                        "Registro Exitoso",
                        JOptionPane.INFORMATION_MESSAGE);
                
                dispose(); // Volver al menú principal
            } else {
                // ERROR controlado
                JOptionPane.showMessageDialog(this,
                        "No se pudo guardar el paciente.\nVerifique la conexión a la base de datos.",
                        "Error de Base de Datos",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            // ERROR inesperado
            JOptionPane.showMessageDialog(this,
                    "Ocurrió un error inesperado:\n" + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}