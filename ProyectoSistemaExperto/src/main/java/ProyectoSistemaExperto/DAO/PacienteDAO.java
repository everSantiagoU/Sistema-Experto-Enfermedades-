package ProyectoSistemaExperto.DAO;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/* PROJECT: Sistema Experto
 * MODULE: Data Access Object
 * 
 * CLASS: PacienteDAO
 * DESCRIPTION: Class that implementes the pattern DAO that manages Paciente data persistence
 *              in MySQL database. Implements CRUD operations for Paciente and handles
 *              relationships with diagnostico.
 * 
 * 
 * @author Jhojan Villada
 * @version 1.1.0
 * @since 2025-12-8
 * @lastModified 2025-12-10
 * @since MySQL Connector/J 8.0
*/

import ProyectoSistemaExperto.models.Paciente;
import java.sql.*;
import java.util.ArrayList;

public class PacienteDAO {


    public int registrar(Paciente paciente) {
        String sql = "INSERT INTO paciente (nombre, edad) VALUES (?, ?)";
        int idGenerado = -1;

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, paciente.getNombre());
            pstmt.setInt(2, paciente.getEdad());

            int filasAfectadas = pstmt.executeUpdate();

            if (filasAfectadas > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        idGenerado = rs.getInt(1);
                        // Vinculacion buena entonces Actualizamos el objeto Java con el ID de la BD
                        paciente.setIdPaciente(idGenerado);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al registrar paciente: " + e.getMessage());
        }
        return idGenerado;
    }

    public Paciente obtenerPorNombre(String nombre) {
        String sql = "SELECT id_paciente, nombre, edad FROM paciente WHERE nombre = ?";
        Paciente paciente = null;

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nombre);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    paciente = new Paciente(
                        rs.getString("nombre"),
                        rs.getInt("edad"),
                        new ArrayList<>() 
                    );
                    paciente.setIdPaciente(rs.getInt("id_paciente"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener paciente por nombre: " + e.getMessage());
        }
        return paciente;
    }

    public Paciente obtenerPorId(int id) {
        String sql = "SELECT id_paciente, nombre, edad FROM paciente WHERE id_paciente = ?";
        Paciente paciente = null;

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    paciente = new Paciente(
                        rs.getString("nombre"),
                        rs.getInt("edad"),
                        new ArrayList<>()
                    );
                    paciente.setIdPaciente(rs.getInt("id_paciente"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener paciente por ID: " + e.getMessage());
        }
        return paciente;
    }
}
