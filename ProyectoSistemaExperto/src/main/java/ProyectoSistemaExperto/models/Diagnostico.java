package ProyectoSistemaExperto.models;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.time.LocalDateTime;
import java.util.List;
/**
 *
 * @author EVER URIBE
 */
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
