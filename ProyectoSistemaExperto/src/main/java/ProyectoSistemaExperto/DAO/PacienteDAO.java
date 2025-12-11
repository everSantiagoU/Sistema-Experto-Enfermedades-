package ProyectoSistemaExperto.DAO;

import ProyectoSistemaExperto.models.Paciente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PacienteDAO {

    /**
     * Registra un nuevo paciente en la base de datos y retorna el ID generado.
     * @param paciente El objeto Paciente con nombre y edad.
     * @return El ID generado del paciente, o -1 si falla.
     */
    public int registrar(Paciente paciente) {
        // La tabla 'paciente' debería tener campos: id_paciente, nombre, edad.
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
                        // Opcional: setear el ID en el objeto Paciente si tu modelo lo tiene
                        // paciente.setIdPaciente(idGenerado); 
                    }
                }
            }
        } catch (SQLException e) {
            // Este error puede ocurrir si el nombre del paciente ya existe (si es UNIQUE)
            System.err.println("Error al registrar paciente: " + e.getMessage());
            e.printStackTrace();
        }
        return idGenerado;
    }

    /**
     * Busca un paciente por su nombre.
     * @param nombre El nombre del paciente.
     * @return El objeto Paciente completo, o null si no se encuentra.
     */
    public Paciente obtenerPorNombre(String nombre) {
        String sql = "SELECT id_paciente, nombre, edad FROM paciente WHERE nombre = ?";
        Paciente paciente = null;

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nombre);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Creamos el paciente, la lista de síntomas se inicializa vacía
                    paciente = new Paciente(
                        rs.getString("nombre"),
                        rs.getInt("edad"),
                        new ArrayList<>() 
                    );
                    // Opcional: setear el ID si el modelo Paciente lo soporta
                    // paciente.setIdPaciente(rs.getInt("id_paciente"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener paciente por nombre: " + e.getMessage());
        }
        return paciente;
    }

    /**
     * Obtiene un paciente por su ID.
     * @param id El ID del paciente.
     * @return El objeto Paciente completo, o null si no se encuentra.
     */
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
                    // paciente.setIdPaciente(rs.getInt("id_paciente"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener paciente por ID: " + e.getMessage());
        }
        return paciente;
    }
}
