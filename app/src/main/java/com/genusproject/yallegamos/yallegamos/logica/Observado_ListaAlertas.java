package com.genusproject.yallegamos.yallegamos.logica;

import android.content.Context;
import android.view.View;

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

public class Observado_ListaAlertas extends Observable {
    private String TAG = this.getClass().getSimpleName().toUpperCase();
    private Context context;
    private alertaTabla alertaT;
    private ListaAlertas lstAlerta;
    private static Observado_ListaAlertas ourInstance;
    private Utilidades utilidades = Utilidades.getInstance();

    public static Observado_ListaAlertas getInstance(Context pContext) {
        if (ourInstance == null)
        {
            ourInstance = new Observado_ListaAlertas(pContext);
        }
        return ourInstance;
    }

    private Observado_ListaAlertas(Context pContext) {
        this.context = pContext;
        alertaT     = alertaTabla.getInstancia(context);
        lstAlerta   = ListaAlertas.getInstance();
        lstAlerta.setLstAlerta(alertaT.DevolverAlertas());
        this.BuscarAlertasActivas();
        this.BuscarAlertasActivasSinProcesar();
        utilidades.MostrarMensaje(TAG, "Cargando lista, cantidad: " + lstAlerta.getLstAlerta().size());

    }

    public List<Alerta> getLstAlerta() {
        return lstAlerta.getLstAlerta();
    }

    public long AddAlerta(Alerta alerta)
    {
        alerta.set_ID(alertaT.AgregarRegistro(alerta));
        lstAlerta.getLstAlerta().add(alerta);
        Notificar();

        return  alerta.get_ID();
    }

    public void ModAlerta(Alerta alerta, boolean notificar)
    {
        alertaT.Update(alerta);
        int posicion = 0;
        for(Alerta unaAlerta : lstAlerta.getLstAlerta())
        {
             if (unaAlerta.get_ID() == alerta.get_ID())
             {
                 posicion = lstAlerta.getLstAlerta().indexOf(unaAlerta);
                 break;
             }
        }

        lstAlerta.getLstAlerta().set(posicion, alerta);

        if(notificar)
        {
            Notificar();
        }
    }

    public void DelAlerta(Alerta alerta)
    {
        alertaT.EliminarRegistro(alerta.get_ID());
        int posicion = 0;
        for(Alerta unaAlerta : lstAlerta.getLstAlerta())
        {
            if (unaAlerta.get_ID() == alerta.get_ID())
            {
                posicion = lstAlerta.getLstAlerta().indexOf(unaAlerta);
                break;
            }
        }

        lstAlerta.getLstAlerta().remove(posicion);

        Notificar();
    }


    private void Notificar(){

        this.BuscarAlertasActivas();
        this.BuscarAlertasActivasSinProcesar();

        setChanged();
        //Notificamos a los observadores y le enviamos el nuevo valor
        notifyObservers(lstAlerta);
    }

    private void BuscarAlertasActivas()
    {
        boolean AlertasActivas = false;
        for(Alerta unaAlerta : lstAlerta.getLstAlerta())
        {
            if (unaAlerta.getActiva().equals(ACTIVA))
            {
                AlertasActivas = true;
                break;
            }
        }
        lstAlerta.setExistenAlertasActivas(AlertasActivas);
    }

    public boolean ExistenAlertasActivas()
    {
        return lstAlerta.isExistenAlertasActivas();
    }

    private void BuscarAlertasActivasSinProcesar()
    {
        boolean AlertasActivas = false;
        for(Alerta unaAlerta : lstAlerta.getLstAlerta())
        {
            if (unaAlerta.getActiva().equals(ACTIVA) && unaAlerta.getEstado().equals(PENDIENTE))
            {
                AlertasActivas = true;
                break;
            }
        }

        lstAlerta.setExistenAlertasActivasSinProcesar(AlertasActivas);
    }

    public boolean ExistenAlertasActivasSinProcesar()
    {
        return lstAlerta.isExistenAlertasActivasSinProcesar();
    }

    public Alerta DevolverAlerta(long ID){
        return alertaT.DevolverUnRegistro(ID);
    }

    public void SetServicioActivo(boolean activo){
        lstAlerta.setServicioActivo(activo);
        Notificar();
    }

    public boolean ServicioActivo(){
        return lstAlerta.isServicioActivo();
    }


}
