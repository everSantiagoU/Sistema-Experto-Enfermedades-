package ProyectoSistemaExperto.DAO;

import ProyectoSistemaExperto.models.Enfermedad;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnfermedadDAO {

    private Connection connection;

    public EnfermedadDAO() {
        this.connection = ConexionDB.getConnection();
    }

    public List<Enfermedad> obtenerEnfermedades() {
        List<Enfermedad> lista = new ArrayList<>();
        String sql = "SELECT * FROM enfermedad";

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String nombre = rs.getString("nombre");
                String categoria = rs.getString("categoria");
                String recStr = rs.getString("recomendacion");
                
                // Convertir string de recomendaciones a lista
                List<String> recomendaciones = new ArrayList<>();
                if(recStr != null && !recStr.isEmpty()) {
                    for(String r : recStr.split(",")) recomendaciones.add(r.trim());
                }

                // Obtener síntomas
                List<String> sintomas = obtenerSintomas(nombre);

                Enfermedad e = new Enfermedad(nombre, sintomas, categoria, recomendaciones);
                e.setIdEnfermedad(rs.getInt("id_enfermedad"));
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

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
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
    
    // Nueva enfermadad añadida
    public boolean registrarNuevaEnfermedad(Enfermedad nuevaEnfermedad) {
        String sqlEnfermedad = "INSERT INTO enfermedad (nombre, categoria, recomendacion) VALUES (?, ?, ?)";
        String sqlBuscarSintoma = "SELECT id_sintoma FROM sintoma WHERE nombre = ?";
        String sqlInsertarSintoma = "INSERT INTO sintoma (nombre) VALUES (?)";
        String sqlRelacion = "INSERT INTO enfermedad_sintoma (id_enfermedad, id_sintoma) VALUES (?, ?)";

        try {
            //
            connection.setAutoCommit(false);

            // Insertar Enfermedad
            int idEnfermedad = -1;
            try (PreparedStatement ps = connection.prepareStatement(sqlEnfermedad, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, nuevaEnfermedad.getNombre());
                ps.setString(2, nuevaEnfermedad.getCategoria());
                ps.setString(3, String.join(", ", nuevaEnfermedad.getRecomendaciones()));
                ps.executeUpdate();
                
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) idEnfermedad = rs.getInt(1);
                }
            }

            if (idEnfermedad == -1) throw new SQLException("No se pudo obtener ID de la enfermedad.");

            // Procesar Síntomas
            for (String nombreSintoma : nuevaEnfermedad.getSintomas()) {
                int idSintoma = -1;
                nombreSintoma = nombreSintoma.toLowerCase().trim();

                // Buscar si existe
                try (PreparedStatement psBuscar = connection.prepareStatement(sqlBuscarSintoma)) {
                    psBuscar.setString(1, nombreSintoma);
                    try (ResultSet rs = psBuscar.executeQuery()) {
                        if (rs.next()) idSintoma = rs.getInt(1);
                    }
                }

                // Si no existe insertar
                if (idSintoma == -1) {
                    try (PreparedStatement psInsert = connection.prepareStatement(sqlInsertarSintoma, Statement.RETURN_GENERATED_KEYS)) {
                        psInsert.setString(1, nombreSintoma);
                        psInsert.executeUpdate();
                        try (ResultSet rs = psInsert.getGeneratedKeys()) {
                            if (rs.next()) idSintoma = rs.getInt(1);
                        }
                    }
                }

                // Crear relación
                try (PreparedStatement psRel = connection.prepareStatement(sqlRelacion)) {
                    psRel.setInt(1, idEnfermedad);
                    psRel.setInt(2, idSintoma);
                    psRel.executeUpdate();
                }
            }

            // Confirmar transacción
            connection.commit();
            return true;

        } catch (SQLException e) {
            try {
                connection.rollback(); // Deshacer cambios si algo falla
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            System.err.println("Error registrando enfermedad: " + e.getMessage());
            return false;
        } finally {
            try {
                connection.setAutoCommit(true); // Restaurar estado normal
            } catch (SQLException e) { e.printStackTrace(); }
        }
    }
}