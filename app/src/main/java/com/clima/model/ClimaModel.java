package com.clima.model;

public class ClimaModel {

    private double temperatura;
    private double presion;
    private int humedad;
    private double minima;
    private double maxima;

    public ClimaModel(double temperatura, double presion, int humedad, double minima, double maxima) {
        this.temperatura = temperatura;
        this.presion = presion;
        this.humedad = humedad;
        this.minima = minima;
        this.maxima = maxima;
    }

    public double getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(double temperatura) {
        this.temperatura = temperatura;
    }

    public double getPresion() {
        return presion;
    }

    public void setPresion(double presion) {
        this.presion = presion;
    }

    public int getHumedad() {
        return humedad;
    }

    public void setHumedad(int humedad) {
        this.humedad = humedad;
    }

    public double getMinima() {
        return minima;
    }

    public void setMinima(double minima) {
        this.minima = minima;
    }

    public double getMaxima() {
        return maxima;
    }

    public void setMaxima(double maxima) {
        this.maxima = maxima;
    }
}
