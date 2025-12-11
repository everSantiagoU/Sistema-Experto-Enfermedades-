package ProyectoSistemaExperto.reports;

import ProyectoSistemaExperto.models.Diagnostico;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.List;

public class Reporte {

    /**
     * Función auxiliar para escapar strings que contienen comas o comillas dobles,
     * siguiendo el estándar CSV (encerrar en comillas dobles y duplicar comillas internas).
     */
    private String escapeCSV(String value) {
        if (value == null) return "";
        
        // 1. Duplicar comillas internas para escapar
        if (value.contains("\"")) {
            value = value.replace("\"", "\"\"");
        }
        
        // 2. Encerrar en comillas dobles si contiene comas o saltos de línea
        if (value.contains(",") || value.contains("\n")) {
             return "\"" + value + "\"";
        }
        return value;
    }

    /**
     * Genera un archivo CSV con el historial de diagnósticos proporcionado.
     *
     * @param listaDiagnosticos Lista de objetos Diagnostico (usualmente el historial de un paciente).
     * @param rutaArchivo Ruta completa donde se guardará el archivo CSV (ej: "C:/reports/historial.csv").
     * @return true si la exportación fue exitosa, false en caso contrario.
     */
    public boolean generarCSV(List<Diagnostico> listaDiagnosticos, String rutaArchivo) throws IOException {
        if (listaDiagnosticos == null || listaDiagnosticos.isEmpty()) {
            System.err.println("Advertencia: La lista de diagnósticos está vacía. No se generará el archivo.");
            return false;
        }
        
        // Define el formato de la fecha
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        // Usar try-with-resources para asegurar el cierre de FileWriter y PrintWriter
        try (FileWriter fileWriter = new FileWriter(rutaArchivo); // ← CORREGIDO: se quitó el "new" extra
             PrintWriter printWriter = new PrintWriter(fileWriter)) {

            // 1. Escribir encabezados (Headers)
            String header = "Paciente,Edad,Fecha Diagnóstico,Enfermedad Diagnosticada,Categoría,Síntomas Reportados,Recomendaciones";
            printWriter.println(header);

            // 2. Escribir cada diagnóstico como una fila
            for (Diagnostico d : listaDiagnosticos) {
                // Formatear la fecha
                String fechaStr = sdf.format(d.getFecha());
                
                // Unir listas a strings para una celda CSV, separando por '|' para distinguirlo de la coma CSV
                String sintomasStr = String.join(" | ", d.getSintomasIngresados());
                String recomendacionesStr = String.join(" | ", d.getRecomendacionesDiagnostico());
                
                // Construir la línea, aplicando la función de escape a cualquier campo que pueda tener comas.
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
            System.err.println("Error al generar el archivo CSV en la ruta: " + rutaArchivo);
            e.printStackTrace();
            return false;
        }
    }
}