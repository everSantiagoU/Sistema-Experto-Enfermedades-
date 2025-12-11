package ProyectoSistemaExperto.DAO;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/* PROJECT: Sistema Experto
 * MODULE: Database Connectivity
 * 
 * CLASS: ConexionDB
 * DESCRIPTION: Provides MySQL database connection management for the expert system
 * 
 * This class handles database connectivity with support for environment-based
 * configuration. It implements a connection factory pattern with automatic
 * fallback to default values when environment variables or system properties
 * are not provided.
 * 
 * @author Jhojan Villada
 * @version 1.1.0
 * @since 2025-12-8
 * @lastModified 2025-12-11
 * @since MySQL Connector/J 8.0
*/

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {
    
    private static final String URL = (System.getProperty("DB_URL") != null)
            ? System.getProperty("DB_URL")
            : System.getenv().getOrDefault("DB_URL", "jdbc:mysql://localhost:3306/sistema_experto");

    private static final String USER = (System.getProperty("DB_USER") != null)
            ? System.getProperty("DB_USER")
            : System.getenv().getOrDefault("DB_USER", "appuser"); // Cambiado a appuser

    private static final String PASS = (System.getProperty("DB_PASS") != null)
            ? System.getProperty("DB_PASS")
            : System.getenv().getOrDefault("DB_PASS", "AppPassword123"); // Contraseña correcta

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (Exception e) {
            System.err.println("Error en la conexion con MySQL: " + e.getMessage());
            return null;
        }
    }

    public static void testConnection() {
        try (Connection conn = getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("Conexion exitosa a MySQL");
                System.out.println("Base de datos: sistema_experto");
            } else {
                System.err.println("No se pudo establecer la conexion ");
            }
        } catch (SQLException e) {
            System.err.println(" Error de conexion: " + e.getMessage());
            e.printStackTrace();
        }
    }
}