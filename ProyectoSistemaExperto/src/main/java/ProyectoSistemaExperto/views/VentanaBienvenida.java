package ProyectoSistemaExperto.views;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import javax.swing.*;
import java.awt.*;

public class VentanaBienvenida extends JFrame {

    public VentanaBienvenida() {

        setTitle("Sistema Experto de Diagnostico Medico");
        setSize(800, 600);
        setLocationRelativeTo(null); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel titulo = new JLabel("Sistema Experto de Diagnostico Medico", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 40));
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        add(titulo, BorderLayout.NORTH);

        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new GridLayout(6, 1, 10, 10));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(20, 100, 20, 100));

        JButton btnPaciente = new JButton("Registrar Paciente");
        JButton btnDiagnostico = new JButton("Generar Diagnostico");
        JButton btnHistorial = new JButton("Historial de Diagnosticos");
        JButton btnAgregar = new JButton("Agregar Nueva Enfermedad");
        JButton btnSalir = new JButton("Salir del sistema");

        panelBotones.add(btnPaciente);
        panelBotones.add(btnDiagnostico);
        panelBotones.add(btnHistorial);
        panelBotones.add(btnAgregar);
        panelBotones.add(btnSalir);

        add(panelBotones, BorderLayout.CENTER);

        // Eventos

        btnPaciente.addActionListener(e -> new RegistroPaciente().setVisible(true));

        btnDiagnostico.addActionListener(e -> new VentanaDiagnostico().setVisible(true));
        
        btnHistorial.addActionListener(e -> new VentanaHistorial().setVisible(true));
        
        btnAgregar.addActionListener(e -> new VentanaNuevaEnfermedad().setVisible(true));

        btnSalir.addActionListener(e -> System.exit(0));
    }
}

