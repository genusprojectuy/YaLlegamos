package com.genusproject.yallegamos.yallegamos.entidades;

import android.net.Uri;

/**
 * Created by alvar on 28/12/2016.
 */

public class Configuracion {
    private long _ID;
    private Uri sonido;
    private String SonidoActivo;
    private String VibracionActiva;

    public Configuracion()
    {

    }

    public Uri getSonido() {
        return sonido;
    }

    public void setSonido(Uri sonido) {
        this.sonido = sonido;
    }

    public long get_ID() {
        return _ID;
    }

    public void set_ID(long _ID) {
        this._ID = _ID;
    }

    public String getSonidoActivo() {
        return SonidoActivo;
    }

    public void setSonidoActivo(String sonidoActivo) {
        SonidoActivo = sonidoActivo;
    }

    public String getVibracionActiva() {
        return VibracionActiva;
    }

    public void setVibracionActiva(String vibracionActiva) {
        VibracionActiva = vibracionActiva;
    }

    @Override
    public String toString() {
        return "Configuracion{" +
                "_ID=" + _ID +
                ", sonido=" + sonido +
                ", SonidoActivo='" + SonidoActivo + '\'' +
                ", VibracionActiva='" + VibracionActiva + '\'' +
                '}';
    }
}
