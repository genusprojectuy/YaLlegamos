package com.genusproject.yallegamos.yallegamos.logica;

import android.content.Context;
import android.media.RingtoneManager;

import com.genusproject.yallegamos.yallegamos.entidades.Alerta;
import com.genusproject.yallegamos.yallegamos.entidades.Configuracion;
import com.genusproject.yallegamos.yallegamos.entidades.Viaje;
import com.genusproject.yallegamos.yallegamos.entidades.ViajeRecorrido;
import com.genusproject.yallegamos.yallegamos.enumerados.TipoDireccion;
import com.genusproject.yallegamos.yallegamos.persistencia.PersistenciaBD;
import com.genusproject.yallegamos.yallegamos.utiles.Utilidades;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Observable;

import static com.genusproject.yallegamos.yallegamos.utiles.Constantes.NO;
import static com.genusproject.yallegamos.yallegamos.utiles.Constantes.SI;
import static com.genusproject.yallegamos.yallegamos.utiles.Constantes.PENDIENTE;

/**
 * Created by alvar on 01/12/2016.
 */

public class Observado_ListaAlertas extends Observable {
    private String TAG = this.getClass().getSimpleName().toUpperCase();
    private Context context;
    private PersistenciaBD alertaT;
    private ListaAlertas lstAlerta;
    private static Observado_ListaAlertas ourInstance;
    private Utilidades utilidades = Utilidades.getInstance();
    private Viaje v;
    private List<Viaje> lstViajes;
    private Configuracion configuracion;


    public static Observado_ListaAlertas getInstance(Context pContext) {
        if (ourInstance == null)
        {
            ourInstance = new Observado_ListaAlertas(pContext);
        }
        return ourInstance;
    }

    private Observado_ListaAlertas(Context pContext) {
        this.context = pContext;
        alertaT     = PersistenciaBD.getInstancia(context);
        lstAlerta   = ListaAlertas.getInstance();
        lstAlerta.setLstAlerta(alertaT.AlertaDevolverLista());
        lstViajes = alertaT.ViajeDevolverLista();
        this.BuscarAlertasActivas();
        this.BuscarAlertasActivasSinProcesar();
        utilidades.MostrarMensaje(TAG, "Cargando lista, cantidad: " + lstAlerta.getLstAlerta().size());

    }

    public List<Alerta> getLstAlerta() {
        return lstAlerta.getLstAlerta();
    }

    public long AddAlerta(Alerta alerta){
        alerta.set_ID(alertaT.AlertaAgregar(alerta));
        lstAlerta.getLstAlerta().add(alerta);
        Notificar();

        return  alerta.get_ID();
    }

