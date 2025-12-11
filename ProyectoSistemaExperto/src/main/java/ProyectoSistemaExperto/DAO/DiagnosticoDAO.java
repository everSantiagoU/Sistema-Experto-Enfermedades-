package ProyectoSistemaExperto.DAO;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/* PROJECT: Sistema Experto
 * MODULE: Data Access Object
 * 
 * CLASS: DiagnosticoDAO
 * DESCRIPTION: Class that implementes the pattern DAO that manages diagnosis data persistence
 *              in MySQL database. Implements CRUD operations for diagnoses and handles
 *              relationships between Paciente and enfermedad.
 * 
 * 
 * @author Jhojan Villada
 * @version 1.1.0
 * @since 2025-12-8
 * @lastModified 2025-12-11
 * @since MySQL Connector/J 8.0
*/


import ProyectoSistemaExperto.models.Diagnostico;
import ProyectoSistemaExperto.models.Paciente;
import ProyectoSistemaExperto.models.Enfermedad;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DiagnosticoDAO {

    private Connection connection;

    public DiagnosticoDAO() {
        this.connection = ConexionDB.getConnection();
    }

    /**
     * Guardar diagnostico en Mysql
     */
    public boolean guardar(Diagnostico d) {
        String sql = "INSERT INTO diagnostico(id_paciente, id_enfermedad) " +
                "VALUES (?, (SELECT id_enfermedad FROM enfermedad WHERE nombre = ? LIMIT 1))";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, d.getPaciente().getIdPaciente()); 
            st.setString(2, d.getNombreEnfermedad().getNombre());
            st.executeUpdate();
            return true;

        } catch (Exception e) {
            System.err.println("ERROR guardar(Diagnostico): " + e.getMessage());
            return false;
        }
    }

    public List<Diagnostico> obtenerPorIdPaciente(int idPaciente) {
        List<Diagnostico> lista = new ArrayList<>();

        String sql = "SELECT d.fecha, e.nombre, e.categoria, e.recomendacion, p.nombre as nombre_paciente, p.edad " +
                "FROM diagnostico d " +
                "JOIN paciente p ON p.id_paciente = d.id_paciente " +
                "JOIN enfermedad e ON e.id_enfermedad = d.id_enfermedad " +
                "WHERE p.id_paciente = ? " +
                "ORDER BY d.fecha DESC";

        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, idPaciente);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                List<String> recomendaciones = splitRecomendaciones(rs.getString("recomendacion"));

                Enfermedad enf = new Enfermedad();
                enf.setNombre(rs.getString("nombre"));
                enf.setCategoria(rs.getString("categoria"));
                enf.setRecomendaciones(recomendaciones);

                Paciente p = new Paciente(rs.getString("nombre_paciente"), rs.getInt("edad"), new ArrayList<>());
                p.setIdPaciente(idPaciente);

                Diagnostico diag = new Diagnostico(
                        p, enf, rs.getString("categoria"), new ArrayList<>(), recomendaciones
                );
                
                // AHORA SÍ FUNCIONARÁ: Asignamos la fecha real de la base de datos
                if (rs.getTimestamp("fecha") != null) {
                    diag.setFecha(rs.getTimestamp("fecha").toLocalDateTime());
                }

                lista.add(diag);
            }
            rs.close();
            st.close();
        } catch (Exception e) {
            System.err.println("ERROR obtenerPorIdPaciente(): " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }

    public List<Diagnostico> obtenerTodos() { //obtener todos los diagnosticos para estadisticas
        List<Diagnostico> lista = new ArrayList<>();
        String sql = "SELECT e.nombre, e.categoria FROM diagnostico d " +
                     "JOIN enfermedad e ON e.id_enfermedad = d.id_enfermedad";

        try (PreparedStatement st = connection.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                Enfermedad enf = new Enfermedad();
                enf.setNombre(rs.getString("nombre"));
                enf.setCategoria(rs.getString("categoria"));
                
                Diagnostico diag = new Diagnostico(null, enf, rs.getString("categoria"), null, null);
                lista.add(diag);
            }
        } catch (Exception e) {
            System.err.println("ERROR obtenerTodos(): " + e.getMessage());
        }
        return lista;
    }

    private List<String> splitRecomendaciones(String r) {
        List<String> list = new ArrayList<>();
        if (r == null || r.isEmpty()) return list;
        String[] parts = r.split(",");
        for (String p : parts) list.add(p.trim());
        return list;
    }
}