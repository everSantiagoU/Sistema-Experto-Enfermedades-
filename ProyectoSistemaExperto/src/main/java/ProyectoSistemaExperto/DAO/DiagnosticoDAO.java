package ProyectoSistemaExperto.DAO;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
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
        // Asegúrate de que ConexionDB tenga las credenciales correctas (appuser)
        this.connection = ConexionDB.getConnection();
    }

    /**
     * Guarda diagnóstico en MySQL
     */
    public boolean guardar(Diagnostico d) {
        // Usamos subconsultas para obtener los IDs basándonos en los nombres
        String sql = "INSERT INTO diagnostico(id_paciente, id_enfermedad) " +
                "VALUES ((SELECT id_paciente FROM paciente WHERE nombre = ? LIMIT 1), " +
                "(SELECT id_enfermedad FROM enfermedad WHERE nombre = ? LIMIT 1))";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, d.getPaciente().getNombre());
            st.setString(2, d.getNombreEnfermedad().getNombre());
            st.executeUpdate();
            return true;

        } catch (Exception e) {
            System.err.println("ERROR guardar(Diagnostico): " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene diagnósticos por nombre de paciente
     */
    public List<Diagnostico> obtenerPorPaciente(String nombrePaciente) {
        List<Diagnostico> lista = new ArrayList<>();

        String sql = "SELECT d.fecha, e.nombre, e.categoria, e.recomendacion " +
                "FROM diagnostico d " +
                "JOIN paciente p ON p.id_paciente = d.id_paciente " +
                "JOIN enfermedad e ON e.id_enfermedad = d.id_enfermedad " +
                "WHERE p.nombre = ?";

        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, nombrePaciente);

            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                List<String> recomendaciones = splitRecomendaciones(rs.getString("recomendacion"));
                
                // AHORA SÍ FUNCIONA: Usamos el constructor vacío y setters
                Enfermedad enf = new Enfermedad();
                enf.setNombre(rs.getString("nombre"));
                enf.setCategoria(rs.getString("categoria"));
                enf.setRecomendaciones(recomendaciones);
                // No seteamos síntomas porque la consulta no los trae, queda la lista vacía por defecto

                Paciente p = new Paciente(nombrePaciente, 0, new ArrayList<>());

                Diagnostico diag = new Diagnostico(
                        p,
                        enf,
                        rs.getString("categoria"),
                        new ArrayList<>(), // Síntomas del diagnóstico (vacío por ahora)
                        recomendaciones
                );
                lista.add(diag);
            }

            rs.close();
            st.close();

        } catch (Exception e) {
            System.err.println("ERROR obtenerPorPaciente(): " + e.getMessage());
            e.printStackTrace();
        }

        return lista;
    }

    /**
     * Obtiene todos los diagnósticos
     */
    public List<Diagnostico> obtenerDiagnosticos() {
        List<Diagnostico> lista = new ArrayList<>();

        String sql = "SELECT d.fecha, e.nombre, e.categoria, e.recomendacion " +
                "FROM diagnostico d " +
                "JOIN enfermedad e ON e.id_enfermedad = d.id_enfermedad";

        try {
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                List<String> recomendaciones = splitRecomendaciones(rs.getString("recomendacion"));

                Enfermedad enf = new Enfermedad();
                enf.setNombre(rs.getString("nombre"));
                enf.setCategoria(rs.getString("categoria"));
                enf.setRecomendaciones(recomendaciones);

                Paciente p = new Paciente("Desconocido", 0, new ArrayList<>());

                Diagnostico diag = new Diagnostico(
                        p,
                        enf,
                        rs.getString("categoria"),
                        new ArrayList<>(),
                        recomendaciones
                );
                lista.add(diag);
            }

            rs.close();
            st.close();

        } catch (Exception e) {
            System.err.println("ERROR obtenerDiagnosticos(): " + e.getMessage());
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