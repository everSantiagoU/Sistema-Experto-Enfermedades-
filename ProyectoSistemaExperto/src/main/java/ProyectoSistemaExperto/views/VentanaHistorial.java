package ProyectoSistemaExperto.views;

import ProyectoSistemaExperto.DAO.DiagnosticoDAO;
import ProyectoSistemaExperto.DAO.PacienteDAO;
import ProyectoSistemaExperto.models.Diagnostico;
import ProyectoSistemaExperto.models.Paciente;
import ProyectoSistemaExperto.reports.Reporte;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class VentanaHistorial extends JFrame {

    private JTextField txtIdPaciente;
    private JLabel lblInfoPaciente;
    private JTable tablaHistorial;
    private JButton btnExportar; 
    

    private List<Diagnostico> diagnosticosActuales = new ArrayList<>();

    public VentanaHistorial() {
        setTitle("Historial de Diagnósticos y Estadísticas");
        setSize(900, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(15, 15));

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

        String[] columnas = {"Fecha", "Enfermedad", "Categoría", "Tratamiento / Recomendación"};
        tablaHistorial = new JTable(new DefaultTableModel(columnas, 0));
        tablaHistorial.setRowHeight(25);
        JScrollPane scroll = new JScrollPane(tablaHistorial);
        scroll.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
        
        add(scroll, BorderLayout.CENTER);

        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        
        JButton btnEstadisticas = new JButton("VER ESTADÍSTICAS GLOBALES");
        btnEstadisticas.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnEstadisticas.setBackground(new Color(102, 0, 153)); // Morado
        btnEstadisticas.setForeground(Color.WHITE);
        btnEstadisticas.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEstadisticas.setPreferredSize(new Dimension(280, 45));
        
        btnExportar = new JButton("EXPORTAR REPORTE CSV");
        btnExportar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnExportar.setBackground(new Color(0, 102, 204)); // Azul
        btnExportar.setForeground(Color.WHITE);
        btnExportar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnExportar.setPreferredSize(new Dimension(250, 45));
        btnExportar.setEnabled(false); // Desactivado hasta que haya datos

        panelInferior.add(btnEstadisticas);
        panelInferior.add(btnExportar);
        
        add(panelInferior, BorderLayout.SOUTH);

        btnBuscar.addActionListener(e -> buscarHistorial());
        btnEstadisticas.addActionListener(e -> mostrarEstadisticas());
        btnExportar.addActionListener(e -> exportarReporte());
    }

    private void buscarHistorial() {
        try {
            String textoId = txtIdPaciente.getText().trim();
            if (textoId.isEmpty()) return;

            int id = Integer.parseInt(textoId);
            
            PacienteDAO pDao = new PacienteDAO();
            Paciente p = pDao.obtenerPorId(id);

            if (p == null) {
                lblInfoPaciente.setText("❌ Paciente no encontrado.");
                lblInfoPaciente.setForeground(Color.RED);
                limpiarTabla();
                btnExportar.setEnabled(false);
                return;
            }

            lblInfoPaciente.setText("✅ Historial de: " + p.getNombre() + " (" + p.getEdad() + " años)");
            lblInfoPaciente.setForeground(new Color(0, 102, 0));

            DiagnosticoDAO dDao = new DiagnosticoDAO();
            diagnosticosActuales = dDao.obtenerPorIdPaciente(id); // Guardamos en la variable de clase
            
            llenarTabla(diagnosticosActuales);

            if (diagnosticosActuales.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El paciente existe pero no tiene diagnósticos registrados.");
                btnExportar.setEnabled(false);
            } else {
                btnExportar.setEnabled(true); // Habilitamos el botón exportar
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El ID debe ser un número.");
        }
    }

    private void llenarTabla(List<Diagnostico> lista) {
        DefaultTableModel model = (DefaultTableModel) tablaHistorial.getModel();
        model.setRowCount(0);

        for (Diagnostico d : lista) {
            String fecha = (d.getFecha() != null) ? d.getFecha().toLocalDate().toString() : "N/A";
            
            model.addRow(new Object[]{
                fecha,
                d.getNombreEnfermedad().getNombre(),
                d.getCategoriaEnfermedad(),
                String.join(", ", d.getNombreEnfermedad().getRecomendaciones())
            });
        }
    }

    private void limpiarTabla() {
        ((DefaultTableModel) tablaHistorial.getModel()).setRowCount(0);
        diagnosticosActuales.clear();
    }

    private void exportarReporte() {
        if (diagnosticosActuales.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay datos para exportar.");
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar Historial Clínico");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos CSV (*.csv)", "csv"));
        
        String nombreSugerido = "Historial_Paciente_" + txtIdPaciente.getText() + ".csv";
        fileChooser.setSelectedFile(new File(nombreSugerido));

        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String ruta = fileToSave.getAbsolutePath();
            
            // .csv
            if (!ruta.toLowerCase().endsWith(".csv")) {
                ruta += ".csv";
            }

            try {
                Reporte reporte = new Reporte();
                boolean exito = reporte.generarCSV(diagnosticosActuales, ruta);
                
                if (exito) {
                    JOptionPane.showMessageDialog(this, 
                        "Reporte exportado exitosamente en:\n" + ruta, 
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Error al generar el reporte.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error de escritura: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    private void mostrarEstadisticas() {
        DiagnosticoDAO dao = new DiagnosticoDAO();
        List<Diagnostico> todos = dao.obtenerTodos();

        if (todos.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay suficientes datos para generar estadísticas.");
            return;
        }

        Map<String, Long> conteoEnfermedades = todos.stream()
            .collect(Collectors.groupingBy(d -> d.getNombreEnfermedad().getNombre(), Collectors.counting()));

        Map<String, Long> conteoCategorias = todos.stream()
            .collect(Collectors.groupingBy(d -> d.getCategoriaEnfermedad(), Collectors.counting()));
            
        StringBuilder sb = new StringBuilder();
        sb.append("=== ESTADÍSTICAS GLOBALES DEL SISTEMA ===\n\n");
        sb.append("Total de Diagnósticos: ").append(todos.size()).append("\n\n");
        
        sb.append("--- Top Enfermedades ---\n");
        conteoEnfermedades.entrySet().stream()
            .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
            .limit(5)
            .forEach(e -> sb.append("• ").append(e.getKey()).append(": ").append(e.getValue()).append("\n"));

        sb.append("\n--- Categorías Frecuentes ---\n");
        conteoCategorias.entrySet().stream()
            .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
            .forEach(e -> sb.append("• ").append(e.getKey()).append(": ").append(e.getValue()).append("\n"));

        JOptionPane.showMessageDialog(this, sb.toString(), "Reporte Estadístico", JOptionPane.INFORMATION_MESSAGE);
    }
}
