package ProyectoSistemaExperto.views;

import ProyectoSistemaExperto.DAO.EnfermedadDAO;
import ProyectoSistemaExperto.models.Enfermedad;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VentanaNuevaEnfermedad extends JFrame {

    private JTextField txtNombre;
    private JComboBox<String> comboCategoria;
    private JTextArea txtSintomas;
    private JTextArea txtRecomendaciones;

    public VentanaNuevaEnfermedad() {
        setTitle("Agregar Nuevo Conocimiento Médico");
        setSize(600, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(15, 15));

        JLabel lblTitulo = new JLabel("Registrar Nueva Enfermedad", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        add(lblTitulo, BorderLayout.NORTH);

        JPanel panelForm = new JPanel(new GridLayout(4, 1, 10, 10));
        panelForm.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        JPanel p1 = new JPanel(new GridLayout(2, 2, 10, 5));
        p1.add(new JLabel("Nombre de la Enfermedad:"));
        txtNombre = new JTextField();
        p1.add(txtNombre);
        
        p1.add(new JLabel("Categoría:"));
        comboCategoria = new JComboBox<>(new String[]{"viral", "bacteriana", "crónica", "alergia", "otra"});
        p1.add(comboCategoria);
        
        JPanel p2 = new JPanel(new BorderLayout(5, 5));
        p2.add(new JLabel("Síntomas (separados por coma):"), BorderLayout.NORTH);
        txtSintomas = new JTextArea(4, 20);
        txtSintomas.setLineWrap(true);
        p2.add(new JScrollPane(txtSintomas), BorderLayout.CENTER);
        JLabel lblNota = new JLabel("Ej: fiebre, dolor de cabeza, manchas rojas");
        lblNota.setForeground(Color.GRAY);
        p2.add(lblNota, BorderLayout.SOUTH);

        JPanel p3 = new JPanel(new BorderLayout(5, 5));
        p3.add(new JLabel("Tratamiento / Recomendaciones:"), BorderLayout.NORTH);
        txtRecomendaciones = new JTextArea(4, 20);
        txtRecomendaciones.setLineWrap(true);
        p3.add(new JScrollPane(txtRecomendaciones), BorderLayout.CENTER);

        panelForm.add(p1);
        panelForm.add(p2);
        panelForm.add(p3);
        
        add(panelForm, BorderLayout.CENTER);

        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelSur.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        
        JButton btnGuardar = new JButton("GUARDAR CONOCIMIENTO");
        btnGuardar.setBackground(new Color(0, 153, 76));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnGuardar.setPreferredSize(new Dimension(250, 45));
        btnGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnGuardar.addActionListener(e -> guardarEnfermedad());
        panelSur.add(btnGuardar);
        
        add(panelSur, BorderLayout.SOUTH);
    }

    private void guardarEnfermedad() {
        String nombre = txtNombre.getText().trim();
        String sintomasRaw = txtSintomas.getText().trim();
        String recomendacionesRaw = txtRecomendaciones.getText().trim();
        String categoria = (String) comboCategoria.getSelectedItem();

        if (nombre.isEmpty() || sintomasRaw.isEmpty() || recomendacionesRaw.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<String> listaSintomas = Arrays.asList(sintomasRaw.split(","));
        listaSintomas.replaceAll(String::trim);
        
        List<String> listaRecomendaciones = Arrays.asList(recomendacionesRaw.split(","));
        listaRecomendaciones.replaceAll(String::trim);

        Enfermedad nueva = new Enfermedad(nombre, listaSintomas, categoria, listaRecomendaciones);

        // Guardar en BD
        EnfermedadDAO dao = new EnfermedadDAO();
        if (dao.registrarNuevaEnfermedad(nueva)) {
            JOptionPane.showMessageDialog(this, "¡Nueva enfermedad agregada al sistema exitosamente!\nAhora el sistema podrá diagnosticarla.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Error al guardar en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}