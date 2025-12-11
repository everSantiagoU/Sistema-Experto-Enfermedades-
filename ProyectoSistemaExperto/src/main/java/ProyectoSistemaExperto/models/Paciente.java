package ProyectoSistemaExperto.models;

import java.util.ArrayList;
import java.util.List;

/* PROJECT: Sistema Experto
 * MODULE: Models
 * 
 * CLASS: Paciente
 * DESCRIPTION: Core entity class representing a patient within the medical expert system
 *              Contains complete patient information including demographics symptoms and diagnosic history
 *              Serves as the central entity for patient management linking symptoms diagnoses
 *              and treatment recommendations in a comprehensive medical record
 * 
 * 
 * - Maps to paciente table in MySQL database
 * - Primary key: id_paciente auto-increment
 * - Foreign relationships: Linked to diagnostico table for medical history
 * 
 * 
 * - Maintains current symptoms for diagnosis
 * - Stores complete diagnostic history
 * - Supports longitudinal patient tracking
 * - Enables trend analysis and follow-up care
 * 
 * 
 * - Unique identifier for database operations
 * - Patient name and age for demographic information
 * - Current symptom list for diagnostic evaluation
 * - Historical diagnoses for medical history tracking
 * 
 * @author Ever Santiago Uribe
 * @version 1.1.0
 * @since 2025-12-11
 * @lastModified 2025-12-11
*/

public class Paciente {
    private int idPaciente; // <--- NUEVO CAMPO
    private String nombre;
    private int edad;
    private List<String> sintomasSeleccionados;
    private List<Diagnostico> historialDiagnosticos;

    public Paciente(String nombre, int edad, List<String> sintomas) {
        this.nombre = nombre;
        this.edad = edad;
        this.sintomasSeleccionados = sintomas;
        this.historialDiagnosticos = new ArrayList<>();
    }

    // === GETTERS Y SETTERS PARA EL ID ===
    public int getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(int idPaciente) {
        this.idPaciente = idPaciente;
    }
    // ====================================

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public List<String> getSintomasSeleccionados() {
        return sintomasSeleccionados;
    }

    public void setSintomasSeleccionados(List<String> sintomas) {
        this.sintomasSeleccionados = sintomas;
    }

    public void agregarDiagnostico(Diagnostico d) {
        historialDiagnosticos.add(d);
    }

    public List<Diagnostico> getHistorialDiagnosticos() {
        return historialDiagnosticos;
    }
}
