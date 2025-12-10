package ProyectoSistemaExperto.models;
import java.util.Arrays;
import java.util.List;
/**
 *
 * @author EVER URIBE
 */
public class MainPruebaClases {
    public static void main(String[] args) {

        // 1. Crear una enfermedad de prueba
        List<String> sintomasGripe = Arrays.asList("fiebre", "tos", "cansancio, pene");
        List<String> recomendacionesGripe = Arrays.asList("descanso", "hidratarse", "paracetamol");

        Enfermedad gripe = new Enfermedad(
                "Gripe",
                sintomasGripe,
                "viral",
                recomendacionesGripe
        );

        System.out.println("=== Enfermedad creada ===");
        System.out.println(gripe);
        System.out.println();
        
        List<String> sintomasHepatitis = Arrays.asList("fiebre", "tos", "cansancio", "sin_hambre");
        List<String> recomendacionesHepatitis = Arrays.asList("descanso", "hidratarse", "antibiotico");
        
        Enfermedad hepatitis = new Enfermedad(
                "hepatitis",
                sintomasHepatitis,
                "cronica",
                recomendacionesHepatitis
        );

        System.out.println("=== Enfermedad creada ===");
        System.out.println(hepatitis);
        System.out.println();
        
        
        // 2. Crear un paciente con síntomas
        List<String> sintomasPaciente1 = Arrays.asList("fiebre", "tos", "sin_hambre");

        Paciente paciente1 = new Paciente(
                "Carlos",
                25,
                sintomasPaciente1
        );
           
        List<String> sintomasPaciente2 = Arrays.asList("fiebre", "tos", "sin_hambre");
        Paciente paciente2 = new Paciente(
                "Pedro",
                28,
                sintomasPaciente2
        );

        System.out.println("=== Paciente creado ===");
        System.out.println("Nombre: " + paciente1.getNombre());
        System.out.println("Edad: " + paciente1.getEdad());
        System.out.println("Síntomas reportados: " + paciente1.getSintomasSeleccionados());
        System.out.println();
        
        System.out.println("=== Paciente creado ===");
        System.out.println("Nombre: " + paciente2.getNombre());
        System.out.println("Edad: " + paciente2.getEdad());
        System.out.println("Síntomas reportados: " + paciente2.getSintomasSeleccionados());
        System.out.println();

        // 3. Crear un diagnóstico para el paciente
        Diagnostico d = new Diagnostico(
                paciente1,
                gripe,
                gripe.getCategoria(),
                sintomasPaciente1,
                recomendacionesGripe
        );

        Diagnostico d2 = new Diagnostico(
                paciente2,
                hepatitis,
                gripe.getCategoria(),
                sintomasPaciente1,
                recomendacionesGripe
        );
        // Agregar el diagnóstico al historial del paciente
        paciente1.agregarDiagnostico(d);
        paciente1.agregarDiagnostico(d2);

        System.out.println("=== Diagnóstico generado ===");
        System.out.println(d);
        System.out.println();
        
        System.out.println("=== Diagnóstico generado ===");
        System.out.println(d2);
        System.out.println();

        // 4. Ver historial del paciente
        System.out.println("=== Historial del paciente ===");
        for (Diagnostico diag : paciente1.getHistorialDiagnosticos()) {
            System.out.println(diag);
        }

        // 5. Probar estadísticas
        EstadisticaEnfermedad est = new EstadisticaEnfermedad("Gripe", 1);
        est.incrementarApariciones();

        EstadisticaEnfermedad est2 = new EstadisticaEnfermedad("Hepatitis", 1);
        est.incrementarApariciones();
        
        System.out.println("\n=== Estadísticas ===");
        System.out.println(est);
        System.out.println(est2);

    }
}
