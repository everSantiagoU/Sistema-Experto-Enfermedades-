package ProyectoSistemaExperto.DAO;

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
     * Guarda diagnóstico en MySQL
     */
    public boolean guardar(Diagnostico d) {
        String sql = "INSERT INTO diagnostico(id_paciente, id_enfermedad) " +
                "VALUES (?, (SELECT id_enfermedad FROM enfermedad WHERE nombre = ? LIMIT 1))";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, d.getPaciente().getIdPaciente()); // Usamos ID directo del objeto
            st.setString(2, d.getNombreEnfermedad().getNombre());
            st.executeUpdate();
            return true;

        } catch (Exception e) {
            System.err.println("ERROR guardar(Diagnostico): " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene historial por ID de Paciente (Para la tabla de historial)
     */
    public List<Diagnostico> obtenerPorIdPaciente(int idPaciente) {
        List<Diagnostico> lista = new ArrayList<>();

        String sql = "SELECT d.fecha, e.nombre, e.categoria, e.recomendacion, p.nombre as nombre_paciente, p.edad " +
                "FROM diagnostico d " +
                "JOIN paciente p ON p.id_paciente = d.id_paciente " +
                "JOIN enfermedad e ON e.id_enfermedad = d.id_enfermedad " +
                "WHERE p.id_paciente = ? " +
                "ORDER BY d.fecha DESC"; // Ordenado por fecha reciente

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
                // Insertamos la fecha real de la BD (si tu modelo Diagnostico lo permite, si no usa current)
                // diag.setFecha(rs.getTimestamp("fecha").toLocalDateTime()); 
                lista.add(diag);
            }
            rs.close();
            st.close();
        } catch (Exception e) {
            System.err.println("ERROR obtenerPorIdPaciente(): " + e.getMessage());
        }
        return lista;
    }

    /**
     * Obtiene TODOS los diagnósticos (Para Estadísticas)
     */
    public List<Diagnostico> obtenerTodos() {
        List<Diagnostico> lista = new ArrayList<>();
        String sql = "SELECT e.nombre, e.categoria FROM diagnostico d " +
                     "JOIN enfermedad e ON e.id_enfermedad = d.id_enfermedad";

        try (PreparedStatement st = connection.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                Enfermedad enf = new Enfermedad();
                enf.setNombre(rs.getString("nombre"));
                enf.setCategoria(rs.getString("categoria"));
                // No necesitamos mas datos para las estadisticas basicas
                
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