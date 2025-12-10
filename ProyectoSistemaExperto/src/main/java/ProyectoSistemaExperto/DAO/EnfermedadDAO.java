package ProyectoSistemaExperto.DAO;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Johan Villada
 */
import ProyectoSistemaExperto.models.Enfermedad;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnfermedadDAO {

    public List<Enfermedad> obtenerEnfermedades() {
        List<Enfermedad> lista = new ArrayList<>();

        String sql = "SELECT * FROM enfermedad";

        try (Connection con = ConexionJava.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String nombre = rs.getString("nombre");
                String categoria = rs.getString("categoria");
                String recomendacion = rs.getString("recomendacion");

                // obtener síntomas
                List<String> sintomas = obtenerSintomas(nombre);

                Enfermedad e = new Enfermedad(
                        nombre,
                        sintomas,
                        categoria,
                        List.of(recomendacion)
                );

                lista.add(e);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public List<String> obtenerSintomas(String nombreEnfermedad) {
        List<String> sintomas = new ArrayList<>();

        String sql = """
            SELECT s.nombre
            FROM sintoma s
            INNER JOIN enfermedad_sintoma es ON s.id_sintoma = es.id_sintoma
            INNER JOIN enfermedad e ON e.id_enfermedad = es.id_enfermedad
            WHERE e.nombre = ?
        """;

        try (Connection con = ConexionJava.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nombreEnfermedad);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                sintomas.add(rs.getString("nombre"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sintomas;
    }
}