    public void ModAlerta(Alerta alerta, boolean notificar){
        alertaT.AlertaModificar(alerta);
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

    public void DelAlerta(Alerta alerta){
        alertaT.AlertaEliminar(alerta.get_ID());
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

    private void BuscarAlertasActivas(){
        boolean AlertasActivas = false;
        for(Alerta unaAlerta : lstAlerta.getLstAlerta())
        {
            if (unaAlerta.getActiva().equals(SI))
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

    private void BuscarAlertasActivasSinProcesar(){
        boolean AlertasActivas = false;
        for(Alerta unaAlerta : lstAlerta.getLstAlerta())
        {
            if (unaAlerta.getActiva().equals(SI) && unaAlerta.getEstado().equals(PENDIENTE))
            {
                AlertasActivas = true;
                break;
            }
        }

        lstAlerta.setExistenAlertasActivasSinProcesar(AlertasActivas);
    }

    public boolean ExistenAlertasActivasSinProcesar(){
        return lstAlerta.isExistenAlertasActivasSinProcesar();
    }

    public Alerta DevolverAlerta(long ID){
        return alertaT.AlertaDevolver(ID);
    }

    public void SetServicioActivo(boolean activo){
        lstAlerta.setServicioActivo(activo);
        Notificar();
    }

    public boolean ServicioActivo(){
        return lstAlerta.isServicioActivo();
    }

    //-----------------------------------------------------------------------------------------------------------------------
    //VIAJE
    //-----------------------------------------------------------------------------------------------------------------------
    public long AddViaje(Viaje viaje){
        viaje.set_ID(alertaT.ViajeAgregar(viaje));
        viaje.setRecorrido(new ArrayList<ViajeRecorrido>());
        this.v = viaje;
        lstViajes.add(viaje);
        return  viaje.get_ID();
    }

    public void ModViaje(Viaje viaje){

        utilidades.MostrarMensaje(TAG, "Modificando viaje " + viaje.toString());
        alertaT.ViajeModificar(viaje);

        int posicion = 0;
        for(Viaje unViaje : lstViajes)
        {
            if (unViaje.get_ID() == viaje.get_ID())
            {
                posicion = lstViajes.indexOf(unViaje);
                break;
            }
        }

        lstViajes.set(posicion, viaje);

    }

    public void DelViaje(Viaje viaje){

        alertaT.ViajeEliminar(viaje.get_ID());

        int posicion = 0;
        for(Viaje unViaje : lstViajes)
        {
            if (unViaje.get_ID() == viaje.get_ID())
            {
                posicion = lstViajes.indexOf(unViaje);
                break;
            }
        }

        lstViajes.remove(posicion);

    }

    public void DelTodosViajes(){

        for(Viaje v : lstViajes)
        {
            alertaT.ViajeEliminar(v.get_ID());
        }

        lstViajes.clear();

    }

    public Viaje ViajeDevolver(long _ID){

        Viaje retViaje = new Viaje();

        for(Viaje unViaje : lstViajes)
        {
            if (unViaje.get_ID() == _ID)
            {
                retViaje = unViaje;
                break;
            }
        }

        return ViajeCargarDatosDinamicos(retViaje);
    }

    public List<Viaje> ViajeDevolverLista(){

        for (ListIterator<Viaje> i = lstViajes.listIterator(); i.hasNext();) {
            Viaje element = i.next();
            i.set(this.ViajeCargarDatosDinamicos(element));
        }

        Collections.sort(lstViajes, new Comparator<Viaje>(){
            public int compare(Viaje o1, Viaje o2){
                if(o1.getFecha().compareTo(o2.getFecha()) == 0)
                    return 0;
                return o1.getFecha().compareTo(o2.getFecha()) > 0  ? -1 : 1;
            }
        });

        return lstViajes;
    }

    private Viaje ViajeCargarDatosDinamicos(Viaje viaje){

        if(!viaje.getRecorrido().isEmpty())
        {
            viaje.setOrigen(viaje.getRecorrido().get(0).getLatitud_longitud());
            viaje.setH_origen(viaje.getRecorrido().get(0).getFecha());

            viaje.setDestino(viaje.getRecorrido().get(viaje.getRecorrido().size() - 1).getLatitud_longitud());
            viaje.setH_destino(viaje.getRecorrido().get(viaje.getRecorrido().size() - 1).getFecha());

            viaje.setDireccion_origen(utilidades.DevolverDirecciones(this.context, viaje.getOrigen(), TipoDireccion.DIRECCION));
            viaje.setDireccion_destino(utilidades.DevolverDirecciones(this.context, viaje.getDestino(), TipoDireccion.DIRECCION));


            float distancia             = 0;
            ViajeRecorrido c_anterior   = null;
            ViajeRecorrido c_distancia  = null;
            long duracion               = 0;


            for(ViajeRecorrido vr : viaje.getRecorrido())
            {

                /*DISTANCIA*/
                if(c_distancia != null){
                    /*Realizo esto, para que no sume variaciones de distancia inferior a 100 metros, esto lo hago para evitar que sume cada coordenada que puede surgir por falla de presision*/
                    float auxDistancia = utilidades.DistanceTo(c_distancia.getLatitud_longitud(), vr.getLatitud_longitud());
                    if(auxDistancia >= 100)
                    {
                        distancia += auxDistancia;
                        c_distancia = vr;
                    }
                }
                else{
                    c_distancia = vr;
                }

                /*DURACION*/
                if(c_anterior != null)
                {
                    duracion += (vr.getFecha().getTime() - c_anterior.getFecha().getTime());
                }

                c_anterior = vr;

            }


            viaje.setDistanciaRecorrida(distancia);
            viaje.setDuracion(duracion);
        }


        return viaje;
    }

    public Viaje ViajeAgregarLatLang(Viaje viaje, LatLng latLng){

        alertaT.ViajeAgregarLatLang(viaje.get_ID(), latLng);
        ViajeRecorrido vr = new ViajeRecorrido();
        vr.setLatitud_longitud(latLng);
        vr.setFecha(new Date());

        viaje.getRecorrido().add(vr);
        v.getRecorrido().add(vr);

        int posicion = 0;
        for(Viaje unViaje : lstViajes)
        {
            if (unViaje.get_ID() == viaje.get_ID())
            {
                posicion = lstViajes.indexOf(unViaje);
                break;
            }
        }

        lstViajes.set(posicion, viaje);

        return viaje;
    }

    public Viaje DevolverViaje(){
        return v;
    }

    //----------------------------------------------------------------------
    //CONFIGURACION
    //----------------------------------------------------------------------
    public Configuracion DevolverConfiguracion(){

        if(this.configuracion == null) {

            Configuracion cfg = alertaT.ConfiguracionDevolver(1);

            if (cfg.get_ID() != 1) {


                cfg = new Configuracion();

                cfg.setSonido(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));
                cfg.setSonidoActivo(SI);
                cfg.setVibracionActiva(SI);

                cfg.set_ID(alertaT.ConfiguracionAgregar(cfg));



            }

            this.configuracion = cfg;
        }
        return  configuracion;

    }

    public void ModificarConfiguracion(Configuracion pConfiguracion){
        alertaT.ConfiguracionModificar(pConfiguracion);
        this.configuracion = pConfiguracion;
    }


}
