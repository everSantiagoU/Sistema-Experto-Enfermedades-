package ProyectoSistemaExperto.models;
import java.util.List;
/**
 *
 * @author EVER URIBE
 */
public class Enfermedad {
    private String nombre;
    private List<String> sintomas;
    private String categoria;
    private List<String> recomendaciones;

    public Enfermedad(String nombre, List<String> sintomas, String categoria, List<String> recomendaciones) {
        this.nombre = nombre;
        this.sintomas = sintomas;
        this.categoria = categoria;
        this.recomendaciones = recomendaciones;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<String> getSintomas() {
        return sintomas;
    }

    public void setSintomas(List<String> sintomas) {
        this.sintomas = sintomas;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public List<String> getRecomendaciones() {
        return recomendaciones;
    }

    public void setRecomendaciones(List<String> recomendaciones) {
        this.recomendaciones = recomendaciones;
    }

    @Override
    public String toString() {
        return "Enfermedad{" +
                "nombre='" + nombre + '\'' +
                ", sintomas=" + sintomas +
                ", categoria='" + categoria + '\'' +
                ", recomendaciones=" + recomendaciones +
                '}';
    }
    
}
