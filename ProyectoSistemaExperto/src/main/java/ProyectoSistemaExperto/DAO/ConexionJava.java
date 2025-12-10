/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ProyectoSistemaExperto.DAO;

/**
 *
 * @author Johan Villada
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionJava {

    private static final String URL = "jdbc:mysql://localhost:3306/sistema_experto";
    private static final String USER = "root";
    private static final String PASS = ""; // tu contraseña o vacío

    public static Connection getConnection() {
        Connection con = null;
        try {
            con = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("Conectado a MySQL correctamente");
        } catch (SQLException e) {
            System.out.println("Error conectando a MySQL");
            e.printStackTrace();
        }
        return con;
    }
}
