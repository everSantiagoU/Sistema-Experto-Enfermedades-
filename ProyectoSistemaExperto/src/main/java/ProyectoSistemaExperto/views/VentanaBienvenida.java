package ProyectoSistemaExperto.views;

import javax.swing.*;
import java.awt.*;

public class VentanaBienvenida extends JFrame {

    public VentanaBienvenida() {

        setTitle("Sistema Experto de Diagnostico Medico");
        setSize(800, 700); // Un poco más alto para que quepan los botones
        setLocationRelativeTo(null); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel titulo = new JLabel("Sistema Experto de Diagnóstico Médico", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 36));
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        add(titulo, BorderLayout.NORTH);

        JPanel panelBotones = new JPanel();
        // Aumentamos a 6 filas para el nuevo botón
        panelBotones.setLayout(new GridLayout(6, 1, 10, 15)); 
        panelBotones.setBorder(BorderFactory.createEmptyBorder(20, 100, 20, 100));

        // Creación de Botones
        JButton btnPaciente = new JButton("Registrar Paciente");
        JButton btnDiagnostico = new JButton("Generar Diagnóstico");
        JButton btnHistorial = new JButton("Historial de Diagnósticos");
        JButton btnNuevaEnfermedad = new JButton("Agregar Nueva Enfermedad"); // Ya lo tenías
        JButton btnNuevoSintoma = new JButton("Agregar Nuevo Síntoma");     // <--- NUEVO BOTÓN
        JButton btnSalir = new JButton("Salir del sistema");

        // Estilo opcional para diferenciar las acciones de gestión
        Font fontNormal = new Font("SansSerif", Font.PLAIN, 16);
        Font fontGestion = new Font("SansSerif", Font.BOLD, 16);
        
        btnPaciente.setFont(fontNormal);
        btnDiagnostico.setFont(fontNormal);
        btnHistorial.setFont(fontNormal);
        
        // Resaltar botones de "Administración/Aprendizaje"
        btnNuevaEnfermedad.setFont(fontGestion);
        btnNuevaEnfermedad.setForeground(new Color(0, 102, 0)); // Verde oscuro
        
        btnNuevoSintoma.setFont(fontGestion);
        btnNuevoSintoma.setForeground(new Color(0, 102, 0)); // Verde oscuro

        btnSalir.setFont(fontNormal);
        btnSalir.setForeground(Color.RED);

        // Añadir al panel en orden
        panelBotones.add(btnPaciente);
        panelBotones.add(btnDiagnostico);
        panelBotones.add(btnHistorial);
        panelBotones.add(btnNuevaEnfermedad);
        panelBotones.add(btnNuevoSintoma); // Añadimos el nuevo
        panelBotones.add(btnSalir);

        add(panelBotones, BorderLayout.CENTER);

        // =====================================================
        // EVENTOS
        // =====================================================

        btnPaciente.addActionListener(e -> new RegistroPaciente().setVisible(true));

        btnDiagnostico.addActionListener(e -> new VentanaDiagnostico().setVisible(true));
        
        btnHistorial.addActionListener(e -> new VentanaHistorial().setVisible(true));
        
        btnNuevaEnfermedad.addActionListener(e -> new VentanaNuevaEnfermedad().setVisible(true));
        
        // Evento del nuevo botón
        btnNuevoSintoma.addActionListener(e -> new VentanaNuevoSintoma().setVisible(true));

        btnSalir.addActionListener(e -> System.exit(0));
    }
}

