package ProyectoSistemaExperto.models;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/* PROJECT: Sistema Experto
 * MODULE: Models
 * 
 * CLASS: EstadisticaEnfermedad
 * DESCRIPTION: Statistical data model class that represents disease frequency and occurrence metrics
 *              within the expert system, Tracks and analyzes how often specific diseases are diagnosed
 *              providing valuable insights for an analysis
 * 
 * - Dashboard statistics display
 * - Disease prevalence tracking
 * - System usage analytics
 * - Report generation for healthcare professionals
 * 
 * 
 * - Frequency distribution of diagnosed diseases
 * - Trend identification for disease outbreaks
 * - Performance metrics for diagnostic accuracy
 * - Resource allocation planning
 * 
 * 
 * - Disease name for identification
 * - Occurrence count for statistical analysis
 * - Methods for data manipulation and incrementation
 * 
 * @author Ever Santiago Uribe
 * @version 1.0.0
 * @since 2025-12-11
 * @lastModified 2025-12-11
*/


public class EstadisticaEnfermedad {
    private String nombreEnfermedad;
    private int cantidadApariciones;

    public EstadisticaEnfermedad(String nombreEnfermedad, int cantidad) {
        this.nombreEnfermedad = nombreEnfermedad;
        this.cantidadApariciones = cantidad;
    }

    public String getNombreEnfermedad() {
        return nombreEnfermedad;
    }

    public int getCantidad() {
        return cantidadApariciones;
    }

    public void setCantidadApariciones(int nuevaCantidad) {
        this.cantidadApariciones = nuevaCantidad;
    }

    public void incrementarApariciones() {
        this.cantidadApariciones++;
    }

    @Override
    public String toString() {
        return "EstadisticaEnfermedad{" +
                "nombre='" + nombreEnfermedad + '\'' +
                ", apariciones=" + cantidadApariciones +
                '}';
    }
}
