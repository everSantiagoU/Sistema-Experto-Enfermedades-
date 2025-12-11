package ProyectoSistemaExperto.models;

import java.util.ArrayList;
import java.util.List;

public class Paciente {
    private String nombre;
    private int edad;
    private List<String> sintomas;
    private List<Diagnostico> historialDiagnosticos;

    public Paciente(String nombre, int edad, List<String> sintomas) {
        this.nombre = nombre;
        this.edad = edad;
        this.sintomas = sintomas == null ? new ArrayList<>() : sintomas;
        this.historialDiagnosticos = new ArrayList<>();
    }

    // Getters y Setters
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

    public List<String> getSintomas() {
        return sintomas;
    }

    public void setSintomas(List<String> sintomas) {
        this.sintomas = sintomas;
    }

    public List<Diagnostico> getHistorialDiagnosticos() {
        return historialDiagnosticos;
    }

    public void setHistorialDiagnosticos(List<Diagnostico> historialDiagnosticos) {
        this.historialDiagnosticos = historialDiagnosticos;
    }

    public void agregarDiagnostico(Diagnostico d) {
        this.historialDiagnosticos.add(d);
    }

    @Override
    public String toString() {
        return "Paciente{" +
                "nombre='" + nombre + '\'' +
                ", edad=" + edad +
                ", sintomas=" + sintomas +
                '}';
    }
}

