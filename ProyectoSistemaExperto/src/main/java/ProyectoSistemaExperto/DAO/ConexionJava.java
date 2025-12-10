/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ProyectoSistemaExperto.DAO;

/**
 *
 * @author Johan Villada
 */
public class ConexionJava {
    private static final String URL = "direccion";
    private static final String user = "user";
    private static final String PASS = "";
    
    public static Connection getConnection(){
        Connection con = null;
        try {
            con = DriverManager.getConnection(URL, USER, PASS);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return con;
        }
    }
}
