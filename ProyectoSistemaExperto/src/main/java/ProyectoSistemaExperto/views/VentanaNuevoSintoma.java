package ProyectoSistemaExperto.views;

import ProyectoSistemaExperto.DAO.EnfermedadDAO;

import javax.swing.*;
import java.awt.*;

public class VentanaNuevoSintoma extends JFrame {

    private JTextField txtSintoma;

    public VentanaNuevoSintoma() {
        setTitle("Agregar Nuevo Síntoma");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(20, 20));

        JLabel lblTitulo = new JLabel("Registrar Nuevo Síntoma", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        add(lblTitulo, BorderLayout.NORTH);

        JPanel panelCentral = new JPanel(new GridLayout(2, 1, 10, 10));
        panelCentral.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));
        
        panelCentral.add(new JLabel("Nombre del Síntoma:"));
        txtSintoma = new JTextField();
        panelCentral.add(txtSintoma);
        
        add(panelCentral, BorderLayout.CENTER);

        JButton btnGuardar = new JButton("GUARDAR SÍNTOMA");
        btnGuardar.setBackground(new Color(0, 153, 76));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnGuardar.setPreferredSize(new Dimension(200, 45));
        btnGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelSur.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        panelSur.add(btnGuardar);
        
        add(panelSur, BorderLayout.SOUTH);

        btnGuardar.addActionListener(e -> guardar());
    }

    private void guardar() {
        String nombre = txtSintoma.getText().trim();
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Escriba un nombre.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        EnfermedadDAO dao = new EnfermedadDAO();
        if (dao.registrarSintoma(nombre)) {
            JOptionPane.showMessageDialog(this, "¡Síntoma '" + nombre + "' agregado exitosamente!");
            txtSintoma.setText(""); // Limpiar para agregar otro
        } else {
            JOptionPane.showMessageDialog(this, "Error: El síntoma ya existe o falló la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
