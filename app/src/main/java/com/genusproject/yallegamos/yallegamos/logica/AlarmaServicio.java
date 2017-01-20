package com.genusproject.yallegamos.yallegamos.logica;

import android.Manifest;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;

import com.genusproject.yallegamos.yallegamos.R;
import com.genusproject.yallegamos.yallegamos.entidades.Alerta;
import com.genusproject.yallegamos.yallegamos.entidades.Viaje;
import com.genusproject.yallegamos.yallegamos.enumerados.EstadoViaje;
import com.genusproject.yallegamos.yallegamos.ui.Mapa;
import com.genusproject.yallegamos.yallegamos.utiles.Utilidades;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import static com.genusproject.yallegamos.yallegamos.utiles.Constantes.COLOR_ALERTA;
import static com.genusproject.yallegamos.yallegamos.utiles.Constantes.COLOR_AREA_ALERTA;
import static com.genusproject.yallegamos.yallegamos.utiles.Constantes.LED_FIN;
import static com.genusproject.yallegamos.yallegamos.utiles.Constantes.LED_INICIO;
import static com.genusproject.yallegamos.yallegamos.utiles.Constantes.MAPA_PRESISION_MINIMA;
import static com.genusproject.yallegamos.yallegamos.utiles.Constantes.SI;
import static com.genusproject.yallegamos.yallegamos.utiles.Constantes.DOS_DECIMALES;
import static com.genusproject.yallegamos.yallegamos.utiles.Constantes.FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS;
import static com.genusproject.yallegamos.yallegamos.utiles.Constantes.NOTIFICACION_DESTINO_ID;
import static com.genusproject.yallegamos.yallegamos.utiles.Constantes.NOTIFICACION_ID;
import static com.genusproject.yallegamos.yallegamos.utiles.Constantes.NOTIFICAR;
import static com.genusproject.yallegamos.yallegamos.utiles.Constantes.PENDIENTE;
import static com.genusproject.yallegamos.yallegamos.utiles.Constantes.PROCESADA;
import static com.genusproject.yallegamos.yallegamos.utiles.Constantes.UPDATE_INTERVAL_IN_MILLISECONDS;
import static com.genusproject.yallegamos.yallegamos.utiles.Constantes.VIBRAR_PAT;


/**
 * Created by alvar on 08/10/2016.
 */

public class AlarmaServicio extends IntentService{

    private String TAG              = this.getClass().getSimpleName().toUpperCase();
    private List<Alerta> lstAlerta;
    private Utilidades utilidades = Utilidades.getInstance();
    private Observado_ListaAlertas observadoListaAlertas;
    private  ListaAlertas c_ListaAlerta;
    private NotificationManager mNotificationManager;


    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public AlarmaServicio(String name) {
        super(name);
    }

