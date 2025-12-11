package ProyectoSistemaExperto.models;

import java.util.List;
import java.util.ArrayList;

public class Enfermedad {
    private int idEnfermedad;
    private String nombre;
    private String categoria;
    private List<String> sintomas;
    private List<String> recomendaciones;
    
    // 1. Constructor vacío (NECESARIO para DiagnosticoDAO y buenas prácticas)
    public Enfermedad() {
        this.sintomas = new ArrayList<>();
        this.recomendaciones = new ArrayList<>();
    }

    // 2. Constructor con parámetros (NECESARIO para que EnfermedadDAO siga funcionando)
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
