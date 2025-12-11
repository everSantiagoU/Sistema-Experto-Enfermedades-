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
     * siguiendo el estándar CSV.
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
     */
    public boolean generarCSV(List<Diagnostico> listaDiagnosticos, String rutaArchivo) throws IOException {
        if (listaDiagnosticos == null || listaDiagnosticos.isEmpty()) {
            System.err.println("Advertencia: La lista de diagnósticos está vacía. No se generará el archivo.");
            return false;
        }
        
        // Define el formato de la fecha
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        try (FileWriter fileWriter = new FileWriter(rutaArchivo);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {

            // 1. Escribir encabezados
            String header = "Paciente,Edad,Fecha Diagnostico,Enfermedad Diagnosticada,Categoria,Sintomas Reportados,Recomendaciones";
            printWriter.println(header);

            // 2. Escribir cada diagnóstico como una fila
            for (Diagnostico d : listaDiagnosticos) {
                // Formatear la fecha
                String fechaStr = (d.getFecha() != null) ? sdf.format(d.getFecha()) : "";
                
                // CORRECCIÓN AQUÍ: Usamos getSintomasEnfermedad() en lugar de getSintomasIngresados()
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
            System.err.println("Error al generar el archivo CSV en la ruta: " + rutaArchivo);
            e.printStackTrace();
            return false;
        }
    }
}