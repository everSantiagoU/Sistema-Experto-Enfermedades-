package ProyectoSistemaExperto.DAO;

import ProyectoSistemaExperto.models.Enfermedad;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnfermedadDAO {
    
    /**
     * Obtiene todas las enfermedades con sus síntomas desde MySQL
     */
    public List<Enfermedad> obtenerEnfermedades() {
        List<Enfermedad> enfermedades = new ArrayList<>();
        
        String sql = """
            SELECT e.id_enfermedad, e.nombre, e.categoria, e.recomendacion
            FROM enfermedad e
        """;
        
        try (Connection conn = ConexionDB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                int id = rs.getInt("id_enfermedad");
                String nombre = rs.getString("nombre");
                String categoria = rs.getString("categoria");
                String recomendacion = rs.getString("recomendacion");
                
                // Obtener síntomas de esta enfermedad
                List<String> sintomas = obtenerSintomasPorEnfermedad(id);
                
                // Convertir recomendación a lista
                List<String> recomendaciones = new ArrayList<>();
                for (String r : recomendacion.split(",")) {
                    recomendaciones.add(r.trim());
                }
                
                // Crear objeto Enfermedad
                Enfermedad enfermedad = new Enfermedad();
                enfermedad.setIdEnfermedad(id);
                enfermedad.setNombre(nombre);
                enfermedad.setCategoria(categoria);
                enfermedad.setSintomas(sintomas);
                enfermedad.setRecomendaciones(recomendaciones);
                
                enfermedades.add(enfermedad);
            }
            
        } catch (SQLException e) {
            System.err.println("Error obtenerEnfermedades(): " + e.getMessage());
            e.printStackTrace();
        }
        
        return enfermedades;
    }
    
    /**
     * Obtiene los síntomas de una enfermedad específica
     */
    private List<String> obtenerSintomasPorEnfermedad(int idEnfermedad) {
        List<String> sintomas = new ArrayList<>();
        
        String sql = """
            SELECT s.nombre
            FROM sintoma s
            INNER JOIN enfermedad_sintoma es ON s.id_sintoma = es.id_sintoma
            WHERE es.id_enfermedad = ?
        """;
        
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idEnfermedad);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                sintomas.add(rs.getString("nombre"));
            }
            
        } catch (SQLException e) {
            System.err.println("Error obtenerSintomasPorEnfermedad(): " + e.getMessage());
        }
        
        return sintomas;
    }
    
    /**
     * Obtiene una enfermedad por su nombre
     */
    public Enfermedad obtenerPorNombre(String nombre) {
        String sql = "SELECT * FROM enfermedad WHERE nombre = ?";
        
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, nombre);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                int id = rs.getInt("id_enfermedad");
                String categoria = rs.getString("categoria");
                String recomendacion = rs.getString("recomendacion");
                
                List<String> sintomas = obtenerSintomasPorEnfermedad(id);
                
                List<String> recomendaciones = new ArrayList<>();
                for (String r : recomendacion.split(",")) {
                    recomendaciones.add(r.trim());
                }
                
                Enfermedad enfermedad = new Enfermedad();
                enfermedad.setIdEnfermedad(id);
                enfermedad.setNombre(nombre);
                enfermedad.setCategoria(categoria);
                enfermedad.setSintomas(sintomas);
                enfermedad.setRecomendaciones(recomendaciones);
                
                return enfermedad;
            }
            
        } catch (SQLException e) {
            System.err.println("Error obtenerPorNombre(): " + e.getMessage());
        }
        
        return null;
    }
}
