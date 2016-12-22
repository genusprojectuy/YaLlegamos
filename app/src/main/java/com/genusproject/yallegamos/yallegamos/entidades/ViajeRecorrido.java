package com.genusproject.yallegamos.yallegamos.entidades;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

/**
 * Created by alvar on 18/12/2016.
 */

public class ViajeRecorrido {
    private LatLng latitud_longitud;
    private Date fecha;


    public ViajeRecorrido() {
    }

    public LatLng getLatitud_longitud() {
        return latitud_longitud;
    }

    public void setLatitud_longitud(LatLng latitud_longitud) {
        this.latitud_longitud = latitud_longitud;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return "ViajeRecorrido{" +
                "latitud_longitud=" + latitud_longitud +
                ", fecha=" + fecha +
                '}';
    }
}
