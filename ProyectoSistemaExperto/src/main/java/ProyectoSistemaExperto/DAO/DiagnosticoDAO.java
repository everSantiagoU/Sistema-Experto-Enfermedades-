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
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DiagnosticoDAO {

    public void guardarDiagnostico(int idPaciente, int idEnfermedad) {
        String sql = "INSERT INTO diagnostico(id_paciente, id_enfermedad) VALUES (?, ?)";

        try (Connection con = ConexionJava.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPaciente);
            ps.setInt(2, idEnfermedad);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}