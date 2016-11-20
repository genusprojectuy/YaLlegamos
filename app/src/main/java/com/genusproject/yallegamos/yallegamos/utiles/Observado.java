package com.genusproject.yallegamos.yallegamos.utiles;

import android.util.Log;

import java.util.Observable;

/**
 * Created by alvar on 30/10/2016.
 */

public class Observado extends Observable {

    private static Observado instancia;
    private boolean alarmasActivas;
    private String mensaje;

    private Observado(){
        mensaje = "Objeto Observado Iniciado";
        Log.e("SERVICIO C", mensaje);
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
        Log.e("SERVICIO C", "Chucha");
        alarmasActivas  = m;
        setChanged();
        //Notificamos a los observadores y le enviamos el nuevo valor
        notifyObservers(alarmasActivas);
        //notifyObservers(); Este metodo solo notifica que hubo cambios en el objeto
        Log.e("SERVICIO C", "ChuchaCHUCHA");

        int observadores = countObservers();
        Log.e("SERVICIO C", "ChuchaCHUCHA a" + observadores);


    }

}
