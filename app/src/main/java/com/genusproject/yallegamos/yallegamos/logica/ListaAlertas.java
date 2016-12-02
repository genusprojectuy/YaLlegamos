package com.genusproject.yallegamos.yallegamos.logica;

import android.content.Context;

import com.genusproject.yallegamos.yallegamos.entidades.Alerta;
import com.genusproject.yallegamos.yallegamos.persistencia.alertaTabla;
import com.genusproject.yallegamos.yallegamos.utiles.Utilidades;

import java.util.List;
import java.util.Observable;

import static com.genusproject.yallegamos.yallegamos.utiles.Constantes.ACTIVA;
import static com.genusproject.yallegamos.yallegamos.utiles.Constantes.PENDIENTE;

/**
 * Created by alvar on 01/12/2016.
 */

public class ListaAlertas extends Observable {
    private String TAG = this.getClass().getSimpleName().toUpperCase();
    private Context context;
    private alertaTabla alertaT;
    private List<Alerta> lstAlerta;
    private static ListaAlertas ourInstance;
    private Utilidades utilidades = Utilidades.getInstance();

    public static ListaAlertas getInstance(Context pContext) {
        if (ourInstance == null)
        {
            ourInstance = new ListaAlertas(pContext);
        }
        return ourInstance;
    }

    private ListaAlertas(Context pContext) {
        this.context = pContext;
        alertaT     = alertaTabla.getInstancia(context);
        lstAlerta   = alertaT.DevolverAlertas();
        utilidades.MostrarMensaje(TAG, "Cargando lista, cantidad: " + lstAlerta.size());

    }

    public List<Alerta> getLstAlerta() {
        return lstAlerta;
    }

    public long AddAlerta(Alerta alerta)
    {
        alerta.set_ID(alertaT.AgregarRegistro(alerta));
        lstAlerta.add(alerta);
        Notificar();

        return  alerta.get_ID();
    }

    public void ModAlerta(Alerta alerta, boolean notificar)
    {
        alertaT.Update(alerta);
        int posicion = 0;
        for(Alerta unaAlerta : lstAlerta)
        {
             if (unaAlerta.get_ID() == alerta.get_ID())
             {
                 posicion = lstAlerta.indexOf(unaAlerta);
                 break;
             }
        }
        lstAlerta.set(posicion, alerta);

        if(notificar)
        {
            Notificar();
        }
    }

    public void DelAlerta(Alerta alerta)
    {
        utilidades.MostrarMensaje(TAG, "Eliminando alerta: " + alerta.get_ID());
        alertaT.EliminarRegistro(alerta.get_ID());
        utilidades.MostrarMensaje(TAG, "Tamaño de lista antes de eliminar: " + lstAlerta.size());
        int posicion = 0;
        for(Alerta unaAlerta : lstAlerta)
        {
            if (unaAlerta.get_ID() == alerta.get_ID())
            {
                posicion = lstAlerta.indexOf(unaAlerta);
                break;
            }
        }

        lstAlerta.remove(posicion);

        utilidades.MostrarMensaje(TAG, "Tamaño de lista luego de eliminar: " + lstAlerta.size());
        Notificar();
    }


    private void Notificar(){
        setChanged();
        //Notificamos a los observadores y le enviamos el nuevo valor
        notifyObservers(lstAlerta);
    }

    public boolean ExistenAlertasActivas()
    {
        boolean AlertasActivas = false;
        for(Alerta unaAlerta : lstAlerta)
        {
            if (unaAlerta.getActiva().equals(ACTIVA))
            {
                AlertasActivas = true;
                break;
            }
        }
        return AlertasActivas;
    }

    public boolean ExistenAlertasActivasSinProcesar()
    {
        boolean AlertasActivas = false;
        for(Alerta unaAlerta : lstAlerta)
        {
            if (unaAlerta.getActiva().equals(ACTIVA) && unaAlerta.getEstado().equals(PENDIENTE))
            {
                AlertasActivas = true;
                break;
            }
        }
        return AlertasActivas;
    }

    public Alerta DevolverAlerta(long ID){
        return alertaT.DevolverUnRegistro(ID);
    }

}
