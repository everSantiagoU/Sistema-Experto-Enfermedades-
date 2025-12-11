package ProyectoSistemaExperto.models;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/* PROJECT: Sistema Experto
 * MODULE: Models
 * 
 * CLASS: Diagnostico
 * DESCRIPTION: Entity class that represents a medical diagnosis within the expert system.
 *              Encapsulates all relevant information about a patient's diagnosis including
 *              the diagnosed disease patient details symptoms and recommendations.
 *              Serves as the core data transfer object between the inference engine
 *              database layer and presentation layer
 * 
 * 
 * - Associated with one Paciente one-to-one
 * - Linked to one Enfermedad one-to-many via symptoms
 * - Contains multiple symptoms and recommendations
 * 
 * 
 * - Created by MotorInferencia after diagnosis process
 * - Stored in database via DiagnosticoDAO
 * - Displayed in UI for medical professionals
 * 
 *
 * - Patient information and demographics
 * - Diagnosed disease with category
 * - Timestamp of diagnosis
 * - Specific symptoms presented
 * - Medical recommendations for treatment
 * 
 * @author Ever Santiago Uribe
 * @version 2.1.0
 * @since 2025-12-08
 * @lastModified 2025-12-11
*/

import java.time.LocalDateTime;
import java.util.List;


   // Represents a medical diagnosis
   // This class stores comprehensive information about a patients diagnosis
   // including the disease identified symptoms observed and treatment recommendations
   // It acts as a complete record of a diagnostic event within the system


public class Diagnostico {
    private Paciente paciente;
    private Enfermedad nombreEnfermedad;
    private String categoriaEnfermedad;
    private LocalDateTime fecha;
    private List<String> sintomasEnfermedad;
    private List<String> recomendacionesDiagnostico;

    public Diagnostico(
            Paciente paciente,
            Enfermedad enfermedad,
            String categoria,
            List<String> sintomasIngresados,
            List<String> recomendaciones
    ) {
        this.paciente = paciente;
        this.nombreEnfermedad = enfermedad;
        this.categoriaEnfermedad = categoria;
        this.fecha = LocalDateTime.now();
        this.sintomasEnfermedad = sintomasIngresados;
        this.recomendacionesDiagnostico = recomendaciones;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public Enfermedad getNombreEnfermedad() {
        return nombreEnfermedad;
    }

    public String getCategoriaEnfermedad() {
        return categoriaEnfermedad;
    }

    public List<String> getSintomasEnfermedad() {
        return sintomasEnfermedad;
    }

    public List<String> getRecomendacionesDiagnostico() {
        return recomendacionesDiagnostico;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }
    
    public void setFecha(java.time.LocalDateTime fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return "Diagnostico{" +
                "paciente=" + paciente.getNombre() +
                ", enfermedad=" + nombreEnfermedad.getNombre() +
                ", categoria='" + categoriaEnfermedad + '\'' +
                ", fecha=" + fecha +
                ", sintomas=" + sintomasEnfermedad +
                ", recomendaciones=" + recomendacionesDiagnostico +
                '}';
    }
}
