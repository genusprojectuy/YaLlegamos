package com.genusproject.yallegamos.yallegamos.utiles;

import android.util.Log;

import com.genusproject.yallegamos.yallegamos.entidades.Alerta;

import java.util.List;
import java.util.Observable;

/**
 * Created by alvar on 30/10/2016.
 */

public class Observado extends Observable {

    private static Observado instancia;
    private boolean alarmasActivas;
    private String TAG = this.getClass().getSimpleName().toUpperCase();
    private Utilidades utilidades = Utilidades.getInstance();

    private Observado(){
        utilidades.MostrarMensaje(TAG, "Iniciando");
    }

    public static Observado getInstancia()
    {
        if (instancia==null)
        {
            instancia = new Observado();
        }
        return instancia;
    }

    public void setAlarmasActivas(boolean m){
        utilidades.MostrarMensaje(TAG, "Cambio el valor: " + m);
        alarmasActivas  = m;
        setChanged();
        //Notificamos a los observadores y le enviamos el nuevo valor
        notifyObservers(alarmasActivas);

    }



}