    public AlarmaServicio() {
        this(AlarmaServicio.class.getName());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        utilidades.MostrarMensaje(TAG, "Destruyendo");
        QuitarNotificacion();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        // If we get killed, after returning from here, restart
        //return START_STICKY;
        utilidades.MostrarMensaje(TAG, "Iniciando servicio");
        observadoListaAlertas = Observado_ListaAlertas.getInstance(this.getApplicationContext());

        utilidades  = Utilidades.getInstance();
        lstAlerta   = observadoListaAlertas.getLstAlerta();

        if (lstAlerta.size() > 0)
        {
            MyLocationListener myLocationListener = new MyLocationListener();
            myLocationListener.IniciarEscuchaUbicacion();
        }
        else
        {
            onDestroy();
        }
        return START_NOT_STICKY;
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        // Gets data from the incoming Intent
        //String dataString = workIntent.getDataString();
        //...
        // Do work here, based on the contents of dataString
        //...

    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    public void QuitarNotificacion(){

        if(mNotificationManager != null)
        {
            Viaje viaje = observadoListaAlertas.DevolverViaje();
            if(viaje.getEstado().equals(EstadoViaje.EN_PROCESO))
            {
                viaje.setEstado(EstadoViaje.CANCELADO);
                observadoListaAlertas.ModViaje(viaje);
            }
            mNotificationManager.cancel(NOTIFICACION_ID);
        }

    }

    public void stopServicio(){
        utilidades.MostrarMensaje(TAG, "Finalizando servicio");
        try {
            finalize();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public class MyLocationListener implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, Observer {
        private LocationRequest mLocationRequest;
        private Location mCurrentLocation;
        private GoogleApiClient client;
        private NotificationCompat.Builder mNotifyBuilder;

        public MyLocationListener(){
            observadoListaAlertas.addObserver(this);

            observadoListaAlertas.SetServicioActivo(true);

            mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            ArmarNotificacion();
            buildGoogleApiClient();
            client.connect();


        }

        public void IniciarEscuchaUbicacion()
        {
            utilidades.MostrarMensaje(TAG, "Iniciando escucha de ubicacion");
        }

        @Override
        public void update(Observable o, Object arg) {
            c_ListaAlerta   = (ListaAlertas) arg;
            lstAlerta       = c_ListaAlerta.getLstAlerta();

            boolean var = observadoListaAlertas.ExistenAlertasActivasSinProcesar();
            if(observadoListaAlertas.ServicioActivo() && !var )
            {
                observadoListaAlertas.SetServicioActivo(false);
            }

            if(!var || !observadoListaAlertas.ServicioActivo())
            {
                QuitarNotificacion();
                FinalizarServicio();
            }

        }

        @Override
        public void onConnected(@Nullable Bundle bundle) {
            if (mCurrentLocation == null) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mCurrentLocation    = LocationServices.FusedLocationApi.getLastLocation(client);

            }

            startLocationUpdates();

        }

        @Override
        public void onConnectionSuspended(int i) {
            client.connect();
        }

        @Override
        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        }

        @Override
        public void onLocationChanged(Location location) {
            utilidades.MostrarMensaje(TAG, "Presición de la ubicación en metros: " + String.valueOf(location.getAccuracy()));
            mCurrentLocation    = location;

            if(location.getAccuracy() <= MAPA_PRESISION_MINIMA) {
                verificarAlertas();
            }

        }


        private synchronized void buildGoogleApiClient() {
            client = new GoogleApiClient.Builder(getApplicationContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .addApi(AppIndex.API)
                    .build();
            createLocationRequest();

        }

        protected void createLocationRequest() {

            mLocationRequest = new LocationRequest();

            mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
            mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        }

        protected void startLocationUpdates() {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            LocationServices.FusedLocationApi.requestLocationUpdates(client, mLocationRequest, this);
        }

        protected void stopLocationUpdates() {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            if(client.isConnected())
            {
                LocationServices.FusedLocationApi.removeLocationUpdates(client, this);// requestLocationUpdates(client, mLocationRequest, this);
            }
        }

        public void verificarAlertas(){
            Viaje viaje = observadoListaAlertas.DevolverViaje();

            if(observadoListaAlertas.ExistenAlertasActivasSinProcesar())
            {
                LatLng origen = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                observadoListaAlertas.ViajeAgregarLatLang(viaje, origen);

                if(lstAlerta != null) {
                    if (lstAlerta.size() > 0) {

                        for (Alerta alerta : lstAlerta) {
                            if (alerta.getActiva().equals(SI) && alerta.getEstado().equals(PENDIENTE)) {
                                alerta = CalcularDistancia(alerta);
                                if (alerta.getDistancia() <= utilidades.ToMetro(alerta.getRango())) {
                                    Notificar(alerta);
                                }
                            }
                        }


                        Collections.sort(lstAlerta, new Comparator<Alerta>(){
                            public int compare(Alerta o1, Alerta o2){
                                if(o1.getDistancia() == o2.getDistancia())
                                    return 0;
                                return o1.getDistancia() < o2.getDistancia() ? -1 : 1;
                            }
                        });

                        if(observadoListaAlertas.ExistenAlertasActivasSinProcesar())
                        {
                            ActualizarNotificacion();
                        }
                        else
                        {
                            viaje.setEstado(EstadoViaje.FINALIZADO);
                            observadoListaAlertas.ModViaje(viaje);

                            QuitarNotificacion();
                        }

                    }
                }
            }
            else
            {
                viaje.setEstado(EstadoViaje.FINALIZADO);
                observadoListaAlertas.ModViaje(viaje);
                observadoListaAlertas.SetServicioActivo(false);
            }
        }

        public void FinalizarServicio(){
            stopLocationUpdates();
            client.disconnect();
            stopServicio();
        }


        public Alerta CalcularDistancia(Alerta alerta){

            LatLng origen = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
            LatLng destino = new LatLng(Double.valueOf(alerta.getLatitud()), Double.valueOf(alerta.getLongitud()));

            alerta.setDistancia(utilidades.DistanceTo(origen, destino));
            observadoListaAlertas.ModAlerta(alerta, NOTIFICAR);

            return alerta;
        }

        public void Notificar(Alerta alerta){
            alerta.setEstado(PROCESADA);
            observadoListaAlertas.ModAlerta(alerta, NOTIFICAR);

            NotificationCompat.Builder mNot_Llegada = new NotificationCompat.Builder(getApplicationContext())
                    .setContentTitle("Ya llegamos!")
                    .setContentText("Estas llegando a: " + alerta.getDireccion())
                    .setColor(COLOR_ALERTA)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setSmallIcon(R.mipmap.ic_notificacion);

            Intent notIntent            = new Intent(getApplicationContext(), Mapa.class);
            PendingIntent contIntent    = PendingIntent.getActivity(getApplicationContext(), 0, notIntent, 0);

            mNot_Llegada.setContentIntent(contIntent);

            if(observadoListaAlertas.DevolverConfiguracion().getVibracionActiva().equals(SI))
            {
                long[] patron = VIBRAR_PAT;
                mNot_Llegada.setVibrate(patron);

            }

            if(observadoListaAlertas.DevolverConfiguracion().getSonidoActivo().equals(SI) && observadoListaAlertas.DevolverConfiguracion().getSonido() != null)
            {
                mNot_Llegada.setSound(observadoListaAlertas.DevolverConfiguracion().getSonido());
            }

            mNot_Llegada.setLights(getColor(R.color.colorCeleste), LED_INICIO, LED_FIN);

            mNot_Llegada.setAutoCancel(true);

            Notification notification = mNot_Llegada.build();
            notification.flags |= Notification.FLAG_INSISTENT | Notification.FLAG_SHOW_LIGHTS;
            mNotificationManager.notify(NOTIFICACION_DESTINO_ID, notification);

            utilidades.MostrarMensaje(TAG, "Notificando llegada a destino");

        }

        public void ArmarNotificacion(){
            utilidades.MostrarMensaje(TAG, "Armar notificacion");
            // Sets an ID for the notification, so it can be updated
            mNotifyBuilder = new NotificationCompat.Builder(getApplicationContext())
                    .setContentTitle("Iniciando viaje")
                    .setContentText("Buscando ubicación precisa.")
                    .setSmallIcon(R.mipmap.ic_notificacion);

            Intent notIntent            = new Intent(getApplicationContext(), Mapa.class);
            PendingIntent contIntent    = PendingIntent.getActivity(getApplicationContext(), 0, notIntent, 0);

            mNotifyBuilder.setContentIntent(contIntent);
            mNotifyBuilder.setOngoing(true);
            mNotifyBuilder.setColor(COLOR_ALERTA);
            mNotifyBuilder.setPriority(Notification.PRIORITY_HIGH);
            mNotificationManager.notify(NOTIFICACION_ID, mNotifyBuilder.build());

        }

        public void ActualizarNotificacion(){
            utilidades.MostrarMensaje(TAG, "Actualizar notificacion");
            if(lstAlerta != null)
            {
                if(lstAlerta.size() > 0 )
                {
                    for(Alerta alerta : lstAlerta)
                    {
                        if (alerta.getActiva().equals(SI) && alerta.getEstado().equals(PENDIENTE)) {

                            mNotifyBuilder.setContentTitle("Distancia del punto mas cercano")
                                    .setContentText(alerta.getDireccion() + ": " + utilidades.Km_Mt(alerta.getDistancia()));

                            mNotificationManager.notify(
                                    NOTIFICACION_ID,
                                    mNotifyBuilder.build());
                            break;
                        }

                    }


                }
            }

        }



    }


}
