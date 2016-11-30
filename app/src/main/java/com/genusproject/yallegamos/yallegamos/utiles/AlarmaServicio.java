package com.genusproject.yallegamos.yallegamos.utiles;

import android.Manifest;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.genusproject.yallegamos.yallegamos.R;
import com.genusproject.yallegamos.yallegamos.entidades.Alerta;
import com.genusproject.yallegamos.yallegamos.persistencia.alertaTabla;
import com.genusproject.yallegamos.yallegamos.ui.Mapa;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.data.DataBufferObserver;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import static com.genusproject.yallegamos.yallegamos.utiles.Constantes.FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS;
import static com.genusproject.yallegamos.yallegamos.utiles.Constantes.NOTIFICACION_DESTINO_ID;
import static com.genusproject.yallegamos.yallegamos.utiles.Constantes.NOTIFICACION_ID;
import static com.genusproject.yallegamos.yallegamos.utiles.Constantes.PENDIENTE;
import static com.genusproject.yallegamos.yallegamos.utiles.Constantes.PROCESADA;
import static com.genusproject.yallegamos.yallegamos.utiles.Constantes.UPDATE_INTERVAL_IN_MILLISECONDS;
import static com.genusproject.yallegamos.yallegamos.utiles.Constantes.VIBRAR_LONG;
import static com.genusproject.yallegamos.yallegamos.utiles.Constantes.VIBRAR_PAT;


/**
 * Created by alvar on 08/10/2016.
 */

public class AlarmaServicio extends IntentService{

    private String TAG              = this.getClass().getSimpleName().toUpperCase();
    private boolean FaltanAlertas   = true;
    private alertaTabla alertaT;
    private List<Alerta> lstAlerta;
    private Utilidades utilidades = Utilidades.getInstance();
    private Observado ob = Observado.getInstancia();
    private LocationManager locationManager;


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

    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        // Gets data from the incoming Intent
        //String dataString = workIntent.getDataString();
        //...
        // Do work here, based on the contents of dataString
        //...

        utilidades.MostrarMensaje(TAG, "Iniciando servicio");

        ob.setAlarmasActivas(true);


        utilidades  = Utilidades.getInstance();
        alertaT     = alertaTabla.getInstancia(this.getApplicationContext());
        lstAlerta   = alertaT.DevolverAlertas();

        if (lstAlerta.size() > 0)
        {
            MyLocationListener myLocationListener = new MyLocationListener();
            myLocationListener.IniciarEscuchaUbicacion();
        }
        else
        {
            onDestroy();
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
        private DecimalFormat df                                = new DecimalFormat("###.##");
        private LocationRequest mLocationRequest;
        private Location mCurrentLocation;
        private GoogleApiClient client;
        private NotificationManager mNotificationManager;
        private NotificationCompat.Builder mNotifyBuilder;

        public MyLocationListener(){
            ob.addObserver(this);

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
            boolean var = (boolean) arg;
            if (!var) {
                FaltanAlertas = false;
                QuitarNotificacion();
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
            mCurrentLocation    = location;
            verificarAlertas();
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
            LocationServices.FusedLocationApi.removeLocationUpdates(client, this);// requestLocationUpdates(client, mLocationRequest, this);
        }

        public void verificarAlertas(){
            lstAlerta   = alertaT.DevolverAlertas();

            if(FaltanAlertas)
            {
                if(lstAlerta != null) {
                    if (lstAlerta.size() > 0) {


                        lstAlerta = alertaT.DevolverAlertas();
                        boolean auxFaltanAlertas = false;
                        for (Alerta alerta : lstAlerta) {
                            if (alerta.getActiva().equals("SI") && alerta.getEstado().equals(PENDIENTE)) {
                                alerta = CalcularDistancia(alerta);
                                if (alerta.getDistancia() <= utilidades.ToMetro(alerta.getRango())) {
                                    Notificar(alerta);
                                } else {
                                    auxFaltanAlertas = true;
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

                        if(auxFaltanAlertas)
                        {
                            ActualizarNotificacion();
                        }
                        else
                        {
                            ob.setAlarmasActivas(false);
                            QuitarNotificacion();
                        }

                        FaltanAlertas = auxFaltanAlertas;
                    }
                }
            }
            else
            {
                FaltanAlertas = false;
                FinalizarServicio();
            }
        }

        public void FinalizarServicio(){
            stopServicio();
            LocationServices.FusedLocationApi.removeLocationUpdates(client, this);
            client.disconnect();

        }

        public Alerta CalcularDistancia(Alerta alerta){

            LatLng origen = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
            LatLng destino = new LatLng(Double.valueOf(alerta.getLatitud()), Double.valueOf(alerta.getLongitud()));

            alerta.setDistancia(utilidades.DistanceTo(origen, destino));
            alertaT.Update(alerta);

            return alerta;
        }

        public void Notificar(Alerta alerta){
            alerta.setEstado(PROCESADA);
            alertaT.Update(alerta);

            NotificationCompat.Builder mNot_Llegada = new NotificationCompat.Builder(getApplicationContext())
                    .setContentTitle("Ya llegamos!")
                    .setContentText("Estas llegando a: " + alerta.getDireccion())
                    .setSmallIcon(R.mipmap.ic_notificacion);

            Intent notIntent            = new Intent(getApplicationContext(), Mapa.class);
            PendingIntent contIntent    = PendingIntent.getActivity(getApplicationContext(), 0, notIntent, 0);

            mNot_Llegada.setContentIntent(contIntent);

            long[] patron = VIBRAR_PAT;
            mNot_Llegada.setVibrate(patron);

            mNotificationManager.notify(NOTIFICACION_DESTINO_ID, mNot_Llegada.build());

            //utilidades.VibrarAlarma(VIBRAR_PAT, AlarmaServicio.this);
            utilidades.MostrarMensaje(TAG, "Notificando llegada a destino");

        }

        public void ArmarNotificacion(){
            utilidades.MostrarMensaje(TAG, "Armar notificacion");
            // Sets an ID for the notification, so it can be updated
            mNotifyBuilder = new NotificationCompat.Builder(getApplicationContext())
                    .setContentTitle("Iniciando viaje")
                    .setContentText("Buscando el destino más proximo.")
                    .setSmallIcon(R.mipmap.ic_notificacion);

            Intent notIntent            = new Intent(getApplicationContext(), Mapa.class);
            PendingIntent contIntent    = PendingIntent.getActivity(getApplicationContext(), 0, notIntent, 0);

            mNotifyBuilder.setContentIntent(contIntent);
            mNotifyBuilder.setOngoing(true);
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
                        if (alerta.getActiva().equals("SI") && alerta.getEstado().equals(PENDIENTE)) {

                            mNotifyBuilder.setContentTitle("Distancia del punto mas cercano")
                                    .setContentText("Distancia: " + df.format(alerta.getDistancia()));

                            mNotificationManager.notify(
                                    NOTIFICACION_ID,
                                    mNotifyBuilder.build());
                            break;
                        }

                    }


                }
            }

        }

        public void QuitarNotificacion(){
            mNotificationManager.cancel(NOTIFICACION_ID);
            FaltanAlertas = false;

            stopLocationUpdates();

        }

    }


}
