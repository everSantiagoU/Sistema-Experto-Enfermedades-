package ProyectoSistemaExperto.views;

import ProyectoSistemaExperto.DAO.DiagnosticoDAO;
import ProyectoSistemaExperto.DAO.EnfermedadDAO;
import ProyectoSistemaExperto.DAO.PacienteDAO;
import ProyectoSistemaExperto.inferencia.MotorInferencia;
import ProyectoSistemaExperto.models.Diagnostico;
import ProyectoSistemaExperto.models.Enfermedad;
import ProyectoSistemaExperto.models.Paciente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VentanaDiagnostico extends JFrame {

    private JPanel panelSintomas;
    private JTable tablaResultados;
    private JComboBox<String> comboCategoria;
    private JTextField txtIdPaciente;
    private JLabel lblNombrePaciente;
    
    // Lógica
    private MotorInferencia motor;
    private List<Enfermedad> todasLasEnfermedades;
    private Paciente pacienteActual; // El paciente al que estamos diagnosticando

    // Constructor por defecto (desde Menú Principal)
    public VentanaDiagnostico() {
        this(null); // Llama al constructor principal sin paciente
    }

    // Constructor principal (permite recibir un paciente ya cargado)
    public VentanaDiagnostico(Paciente paciente) {
        this.pacienteActual = paciente;
        inicializarLogica();
        inicializarInterfaz();
        
        // Si recibimos un paciente, actualizamos la UI automáticamente
        if (pacienteActual != null) {
            txtIdPaciente.setText(String.valueOf(pacienteActual.getIdPaciente()));
            lblNombrePaciente.setText("Paciente: " + pacienteActual.getNombre());
            txtIdPaciente.setEnabled(false); // Bloqueamos para no cambiarlo accidentalmente
        }
    }

    private void inicializarLogica() {
        try {
            // 1. Cargar enfermedades desde BD
            EnfermedadDAO dao = new EnfermedadDAO();
            todasLasEnfermedades = dao.obtenerEnfermedades();
            
            // 2. Iniciar Motor y cargar conocimientos
            motor = new MotorInferencia();
            motor.limpiarBD(); // Limpiamos hechos viejos por si acaso
            for (Enfermedad e : todasLasEnfermedades) {
                motor.agregarEnfermedad(e);
            }
            System.out.println("Lógica inicializada: " + todasLasEnfermedades.size() + " enfermedades cargadas.");
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error inicializando motor: " + e.getMessage());
        }
    }

    private void inicializarInterfaz() {
        setTitle("Generación de Diagnóstico Médico - Sistema Experto");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // --- PANEL SUPERIOR: DATOS PACIENTE Y FILTROS ---
        JPanel panelSuperior = new JPanel(new GridLayout(2, 1));
        
        // Fila 1: Buscador de Paciente
        JPanel panelPaciente = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelPaciente.setBorder(BorderFactory.createTitledBorder("Datos del Paciente"));
        
        panelPaciente.add(new JLabel("ID Paciente:"));
        txtIdPaciente = new JTextField(10);
        JButton btnBuscar = new JButton("Buscar / Cargar");
        lblNombrePaciente = new JLabel("Paciente: No seleccionado");
        lblNombrePaciente.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblNombrePaciente.setForeground(new Color(0, 102, 204));
        
        panelPaciente.add(txtIdPaciente);
        panelPaciente.add(btnBuscar);
        panelPaciente.add(Box.createHorizontalStrut(20));
        panelPaciente.add(lblNombrePaciente);
        
        // Fila 2: Filtros de Diagnóstico
        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelFiltros.setBorder(BorderFactory.createTitledBorder("Filtros de Diagnóstico"));
        
        panelFiltros.add(new JLabel("Filtrar por categoría:"));
        comboCategoria = new JComboBox<>();
        comboCategoria.addItem("Todas");
        comboCategoria.addItem("viral");
        comboCategoria.addItem("crónica");
        comboCategoria.addItem("alergia");
        comboCategoria.addItem("bacteriana"); // Asegúrate que coincida con tus inserts.sql
        
        JButton btnDiagnosticar = new JButton("EJECUTAR DIAGNÓSTICO");
        btnDiagnosticar.setBackground(new Color(0, 153, 76)); // Verde
        btnDiagnosticar.setForeground(Color.WHITE);
        btnDiagnosticar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnDiagnosticar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        panelFiltros.add(comboCategoria);
        panelFiltros.add(Box.createHorizontalStrut(20));
        panelFiltros.add(btnDiagnosticar);

        panelSuperior.add(panelPaciente);
        panelSuperior.add(panelFiltros);
        add(panelSuperior, BorderLayout.NORTH);

        // --- PANEL IZQUIERDO: SÍNTOMAS ---
        panelSintomas = new JPanel();
        panelSintomas.setLayout(new GridLayout(0, 1)); // 1 columna, n filas
        panelSintomas.setBorder(BorderFactory.createTitledBorder("Seleccione Síntomas"));

        // Usamos una lista auxiliar para llenar los checkbox 
        String[] listadoSintomas = {
            "fiebre", "tos", "dolor_cabeza", "dolor_muscular", "estornudos", 
            "dolor_garganta", "sed", "cansancio", "perdida_peso", 
            "perdida_gusto_olfato", "erupcion", "picazon", "nausea", 
            "sensibilidad_luz", "ojos_lagrimosos", "aumento_peso", "piel_seca", 
            "vomito", "diarrea", "dolor_abdominal"
        };

        for (String s : listadoSintomas) {
            JCheckBox chk = new JCheckBox(s);
            panelSintomas.add(chk);
        }

        JScrollPane scrollSintomas = new JScrollPane(panelSintomas);
        scrollSintomas.setPreferredSize(new Dimension(250, 0));
        scrollSintomas.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollSintomas, BorderLayout.WEST);

        // --- CENTRO: TABLA RESULTADOS ---
        tablaResultados = new JTable(new DefaultTableModel(
                new Object[]{"Enfermedad", "Categoría", "Coincidencias", "Recomendación"}, 0
        ));
        tablaResultados.setRowHeight(25); // Filas un poco más altas para leer mejor
        JScrollPane scrollTabla = new JScrollPane(tablaResultados);
        add(scrollTabla, BorderLayout.CENTER);

        // --- PANEL INFERIOR: ACCIONES (MODIFICADO) ---
        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Centrado
        panelSur.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0)); // Margen arriba/abajo

        JButton btnGuardar = new JButton("GUARDAR DIAGNÓSTICO SELECCIONADO");
        btnGuardar.setFont(new Font("SansSerif", Font.BOLD, 16)); // Letra grande
        btnGuardar.setBackground(new Color(0, 102, 204)); // Azul intenso
        btnGuardar.setForeground(Color.WHITE); // Texto blanco
        btnGuardar.setFocusPainted(false); // Quitar borde de foco al hacer clic
        btnGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Cursor de mano
        btnGuardar.setPreferredSize(new Dimension(450, 50)); // Botón ancho y alto
        
        panelSur.add(btnGuardar);
        add(panelSur, BorderLayout.SOUTH);

        // ================= EVENTOS =================
        btnBuscar.addActionListener(e -> buscarPaciente());
        btnDiagnosticar.addActionListener(e -> generarDiagnostico());
        btnGuardar.addActionListener(e -> guardarDiagnosticoBD());
    }

    private void buscarPaciente() {
        try {
            String textoId = txtIdPaciente.getText().trim();
            if(textoId.isEmpty()) return;
            
            int id = Integer.parseInt(textoId);
            PacienteDAO dao = new PacienteDAO();
            Paciente p = dao.obtenerPorId(id);
            
            if (p != null) {
                this.pacienteActual = p;
                lblNombrePaciente.setText("Paciente: " + p.getNombre() + " (Edad: " + p.getEdad() + ")");
                lblNombrePaciente.setForeground(new Color(0, 102, 0)); // Verde
            } else {
                lblNombrePaciente.setText("Paciente: NO ENCONTRADO");
                lblNombrePaciente.setForeground(Color.RED);
                this.pacienteActual = null;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese un ID numérico válido.");
        }
    }

    private void generarDiagnostico() {
        // 1. Obtener síntomas seleccionados
        List<String> seleccionados = new ArrayList<>();
        for (Component c : panelSintomas.getComponents()) {
            if (c instanceof JCheckBox) {
                JCheckBox chk = (JCheckBox) c;
                if (chk.isSelected()) seleccionados.add(chk.getText());
            }
        }

        if (seleccionados.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione al menos un síntoma.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 2. Consultar al Motor de Inferencia
        List<String> resultadosMotor = motor.diagnosticar(seleccionados);
        
        // 3. Filtrar por categoría (Java side)
        String catFiltro = (String) comboCategoria.getSelectedItem();
        boolean filtrar = !"Todas".equalsIgnoreCase(catFiltro);
        
        // 4. Llenar tabla
        DefaultTableModel model = (DefaultTableModel) tablaResultados.getModel();
        model.setRowCount(0); // Limpiar

        boolean huboResultados = false;

        for (String nombreEnfermedad : resultadosMotor) {
            Optional<Enfermedad> optEnf = todasLasEnfermedades.stream()
                    .filter(e -> e.getNombre().equalsIgnoreCase(nombreEnfermedad))
                    .findFirst();
            
            if (optEnf.isPresent()) {
                Enfermedad enf = optEnf.get();
                
                if (filtrar && !enf.getCategoria().equalsIgnoreCase(catFiltro)) {
                    continue;
                }

                long matches = seleccionados.stream()
                        .filter(s -> enf.getSintomas().contains(s))
                        .count();
                
                String recomendacionesStr = String.join(", ", enf.getRecomendaciones());

                model.addRow(new Object[]{
                    enf.getNombre(),
                    enf.getCategoria(),
                    matches + " / " + enf.getSintomas().size(),
                    recomendacionesStr
                });
                huboResultados = true;
            }
        }

        if (!huboResultados) {
            JOptionPane.showMessageDialog(this, "No se encontraron enfermedades compatibles.", "Resultado", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void guardarDiagnosticoBD() {
        if (pacienteActual == null) {
            JOptionPane.showMessageDialog(this, "Primero debe buscar/seleccionar un Paciente (ID).", "Falta Paciente", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int fila = tablaResultados.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una enfermedad de la tabla para guardar.", "Selección requerida", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nombreEnfermedad = (String) tablaResultados.getValueAt(fila, 0);
        
        Enfermedad enfermedadSelec = todasLasEnfermedades.stream()
                .filter(e -> e.getNombre().equals(nombreEnfermedad))
                .findFirst().orElse(null);

        if (enfermedadSelec != null) {
            Diagnostico nuevoDiag = new Diagnostico(
                    pacienteActual,
                    enfermedadSelec,
                    enfermedadSelec.getCategoria(),
                    new ArrayList<>(), 
                    enfermedadSelec.getRecomendaciones()
            );

            DiagnosticoDAO diagDao = new DiagnosticoDAO();
            if (diagDao.guardar(nuevoDiag)) {
                JOptionPane.showMessageDialog(this, "Diagnóstico guardado exitosamente en el historial.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Error al guardar en la Base de Datos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}