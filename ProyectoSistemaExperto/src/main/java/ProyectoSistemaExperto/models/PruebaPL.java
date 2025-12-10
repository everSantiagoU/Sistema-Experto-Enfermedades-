package ProyectoSistemaExperto.models;

import org.jpl7.Query;
import org.jpl7.Term;
import java.io.File;
import java.util.Map;

public class PruebaPL {
    public static void main(String[] args) {
        File prologFile = new File("reglas.pl");
        
        Query q = new Query("consult('reglas.pl')");
        if (!q.hasSolution()) {
            System.err.println("ERROR: No se pudo cargar el archivo");
            return;
        }
        System.out.println("✓ Archivo Prolog cargado correctamente.\n");
        q.close();
        
        // ============================================
        // PRUEBA 1: coincide_sintomas
        // ============================================
        System.out.println("========================================");
        System.out.println("PRUEBA 1: coincide_sintomas");
        System.out.println("========================================");
        
        Query p1a = new Query("coincide_sintomas([fiebre, tos], gripe)");
        System.out.println("¿coincide_sintomas([fiebre, tos], gripe)? " + p1a.hasSolution());
        p1a.close();
        
        Query p1b = new Query("coincide_sintomas([fiebre, dolor], gripe)");
        System.out.println("¿coincide_sintomas([fiebre, dolor], gripe)? " + p1b.hasSolution());
        p1b.close();
        
        Query p1c = new Query("coincide_sintomas([], gripe)");
        System.out.println("¿coincide_sintomas([], gripe)? " + p1c.hasSolution());
        p1c.close();
        
        // ============================================
        // PRUEBA 2: diagnostico
        // ============================================
        System.out.println("\n========================================");
        System.out.println("PRUEBA 2: diagnostico");
        System.out.println("========================================");
        
        Query p2a = new Query("diagnostico([fiebre, epen], Enfermedad)");
        System.out.println("diagnostico([fiebre, epen], Enfermedad):");
        while (p2a.hasMoreSolutions()) {
            Map<String, Term> sol = p2a.nextSolution();
            System.out.println("  - " + sol.get("Enfermedad"));
        }
        p2a.close();
        
        Query p2b = new Query("diagnostico([fiebre, diarrea], Enfermedad)");
        System.out.println("\ndiagnostico([fiebre, diarrea], Enfermedad):");
        while (p2b.hasMoreSolutions()) {
            Map<String, Term> sol = p2b.nextSolution();
            System.out.println("  - " + sol.get("Enfermedad"));
        }
        p2b.close();
        
        // ============================================
        // PRUEBA 3: diagnostico_categoria
        // ============================================
        System.out.println("\n========================================");
        System.out.println("PRUEBA 3: diagnostico_categoria");
        System.out.println("========================================");
        
        Query p3a = new Query("diagnostico_categoria(virus, Enfermedad)");
        System.out.println("diagnostico_categoria(virus, Enfermedad):");
        while (p3a.hasMoreSolutions()) {
            Map<String, Term> sol = p3a.nextSolution();
            System.out.println("  - " + sol.get("Enfermedad"));
        }
        p3a.close();
        
        Query p3b = new Query("diagnostico_categoria(cronica, Enfermedad)");
        System.out.println("\ndiagnostico_categoria(cronica, Enfermedad):");
        while (p3b.hasMoreSolutions()) {
            Map<String, Term> sol = p3b.nextSolution();
            System.out.println("  - " + sol.get("Enfermedad"));
        }
        p3b.close();
        
        // ============================================
        // PRUEBA 4: recomendacion
        // ============================================
        System.out.println("\n========================================");
        System.out.println("PRUEBA 4: recomendacion");
        System.out.println("========================================");
        
        Query p4a = new Query("recomendacion(gripe, Recomendaciones)");
        if (p4a.hasSolution()) {
            Map<String, Term> sol = p4a.oneSolution();
            System.out.println("recomendacion(gripe, Recomendaciones): " + sol.get("Recomendaciones"));
        }
        p4a.close();
        
        Query p4b = new Query("recomendacion(diabetes, Recomendaciones)");
        if (p4b.hasSolution()) {
            Map<String, Term> sol = p4b.oneSolution();
            System.out.println("recomendacion(diabetes, Recomendaciones): " + sol.get("Recomendaciones"));
        }
        p4b.close();
        
        // ============================================
        // PRUEBA 5: enfermedades_cronicas
        // ============================================
        System.out.println("\n========================================");
        System.out.println("PRUEBA 5: enfermedades_cronicas");
        System.out.println("========================================");
        
        Query p5 = new Query("enfermedades_cronicas(Enfermedad)");
        System.out.println("enfermedades_cronicas(Enfermedad):");
        while (p5.hasMoreSolutions()) {
            Map<String, Term> sol = p5.nextSolution();
            System.out.println("  - " + sol.get("Enfermedad"));
        }
        p5.close();
        
        // ============================================
        // PRUEBA 6: enfermedades_por_sintoma
        // ============================================
        System.out.println("\n========================================");
        System.out.println("PRUEBA 6: enfermedades_por_sintoma");
        System.out.println("========================================");
        
        Query p6a = new Query("enfermedades_por_sintoma(fiebre, Enfermedad)");
        System.out.println("enfermedades_por_sintoma(fiebre, Enfermedad):");
        while (p6a.hasMoreSolutions()) {
            Map<String, Term> sol = p6a.nextSolution();
            System.out.println("  - " + sol.get("Enfermedad"));
        }
        p6a.close();
        
        Query p6b = new Query("enfermedades_por_sintoma(dolor_cabeza, Enfermedad)");
        System.out.println("\nenfermedades_por_sintoma(dolor_cabeza, Enfermedad):");
        while (p6b.hasMoreSolutions()) {
            Map<String, Term> sol = p6b.nextSolution();
            System.out.println("  - " + sol.get("Enfermedad"));
        }
        p6b.close();
        
        System.out.println("\n========================================");
        System.out.println("✓ TODAS LAS PRUEBAS COMPLETADAS");
        System.out.println("========================================");
    }
}