package ProyectoSistemaExperto.inferencia;
import ProyectoSistemaExperto.DAO.EnfermedadDAO;
import ProyectoSistemaExperto.DAO.ConexionDB;
import ProyectoSistemaExperto.models.Enfermedad;

import java.util.Arrays;
import java.util.List;

public class MainPruebaMI {
    public static void main(String[] args) {

        System.out.println("===========================================");
        System.out.println("  PRUEBA INTEGRAL: DAO + MotorInferencia   ");
        System.out.println("===========================================\n");

        try {
            // 1. Inicializar motor
            MotorInferencia motor = new MotorInferencia();

            // 2. Verificar conexión MySQL y cargar enfermedades
            ConexionDB.testConnection();
            EnfermedadDAO dao = new EnfermedadDAO();
            List<Enfermedad> lista = dao.obtenerEnfermedades();

            System.out.println("✓ Enfermedades obtenidas desde MySQL: " + lista.size());

            // 3. Limpiar hechos previos del motor
            motor.limpiarBD();

            // 4. Cargar enfermedades en Prolog una por una
            System.out.println("\nCargando enfermedades en Prolog...");
            for (Enfermedad e : lista) {
                motor.agregarEnfermedad(e);
                System.out.println("  → Cargada: " + e.getNombre());
            }

            System.out.println("\n✓ Todas las enfermedades fueron cargadas correctamente en Prolog\n");

            // ===========================================================
            // 5. PRUEBAS DE INFERENCIA
            // ===========================================================

            // ---- PRUEBA 1: Diagnóstico por síntomas ----
            System.out.println("=== PRUEBA 1: diagnosticar([fiebre, tos]) ===");
            List<String> diag = motor.diagnosticar(Arrays.asList("fiebre", "tos"));
            System.out.println("Resultado: " + diag);

            // ---- PRUEBA 2: Enfermedades por categoría ----
            System.out.println("\n=== PRUEBA 2: diagnosticoPorCategoria('viral') ===");
            List<String> virales = motor.diagnosticoPorCategoria("viral");
            System.out.println("Virales: " + virales);

            // ---- PRUEBA 3: Enfermedades por síntoma ----
            System.out.println("\n=== PRUEBA 3: enfermedadesPorSintoma('fiebre') ===");
            List<String> fiebre = motor.enfermedadesPorSintoma("fiebre");
            System.out.println("Enfermedades con fiebre: " + fiebre);

            // ---- PRUEBA 4: Enfermedades crónicas ----
            System.out.println("\n=== PRUEBA 4: enfermedadesCronicas() ===");
            List<String> cronicas = motor.enfermedadesCronicas();
            System.out.println("Crónicas: " + cronicas);

            // ---- PRUEBA 5: Recomendación ----
            System.out.println("\n=== PRUEBA 5: recomendacion('Gripe') ===");
            List<String> rec = motor.recomendacion("Gripe");
            System.out.println("Recomendación: " + rec);

            System.out.println("\n===========================================");
            System.out.println("       TODAS LAS PRUEBAS COMPLETADAS       ");
            System.out.println("===========================================\n");

        } catch (Exception e) {
            System.err.println("✗ ERROR durante pruebas: " + e.getMessage());
            e.printStackTrace();
        }
    }


}
