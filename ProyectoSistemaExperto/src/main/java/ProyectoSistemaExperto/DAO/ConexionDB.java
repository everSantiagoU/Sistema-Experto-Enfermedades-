package ProyectoSistemaExperto.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {
    // Usamos 'appuser' como default si no se pasan variables de entorno
    private static final String URL = (System.getProperty("DB_URL") != null)
            ? System.getProperty("DB_URL")
            : System.getenv().getOrDefault("DB_URL", "jdbc:mysql://localhost:3306/sistema_experto");

    private static final String USER = (System.getProperty("DB_USER") != null)
            ? System.getProperty("DB_USER")
            : System.getenv().getOrDefault("DB_USER", "appuser"); // Cambiado a appuser

    private static final String PASS = (System.getProperty("DB_PASS") != null)
            ? System.getProperty("DB_PASS")
            : System.getenv().getOrDefault("DB_PASS", "AppPassword123"); // Contraseña correcta

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (Exception e) {
            System.err.println("ERROR en la conexión MySQL: " + e.getMessage());
            return null;
        }
    }

    public static void testConnection() {
        try (Connection conn = getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("✓ Conexión exitosa a MySQL");
                System.out.println("✓ Base de datos: sistema_experto");
            } else {
                System.err.println("✗ No se pudo establecer la conexión con las credenciales proporcionadas.");
            }
        } catch (SQLException e) {
            System.err.println("✗ Error de conexión: " + e.getMessage());
            e.printStackTrace();
        }
    }
}