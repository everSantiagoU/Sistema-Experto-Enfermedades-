package ProyectoSistemaExperto.inferencia;

import ProyectoSistemaExperto.DAO.EnfermedadDAO;
import ProyectoSistemaExperto.DAO.PacienteDAO; // <--- IMPORTANTE: Importar el nuevo DAO
import ProyectoSistemaExperto.DAO.ConexionDB;
import ProyectoSistemaExperto.models.Enfermedad;
import ProyectoSistemaExperto.models.Paciente; // <--- Importar modelo Paciente

import java.util.Arrays;
import java.util.List;

public class MainPruebaMI {
    public static void main(String[] args) {

        System.out.println("===========================================");
        System.out.println("  PRUEBA INTEGRAL: DAO + MotorInferencia   ");
        System.out.println("===========================================\n");

        try {
            // ... (Código anterior 1 al 4) ...
            // 1. Inicializar motor
            MotorInferencia motor = new MotorInferencia();
            
            // 2. Verificar conexión
            ConexionDB.testConnection();
            EnfermedadDAO dao = new EnfermedadDAO();
            List<Enfermedad> lista = dao.obtenerEnfermedades();
            System.out.println("✓ Enfermedades obtenidas desde MySQL: " + lista.size());

            // 3. Limpiar y 4. Cargar (código anterior)
            motor.limpiarBD();
            System.out.println("\nCargando enfermedades en Prolog...");
            for (Enfermedad e : lista) {
                motor.agregarEnfermedad(e);
            }
            System.out.println("\n✓ Todas las enfermedades cargadas.\n");

            // ... (Pruebas de Inferencia 1 a 5, déjalas tal cual) ...
            
            // ===========================================================
            // 6. PRUEBA DE PACIENTE DAO (NUEVO)
            // ===========================================================
            System.out.println("\n=== PRUEBA 6: Registro de Paciente (MySQL) ===");
            PacienteDAO pacienteDAO = new PacienteDAO();
            
            // Creamos un paciente de prueba (usamos System.currentTimeMillis para nombre unico si quieres, o fijo)
            String nombrePrueba = "Paciente Test " + (System.currentTimeMillis() % 1000);
            Paciente nuevoPaciente = new Paciente(nombrePrueba, 25, Arrays.asList("tos", "fiebre"));
            
            System.out.println("Intentando registrar: " + nombrePrueba);
            int idGenerado = pacienteDAO.registrar(nuevoPaciente);
            
            if (idGenerado != -1) {
                System.out.println("✓ Paciente registrado con éxito. ID generado: " + idGenerado);
                System.out.println("  (ID en objeto Java actualizado: " + nuevoPaciente.getIdPaciente() + ")");
                
                // Verificar recuperando desde BD
                Paciente recuperado = pacienteDAO.obtenerPorId(idGenerado);
                if (recuperado != null) {
                    System.out.println("✓ Paciente recuperado desde BD: " + recuperado.getNombre() + ", Edad: " + recuperado.getEdad());
                } else {
                    System.err.println("✗ Error: No se pudo recuperar el paciente recién creado.");
                }
            } else {
                System.err.println("✗ Error: Falló el registro del paciente.");
            }

            System.out.println("\n===========================================");
            System.out.println("        TODAS LAS PRUEBAS COMPLETADAS        ");
            System.out.println("===========================================\n");

        } catch (Exception e) {
            System.err.println("✗ ERROR durante pruebas: " + e.getMessage());
            e.printStackTrace();
        }
    }
}