package ProyectoSistemaExperto.reports;

import ProyectoSistemaExperto.models.Diagnostico;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter; // <--- IMPORTANTE: Usamos esta librería
import java.util.List;

public class Reporte {

    /**
     * Función auxiliar para escapar strings (CSV standard).
     */
    private String escapeCSV(String value) {
        if (value == null) return "";
        if (value.contains("\"")) {
            value = value.replace("\"", "\"\"");
        }
        if (value.contains(",") || value.contains("\n")) {
             return "\"" + value + "\"";
        }
        return value;
    }

    /**
     * Genera un archivo CSV con el historial de diagnósticos.
     */
    public boolean generarCSV(List<Diagnostico> listaDiagnosticos, String rutaArchivo) throws IOException {
        if (listaDiagnosticos == null || listaDiagnosticos.isEmpty()) {
            System.err.println("Advertencia: La lista de diagnósticos está vacía.");
            return false;
        }
        
        // CORRECCIÓN: Usamos DateTimeFormatter para LocalDateTime
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
        try (FileWriter fileWriter = new FileWriter(rutaArchivo);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {

            // Encabezados
            String header = "Paciente,Edad,Fecha Diagnostico,Enfermedad Diagnosticada,Categoria,Sintomas,Recomendaciones";
            printWriter.println(header);

            for (Diagnostico d : listaDiagnosticos) {
                // CORRECCIÓN: Usamos el método .format() del objeto LocalDateTime
                String fechaStr = (d.getFecha() != null) ? d.getFecha().format(formatter) : "Sin Fecha";
                
                // Unimos las listas con un separador diferente a la coma
                String sintomasStr = String.join(" | ", d.getSintomasEnfermedad());
                String recomendacionesStr = String.join(" | ", d.getRecomendacionesDiagnostico());
                
                // Construir la línea CSV
                String linea = String.format("%s,%d,%s,%s,%s,%s,%s",
                    escapeCSV(d.getPaciente().getNombre()), 
                    d.getPaciente().getEdad(),
                    fechaStr,
                    escapeCSV(d.getNombreEnfermedad().getNombre()),
                    escapeCSV(d.getCategoriaEnfermedad()),
                    escapeCSV(sintomasStr),
                    escapeCSV(recomendacionesStr)
                );

                printWriter.println(linea);
            }

            System.out.println("Reporte CSV generado exitosamente en: " + rutaArchivo);
            return true;

        } catch (IOException e) {
            System.err.println("Error al generar el archivo CSV: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}