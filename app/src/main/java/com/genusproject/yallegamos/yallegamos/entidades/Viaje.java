package com.genusproject.yallegamos.yallegamos.entidades;

import com.genusproject.yallegamos.yallegamos.enumerados.EstadoViaje;
import com.google.android.gms.maps.model.LatLng;

import java.util.Date;
import java.util.List;

/**
 * Created by alvar on 04/12/2016.
 */

public class Viaje {
    private long     _ID;
    private Date    fecha;
    private EstadoViaje estado;
    private List<LatLng> recorrido;
    private List<Alerta> alertas;




    public Viaje() {
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

    public List<LatLng> getRecorrido() {
        return recorrido;
    }

    public void setRecorrido(List<LatLng> recorrido) {
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

    @Override
    public String toString() {
        return "Viaje{" +
                "_ID=" + _ID +
                ", fecha=" + fecha +
                ", estado=" + estado +
                ", recorrido=" + recorrido +
                ", alertas=" + alertas +
                '}';
    }
}
