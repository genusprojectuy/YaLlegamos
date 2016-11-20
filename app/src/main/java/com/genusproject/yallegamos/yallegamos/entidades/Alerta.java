package com.genusproject.yallegamos.yallegamos.entidades;

/**
 * Created by alvar on 29/09/2016.
 */

public class Alerta {
    private long     _ID;
    private String  latitud;
    private String  longitud;
    private String  activa;
    private String  direccion;
    private int     rango;
    private String  estado;
    private float   distancia;

    public Alerta() {
    }

    public long get_ID() {
        return _ID;
    }

    public void set_ID(long _ID) {
        this._ID = _ID;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getActiva() {
        return activa;
    }

    public void setActiva(String activa) {
        this.activa = activa;
    }

    public int getRango() {
        return rango;
    }

    public void setRango(int rango) {
        this.rango = rango;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public float getDistancia() {
        return distancia;
    }

    public void setDistancia(float distancia) {
        this.distancia = distancia;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Alerta{" +
                "_ID=" + _ID +
                ", latitud='" + latitud + '\'' +
                ", longitud='" + longitud + '\'' +
                ", activa='" + activa + '\'' +
                ", direccion='" + direccion + '\'' +
                ", estado='" + estado + '\'' +
                ", distancia='" + distancia + '\'' +
                ", rango=" + rango +
                '}';
    }
}
