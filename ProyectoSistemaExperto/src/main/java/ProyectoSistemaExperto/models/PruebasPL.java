package ProyectoSistemaExperto.models;

import org.jpl7.Query;
import java.io.File;
import java.net.URL;

public class PruebasPL {
    public static void main(String[] args) {
        try {
            // Cargar desde resources
            URL resource = PruebasPL.class.getClassLoader().getResource("reglas.pl");
            
            if (resource == null) {
                System.err.println("✗ ERROR: reglas.pl no encontrado en src/main/resources");
                return;
            }
            
            String prologPath = new File(resource.toURI()).getAbsolutePath().replace("\\", "/");
            System.out.println("Archivo encontrado en: " + prologPath);
            
            // Intentar cargar archivo
            Query q = new Query("consult('" + prologPath + "')");
            
            if (q.hasSolution()) {
                System.out.println("\n✓ CONEXIÓN EXITOSA: El archivo Prolog se cargó correctamente");
                System.out.println("✓ JPL está funcionando correctamente");
                System.out.println("✓ Listo para cargar hechos dinámicamente desde MySQL");
            } else {
                System.err.println("\n✗ ERROR: No se pudo cargar el archivo");
            }
            q.close();
            
        } catch (Exception e) {
            System.err.println("✗ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
