package ProyectoSistemaExperto.views;

import ProyectoSistemaExperto.DAO.DiagnosticoDAO;
import ProyectoSistemaExperto.DAO.PacienteDAO;
import ProyectoSistemaExperto.models.Diagnostico;
import ProyectoSistemaExperto.models.Paciente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class VentanaHistorial extends JFrame {

    private JTextField txtIdPaciente;
    private JLabel lblInfoPaciente;
    private JTable tablaHistorial;

    public VentanaHistorial() {
        setTitle("Historial de Diagnósticos y Estadísticas");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(15, 15));

        // --- PANEL SUPERIOR: BUSCADOR ---
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        panelSuperior.setBorder(BorderFactory.createTitledBorder("Consultar Historial de Paciente"));

        panelSuperior.add(new JLabel("ID del Paciente:"));
        txtIdPaciente = new JTextField(10);
        JButton btnBuscar = new JButton("Buscar Historial");
        lblInfoPaciente = new JLabel("Ingrese un ID para ver sus diagnósticos previos.");
        lblInfoPaciente.setForeground(Color.GRAY);

        panelSuperior.add(txtIdPaciente);
        panelSuperior.add(btnBuscar);
        panelSuperior.add(Box.createHorizontalStrut(20));
        panelSuperior.add(lblInfoPaciente);

        add(panelSuperior, BorderLayout.NORTH);

        // --- CENTRO: TABLA ---
        String[] columnas = {"Fecha (Aprox)", "Enfermedad Diagnosticada", "Categoría", "Tratamiento / Recomendación"};
        tablaHistorial = new JTable(new DefaultTableModel(columnas, 0));
        tablaHistorial.setRowHeight(25);
        JScrollPane scroll = new JScrollPane(tablaHistorial);
        scroll.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15)); // Margenes laterales
        
        add(scroll, BorderLayout.CENTER);

        // --- PANEL INFERIOR: ESTADÍSTICAS ---
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        JButton btnEstadisticas = new JButton("GENERAR ESTADÍSTICAS GLOBALES");
        btnEstadisticas.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnEstadisticas.setBackground(new Color(102, 0, 153)); // Morado
        btnEstadisticas.setForeground(Color.WHITE);
        btnEstadisticas.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEstadisticas.setPreferredSize(new Dimension(350, 45));

        panelInferior.add(btnEstadisticas);
        add(panelInferior, BorderLayout.SOUTH);

        // --- EVENTOS ---
        btnBuscar.addActionListener(e -> buscarHistorial());
        btnEstadisticas.addActionListener(e -> mostrarEstadisticas());
    }

    private void buscarHistorial() {
        try {
            String textoId = txtIdPaciente.getText().trim();
            if (textoId.isEmpty()) return;

            int id = Integer.parseInt(textoId);
            
            // 1. Buscar Paciente para mostrar nombre
            PacienteDAO pDao = new PacienteDAO();
            Paciente p = pDao.obtenerPorId(id);

            if (p == null) {
                lblInfoPaciente.setText("❌ Paciente no encontrado.");
                lblInfoPaciente.setForeground(Color.RED);
                limpiarTabla();
                return;
            }

            lblInfoPaciente.setText("✅ Historial de: " + p.getNombre() + " (" + p.getEdad() + " años)");
            lblInfoPaciente.setForeground(new Color(0, 102, 0));

            // 2. Buscar Diagnósticos
            DiagnosticoDAO dDao = new DiagnosticoDAO();
            List<Diagnostico> historial = dDao.obtenerPorIdPaciente(id);
            
            llenarTabla(historial);

            if (historial.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El paciente existe pero no tiene diagnósticos registrados.");
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El ID debe ser un número.");
        }
    }

    private void llenarTabla(List<Diagnostico> lista) {
        DefaultTableModel model = (DefaultTableModel) tablaHistorial.getModel();
        model.setRowCount(0);

        for (Diagnostico d : lista) {
            model.addRow(new Object[]{
                d.getFecha().toLocalDate().toString(), // Fecha simple
                d.getNombreEnfermedad().getNombre(),
                d.getCategoriaEnfermedad(),
                String.join(", ", d.getNombreEnfermedad().getRecomendaciones())
            });
        }
    }

    private void limpiarTabla() {
        ((DefaultTableModel) tablaHistorial.getModel()).setRowCount(0);
    }

    private void mostrarEstadisticas() {
        DiagnosticoDAO dao = new DiagnosticoDAO();
        List<Diagnostico> todos = dao.obtenerTodos();

        if (todos.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay suficientes datos para generar estadísticas.");
            return;
        }

        // --- CÁLCULO DE ESTADÍSTICAS (Java Streams) ---
        
        // 1. Enfermedades más frecuentes
        Map<String, Long> conteoEnfermedades = todos.stream()
            .collect(Collectors.groupingBy(d -> d.getNombreEnfermedad().getNombre(), Collectors.counting()));

        // 2. Categorías más comunes
        Map<String, Long> conteoCategorias = todos.stream()
            .collect(Collectors.groupingBy(d -> d.getCategoriaEnfermedad(), Collectors.counting()));
            
        // Construir Mensaje
        StringBuilder sb = new StringBuilder();
        sb.append("=== ESTADÍSTICAS DEL SISTEMA ===\n\n");
        sb.append("Total de Diagnósticos Realizados: ").append(todos.size()).append("\n\n");
        
        sb.append("--- Enfermedades Más Comunes ---\n");
        conteoEnfermedades.entrySet().stream()
            .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue())) // Ordenar descendente
            .limit(5) // Top 5
            .forEach(e -> sb.append("• ").append(e.getKey()).append(": ").append(e.getValue()).append(" casos\n"));

        sb.append("\n--- Categorías Recurrentes ---\n");
        conteoCategorias.entrySet().stream()
            .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
            .forEach(e -> sb.append("• ").append(e.getKey()).append(": ").append(e.getValue()).append(" casos\n"));

        JOptionPane.showMessageDialog(this, sb.toString(), "Reporte Estadístico", JOptionPane.INFORMATION_MESSAGE);
    }
}
