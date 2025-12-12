package ProyectoSistemaExperto.views;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import com.formdev.flatlaf.FlatLightLaf;
/**
 *
 * @author EVER URIBE
 */
public class ProyectoSistemaExperto {
    public static void main(String[] args) {
        FlatLightLaf.setup(); // activa apariencia de la lib flatlaf

        java.awt.EventQueue.invokeLater(() -> {
            new VentanaBienvenida().setVisible(true);
        });
    }
}
