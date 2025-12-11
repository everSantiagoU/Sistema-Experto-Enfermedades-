
package ProyectoSistemasExperto.views;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.Normalizer;
import java.util.*;
import java.util.List;

public class VentanaDiagnostico extends JFrame {

    private JPanel panelSintomas;
    private JTable tablaResultados;
    private JComboBox<String> comboCategoria;

    // Datos demo de enfermedades (nombre -> categoria y sintomas demo y recomendacion)
    private static final List<DemoEnfermedad> DEMO_ENFERMEDADES = List.of(
            new DemoEnfermedad("Gripe", "viral",
                    List.of("fiebre","tos","dolor_cabeza","dolor_muscular"),
                    "Descansar, hidratar, consultar médico"),
            new DemoEnfermedad("COVID-19", "viral",
                    List.of("fiebre","tos","cansancio","perdida_gusto_olfato"),
                    "Aislamiento, consultar médico"),
            new DemoEnfermedad("Migraña", "crónica",
                    List.of("dolor_cabeza","nausea","sensibilidad_luz"),
                    "Descansar, evitar luz intensa"),
            new DemoEnfermedad("Diabetes", "crónica",
                    List.of("sed","cansancio","perdida_peso"),
                    "Controlar dieta, consultar especialista"),
            new DemoEnfermedad("Alergia", "alergia",
                    List.of("estornudos","picazon","ojos_lagrimosos"),
                    "Evitar alérgenos, tomar antihistamínicos"),
            new DemoEnfermedad("Faringitis", "bacteriana",
                    List.of("dolor_garganta","fiebre","tos"),
                    "Consultar médico, posible antibiótico")
    );

    public VentanaDiagnostico() {

        setTitle("Generación de Diagnóstico Médico");
        setSize(950, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // PANEL SUPERIOR – FILTRO POR CATEGORÍA Y BOTÓN DIAGNOSTICAR
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));

        JLabel lblCat = new JLabel("Filtrar por categoría:");
        lblCat.setFont(new Font("SansSerif", Font.PLAIN, 16));

        comboCategoria = new JComboBox<>();
        comboCategoria.addItem("Todas");
        comboCategoria.addItem("viral");
        comboCategoria.addItem("crónica");
        comboCategoria.addItem("alergia");
        comboCategoria.addItem("bacteriana");

        JButton btnDiagnosticar = new JButton("Generar Diagnóstico");

        panelSuperior.add(lblCat);
        panelSuperior.add(comboCategoria);
        panelSuperior.add(btnDiagnosticar);

        add(panelSuperior, BorderLayout.NORTH);

        // PANEL IZQUIERDO – LISTA DE SÍNTOMAS (CHECKBOXES)
        panelSintomas = new JPanel();
        panelSintomas.setLayout(new BoxLayout(panelSintomas, BoxLayout.Y_AXIS));
        panelSintomas.setBorder(BorderFactory.createTitledBorder("Seleccione los síntomas"));

        // TODOS los síntomas de tu inserts.sql
        String[] sintomasDemo = {
                "fiebre", "tos", "dolor_cabeza", "dolor_muscular",
                "estornudos", "dolor_garganta", "sed", "cansancio",
                "perdida_peso", "perdida_gusto_olfato", "erupcion", "picazon",
                "nausea", "sensibilidad_luz", "ojos_lagrimosos", "aumento_peso",
                "piel_seca", "vomito", "diarrea", "dolor_abdominal"
        };

        for (String s : sintomasDemo) {
            JCheckBox chk = new JCheckBox(s);
            chk.setFont(new Font("SansSerif", Font.PLAIN, 15));
            panelSintomas.add(chk);
        }

        JScrollPane scrollSintomas = new JScrollPane(panelSintomas);
        scrollSintomas.setPreferredSize(new Dimension(300, 0));

        add(scrollSintomas, BorderLayout.WEST);

        // TABLA DE RESULTADOS
        tablaResultados = new JTable(new DefaultTableModel(
                new Object[]{"Enfermedad", "Categoría", "Coincidencias", "Recomendación"},
                0
        ));
        JScrollPane scrollTabla = new JScrollPane(tablaResultados);

        add(scrollTabla, BorderLayout.CENTER);

        // EVENTO DEL BOTÓN
        btnDiagnosticar.addActionListener(e -> generarDiagnostico());
    }

    private void generarDiagnostico() {
        // Obtener síntomas seleccionados
        List<String> seleccionados = new ArrayList<>();
        for (Component c : panelSintomas.getComponents()) {
            if (c instanceof JCheckBox) {
                JCheckBox chk = (JCheckBox) c;
                if (chk.isSelected()) seleccionados.add(chk.getText());
            }
        }

        if (seleccionados.isEmpty()) {
            int r = JOptionPane.showConfirmDialog(this,
                    "No seleccionó síntomas. ¿Desea continuar mostrando resultados demo igualmente?",
                    "Confirmar", JOptionPane.YES_NO_OPTION);
            if (r != JOptionPane.YES_OPTION) return;
        }

        String categoriaFiltro = (String) comboCategoria.getSelectedItem();
        if ("Todas".equalsIgnoreCase(categoriaFiltro)) categoriaFiltro = null;

        // Limpiar tabla
        DefaultTableModel model = (DefaultTableModel) tablaResultados.getModel();
        model.setRowCount(0);

        // Generar resultados demo aplicando filtro por categoría y calculando coincidencias
        for (DemoEnfermedad de : DEMO_ENFERMEDADES) {

            // filtrar por categoria si aplica
            if (categoriaFiltro != null) {
                if (!normalize(de.getCategoria()).equals(normalize(categoriaFiltro))) continue;
            }

            // calcular coincidencias reales entre sintomas seleccionados y sintomas de la enfermedad
            long coincidencias = seleccionados.stream()
                    .filter(s -> de.getSintomas().contains(s))
                    .count();

            // Solo mostrar si hay al menos 1 coincidencia — esto simula tu regla "diagnostico"
            if (coincidencias > 0 || seleccionados.isEmpty()) {
                model.addRow(new Object[]{
                        de.getNombre(),
                        de.getCategoria(),
                        coincidencias,
                        de.getRecomendacion()
                });
            }
        }

        // Si no hay filas (p. ej. filtro restringe todo) mostrar mensaje
        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No se encontraron diagnósticos con los filtros/síntomas seleccionados.");
        }
    }

    // normalizar strings para comparar ignoring case + tildes
    private static String normalize(String s) {
        if (s == null) return "";
        String n = Normalizer.normalize(s, Normalizer.Form.NFD);
        return n.replaceAll("\\p{M}", "").toLowerCase().trim();
    }

    // Clase auxiliar demo
    private static class DemoEnfermedad {
        private final String nombre;
        private final String categoria;
        private final List<String> sintomas;
        private final String recomendacion;

        public DemoEnfermedad(String nombre, String categoria, List<String> sintomas, String recomendacion) {
            this.nombre = nombre;
            this.categoria = categoria;
            this.sintomas = sintomas;
            this.recomendacion = recomendacion;
        }

        public String getNombre() { return nombre; }
        public String getCategoria() { return categoria; }
        public List<String> getSintomas() { return sintomas; }
        public String getRecomendacion() { return recomendacion; }
    }
}
