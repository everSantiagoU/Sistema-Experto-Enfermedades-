package ProyectoSistemaExperto.models;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author EVER URIBE
 */
public class Paciente {
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
