package com.genusproject.yallegamos.yallegamos.entidades;

import com.genusproject.yallegamos.yallegamos.enumerados.EstadoViaje;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by alvar on 04/12/2016.
 */

public class Viaje {
    private long     _ID;
    private Date    fecha;
    private EstadoViaje estado;
    private List<ViajeRecorrido> recorrido;
    private List<Alerta> alertas;

    private LatLng origen;
    private String direccion_origen;
    private Date h_origen;
    private LatLng destino;
    private String direccion_destino;
    private Date h_destino;
    private float distanciaRecorrida;
    private long duracion;




    public Viaje() {
        recorrido = new ArrayList<ViajeRecorrido>();
    }

    public long get_ID() {
        return _ID;
    }

    public void set_ID(long _ID) {
        this._ID = _ID;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public List<ViajeRecorrido> getRecorrido() {
        return recorrido;
    }

    public void setRecorrido(List<ViajeRecorrido> recorrido) {
        this.recorrido = recorrido;
    }

    public EstadoViaje getEstado() {
        return estado;
    }

    public void setEstado(EstadoViaje estado) {
        this.estado = estado;
    }

    public List<Alerta> getAlertas() {
        return alertas;
    }

    public void setAlertas(List<Alerta> alertas) {
        this.alertas = alertas;
    }

    public LatLng getOrigen() {
        return origen;
    }

    public void setOrigen(LatLng origen) {
        this.origen = origen;
    }

    public Date getH_origen() {
        return h_origen;
    }

    public void setH_origen(Date h_origen) {
        this.h_origen = h_origen;
    }

    public LatLng getDestino() {
        return destino;
    }

    public void setDestino(LatLng destino) {
        this.destino = destino;
    }

    public Date getH_destino() {
        return h_destino;
    }

    public void setH_destino(Date h_destino) {
        this.h_destino = h_destino;
    }

    public String getDireccion_origen() {
        return direccion_origen;
    }

    public void setDireccion_origen(String direccion_origen) {
        this.direccion_origen = direccion_origen;
    }

    public String getDireccion_destino() {
        return direccion_destino;
    }

    public void setDireccion_destino(String direccion_destino) {
        this.direccion_destino = direccion_destino;
    }

    public float getDistanciaRecorrida() {
        return distanciaRecorrida;
    }

    public void setDistanciaRecorrida(float distanciaRecorrida) {
        this.distanciaRecorrida = distanciaRecorrida;
    }

    public long getDuracion() {
        return duracion;
    }

    public void setDuracion(long duracion) {
        this.duracion = duracion;
    }

    @Override
    public String toString() {
        return "Viaje{" +
                "_ID=" + _ID +
                ", fecha=" + fecha +
                ", estado=" + estado +
                ", recorrido=" + recorrido +
                ", alertas=" + alertas +
                ", origen=" + origen +
                ", direccion_origen='" + direccion_origen + '\'' +
                ", h_origen=" + h_origen +
                ", destino=" + destino +
                ", direccion_destino='" + direccion_destino + '\'' +
                ", h_destino=" + h_destino +
                '}';
    }
}
