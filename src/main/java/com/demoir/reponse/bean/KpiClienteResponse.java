package com.demoir.reponse.bean;

public class KpiClienteResponse {

    private double promedioEdad;
    private double desviacionEstandar;

    public double getPromedioEdad() {
        return promedioEdad;
    }

    public void setPromedioEdad(double promedioEdad) {
        this.promedioEdad = promedioEdad;
    }

    public double getDesviacionEstandar() {
        return desviacionEstandar;
    }

    public void setDesviacionEstandar(double desviacionEstandar) {
        this.desviacionEstandar = desviacionEstandar;
    }

    @Override
    public String toString() {
        return "KpiClienteResponse{" +
                "promedioEdad=" + promedioEdad +
                ", desviacionEstandar=" + desviacionEstandar +
                '}';
    }
}
