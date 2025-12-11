package ProyectoSistemaExperto.models;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author EVER URIBE
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
