package ProyectoSistemaExperto.models;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/* PROJECT: Sistema Experto
 * MODULE: Models
 * 
 * CLASS: Enfermedad
 * DESCRIPTION: Entity class that represents a medical disease or condition within the expert system
 *              Contains complete medical information including symptoms category and treatment recommendations
 *              Serves as a knowledge base entity that is loaded from MySQL and used by the inference engine
 *              for diagnostic reasoning and patient assessment
 * 
 * 
 * - Maps to 'enfermedad' table in MySQL database
 * - Primary key: id_enfermedad
 * - Foreign relationships: Linked to diagnostico and sintoma tables
 * 
 *
 * - Symptoms are used for pattern matching in Prolog rules
 * - Categories enable disease classification and filtering
 * - Recommendations provide treatment guidance
 * 
 * 
 * - Unique identifier for database operations
 * - Disease name for identification and display
 * - Medical category for classification
 * - Symptom list for diagnostic matching
 * - Recommendation list for treatment planning
 * 
 * @author Ever Santiago Uribe
 * @version 1.4.0
 * @since 2025-12-09
 * @lastModified 2025-12-11
*/

import java.util.List;
import java.util.ArrayList;

public class Enfermedad {
    private int idEnfermedad;
    private String nombre;
    private String categoria;
    private List<String> sintomas;
    private List<String> recomendaciones;
    
    // Empty Constructor (Necessary for DiagnosticoDAO)
    public Enfermedad() {
        this.sintomas = new ArrayList<>();
        this.recomendaciones = new ArrayList<>();
    }

    // Constructor for parameters (necessary for the EnfermedadDAO stay alive)
    public Enfermedad(String nombre, List<String> sintomas, String categoria, List<String> recomendaciones) {
        this.nombre = nombre;
        this.sintomas = sintomas;
        this.categoria = categoria;
        this.recomendaciones = recomendaciones;
    }
    
    // Getters y Setters
    public int getIdEnfermedad() { return idEnfermedad; }
    public void setIdEnfermedad(int idEnfermedad) { this.idEnfermedad = idEnfermedad; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    
    public List<String> getSintomas() { return sintomas; }
    public void setSintomas(List<String> sintomas) { this.sintomas = sintomas; }
    
    public List<String> getRecomendaciones() { return recomendaciones; }
    public void setRecomendaciones(List<String> recomendaciones) { this.recomendaciones = recomendaciones; }
    
    @Override
    public String toString() {
        return "Enfermedad{" +
                "id=" + idEnfermedad +
                ", nombre='" + nombre + '\'' +
                ", categoria='" + categoria + '\'' +
                ", sintomas=" + sintomas +
                ", recomendaciones=" + recomendaciones +
                '}';
    }
}
