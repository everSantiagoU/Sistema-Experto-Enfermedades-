package ProyectoSistemaExperto.reports;

/* PROJECT: Sistema Experto
 * MODULE: Reports
 * 
 * CLASS: Reporte
 * DESCRIPTION: Reporting utility class that generates structured data exports in CSV format.
 *              Provides functionality to convert diagnosis data into a comma separated file
 *              value files for analysis sharing and archival purposes. Implements proper CSV
 *              formatting including escaping and delimiters for medical data compatibility
 * 
 * EXPORT 
 * - CSV file generation with proper formatting
 * - Automatic escaping of special characters
 * - Structured medical data presentation
 * - Support for multiline fields and lists
 * 
 * 
 * - Converts LocalDateTime to readable timestamps
 * - Joins list data with appropriate separators
 * - Handles null values and empty datasets
 * - Ensures CSV standard compliance
 * 
 * 
 * - Medical record export for external systems
 * - Statistical analysis in spreadsheet software
 * - Audit trail creation
 * - Backup and archival of diagnosis data
 * 
 * @author Jhojan Villada
 * @version 1.0.0
 * @since 2025-12-11
 * @lastModified 2025-12-11
 * @compatibility CSV 
*/

import ProyectoSistemaExperto.models.Diagnostico;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter; //-Important
import java.util.List;

public class Reporte {

    // Auxiliar Function for spaces escapes
    
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

    // CSV with th ehistorial diagnosis
    public boolean generarCSV(List<Diagnostico> listaDiagnosticos, String rutaArchivo) throws IOException {
        if (listaDiagnosticos == null || listaDiagnosticos.isEmpty()) {
            System.err.println("Advertencia: La lista de diagnósticos está vacía.");
            return false;
        }
        
        // CORRECTION: UsE DateTimeFormatter for the LocalDateTime
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
        try (FileWriter fileWriter = new FileWriter(rutaArchivo);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {

            // Header
            String header = "Paciente,Edad,Fecha Diagnostico,Enfermedad Diagnosticada,Categoria,Sintomas,Recomendaciones";
            printWriter.println(header);

            for (Diagnostico d : listaDiagnosticos) {
                // Correction: i use method .format() from the object LocalDateTime
                String fechaStr = (d.getFecha() != null) ? d.getFecha().format(formatter) : "Sin Fecha";
                
                // join the list with a separator comma
                String sintomasStr = String.join(" | ", d.getSintomasEnfermedad());
                String recomendacionesStr = String.join(" | ", d.getRecomendacionesDiagnostico());
                
                // made line csv
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