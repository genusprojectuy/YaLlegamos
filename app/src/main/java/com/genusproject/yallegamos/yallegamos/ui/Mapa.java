package com.genusproject.yallegamos.yallegamos.ui;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.genusproject.yallegamos.yallegamos.R;
import com.genusproject.yallegamos.yallegamos.adaptadores.DrawerListAdapter;
import com.genusproject.yallegamos.yallegamos.entidades.Alerta;
import com.genusproject.yallegamos.yallegamos.entidades.Viaje;
import com.genusproject.yallegamos.yallegamos.entidades.ViajeRecorrido;
import com.genusproject.yallegamos.yallegamos.enumerados.EstadoViaje;
import com.genusproject.yallegamos.yallegamos.logica.ListaAlertas;
import com.genusproject.yallegamos.yallegamos.logica.Observado_ListaAlertas;
import com.genusproject.yallegamos.yallegamos.logica.AlarmaServicio;
import com.genusproject.yallegamos.yallegamos.enumerados.TipoDireccion;
import com.genusproject.yallegamos.yallegamos.utiles.Utilidades;
import com.genusproject.yallegamos.yallegamos.utiles.c_Circulo;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.genusproject.yallegamos.yallegamos.utiles.Constantes.MAPA_LINEA_COLOR;
import static com.genusproject.yallegamos.yallegamos.utiles.Constantes.MAPA_LINEA_WIDTH;
import static com.genusproject.yallegamos.yallegamos.utiles.Constantes.SI;
import static com.genusproject.yallegamos.yallegamos.utiles.Constantes.BORDER_OPACIDAD;
import static com.genusproject.yallegamos.yallegamos.utiles.Constantes.BORDER_WIDTH;
import static com.genusproject.yallegamos.yallegamos.utiles.Constantes.COLOR_AREA_ALERTA;
import static com.genusproject.yallegamos.yallegamos.utiles.Constantes.FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS;
import static com.genusproject.yallegamos.yallegamos.utiles.Constantes.MAPA_PADDING;
import static com.genusproject.yallegamos.yallegamos.utiles.Constantes.MAPA_ZOOM;
import static com.genusproject.yallegamos.yallegamos.utiles.Constantes.NOTIFICAR;
import static com.genusproject.yallegamos.yallegamos.utiles.Constantes.PENDIENTE;
import static com.genusproject.yallegamos.yallegamos.utiles.Constantes.PROCESADA;
import static com.genusproject.yallegamos.yallegamos.utiles.Constantes.RANGO_MAXIMO;
import static com.genusproject.yallegamos.yallegamos.utiles.Constantes.RANGO_STANDAR;
import static com.genusproject.yallegamos.yallegamos.utiles.Constantes.UPDATE_INTERVAL_IN_MILLISECONDS;
import static com.genusproject.yallegamos.yallegamos.utiles.Constantes.SIN_NOTIFICAR;

import java.util.Observable;
import java.util.Observer;

import static com.genusproject.yallegamos.yallegamos.utiles.Constantes.VIBRAR_LONG;

public class Mapa extends FragmentActivity implements GoogleMap.OnInfoWindowClickListener, OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, Observer {

    private GoogleMap mMap;
    private ListView drawerList;
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;
    private Marker myLocationMarker;
    private String TAG = this.getClass().getSimpleName().toUpperCase();
    private String LOCATION_KEY = "location-key";
    private String LAST_UPDATED_TIME_STRING_KEY = "last-updated-time-string-key";
    private LocationRequest mLocationRequest;
    private Location mCurrentLocation;
    private String mLastUpdateTime;
    private List<Alerta> lstAlerta;
    private boolean ubicacionEncontrada;
    private boolean alarmasActivas;
    private LatLngBounds.Builder cameraBuilder;
    private ArrayList<Marker> lstMarcadores;
    private ArrayList<c_Circulo> lstCirculos;
    private Boolean MenuAbierto = false;
    private CameraUpdate cu;
    private EditText etSearch;
    private Utilidades utilidades;
    private Button btnIniciarViaje;
    private Observado_ListaAlertas o_Observado_ListaAlertas;
    private  ListaAlertas c_ListaAlerta;
    private Polyline polyline;
    private boolean primerArranque;
    private boolean primerServicio;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapa_matias);

        utilidades = Utilidades.getInstance();

        ComprobarPermisos();

        //-----------------------------------------------------------------------------------------------
        //CARGANDO MAPA
        //-----------------------------------------------------------------------------------------------

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //-----------------------------------------------------------------------------------------------
        //CARGAR OBJETOS
        //-----------------------------------------------------------------------------------------------
        //etSearch = (EditText) findViewById(R.id.et_search);
        btnIniciarViaje = (Button) findViewById(R.id.btn_IniciarViaje);
        //RelativeLayout btnSearch = (RelativeLayout) findViewById(R.id.mapa_btnSearch);
        RelativeLayout boton    = (RelativeLayout) findViewById(R.id.btn_DrawOpen);
        ImageView img_boton     = (ImageView) findViewById(R.id.img_DrawOpen);

        primerArranque = true;



        //-----------------------------------------------------------------------------------------------
        //SETEAR EVENTOS
        //-----------------------------------------------------------------------------------------------
        /*CAJA DE BUSQUEDA
        etSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etSearch.setCursorVisible(true);
            }
        });*/
        /*BOTON DE BUSQUEDA
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BuscarDireccion();
            }
        });*/
        /*BOTON DE INICIAR VIAJE*/
        btnIniciarViaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               /*

                AnimatorSet anim = (AnimatorSet) AnimatorInflater.loadAnimator(Mapa.this, R.animator.flip);

                anim.setTarget(btnIniciarViaje);
                anim.setDuration(BOTON_DURACION);

                anim.start();
*/


                ManejarServicio();
                MoverCamaraTodosMarcadores();
            }
        });
        /*BOTON DE MENU LATERAL*/
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
        img_boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        //-----------------------------------------------------------------------------------------------

        o_Observado_ListaAlertas = Observado_ListaAlertas.getInstance(this);
        o_Observado_ListaAlertas.addObserver(this);
        lstAlerta = o_Observado_ListaAlertas.getLstAlerta();
        utilidades.MostrarMensaje(TAG, "Cargando lista, cantidad: " + lstAlerta.size());

        buildGoogleApiClient();
        updateValuesFromBundle(savedInstanceState);
        ArmarMenu();
        this.ManejarBoton();
        ArmarBusquedas();

    }

    //-----------------------------------------------------------------------------------------------
    //OVERRIDE BASICOS
    //-----------------------------------------------------------------------------------------------

    @Override
    public void onStart() {
        super.onStart();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (client.isConnected()) {
            startLocationUpdates();
            //o_Observado_ListaAlertas.addObserver(this);
        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop location updates to save battery, but don't disconnect the GoogleApiClient object.
        if (client.isConnected()) {
            stopLocationUpdates();
          //  o_Observado_ListaAlertas.deleteObserver(this);
        }

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sincronizar el estado del drawer
        drawerToggle.syncState();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Cambiar las configuraciones del drawer si hubo modificaciones
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        // If the initial location was never previously requested, we use
        // FusedLocationApi.getLastLocation() to get it. If it was previously requested, we store
        // its value in the Bundle and check for it in onCreate(). We
        // do not request it again unless the user specifically requests location updates by pressing
        // the Start Updates button.
        //
        // Because we cache the value of the initial location in the Bundle, it means that if the
        // user launches the activity,
        // moves to a new location, and then changes the device orientation, the original location
        // is displayed as the activity is re-created.
        if (mCurrentLocation == null) {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(client);
            mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());

            CambioUbicacion();
        }

        startLocationUpdates();

    }

    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        utilidades.MostrarMensaje(TAG, "Conección con el cliente de google suspendida");
        client.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        utilidades.MostrarMensaje(TAG, "Fallo la coneccion con el cliente: " + result.getErrorCode());
    }

    private Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Mapa Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {

            if (savedInstanceState.keySet().contains(LOCATION_KEY)) {
                mCurrentLocation = savedInstanceState.getParcelable(LOCATION_KEY);
            }

            if (savedInstanceState.keySet().contains(LAST_UPDATED_TIME_STRING_KEY)) {
                mLastUpdateTime = savedInstanceState.getString(LAST_UPDATED_TIME_STRING_KEY);
            }

            CambioUbicacion();
        }
    }

    private void ComprobarPermisos() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            ActivityCompat.requestPermissions(Mapa.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            ActivityCompat.requestPermissions(Mapa.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

            utilidades.MostrarMensaje(TAG, "Sin permiso para ver ubicación");

            return;
        }
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelable(LOCATION_KEY, mCurrentLocation);
        savedInstanceState.putString(LAST_UPDATED_TIME_STRING_KEY, mLastUpdateTime);
        super.onSaveInstanceState(savedInstanceState);
    }
    //<<<<<------------------------------------------------------------------------------------------

    //-----------------------------------------------------------------------------------------------
    //OTROS OVERRIDE
    //-----------------------------------------------------------------------------------------------
    @Override
    public void onBackPressed() {
        if (MenuAbierto && drawerLayout != null) {
            drawerLayout.closeDrawers();
        } else {
            finish();
        }
    }

    @Override
    public void onInfoWindowClick(final Marker marker) {
        MostrarInfoView(marker);
    }
    //<<<<<------------------------------------------------------------------------------------------

    //-----------------------------------------------------------------------------------------------
    //MAPA
    //-----------------------------------------------------------------------------------------------
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.getUiSettings().setMapToolbarEnabled(false);

        CargarInfoViewAdapter();
        mMap.clear();
        mMap.setOnInfoWindowClickListener(this);

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng arg0) {
                utilidades.Vibrar(VIBRAR_LONG, getApplicationContext());
                CrearAlerta(arg0);
            }
        });

    }

    public void CargarInfoViewAdapter() {

        //--------------------------------------------------------------------------------------------------
        //-InfoView de Marcadores
        //--------------------------------------------------------------------------------------------------
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            // Use default InfoWindow frame
            @Override
            public View getInfoContents(Marker arg0) {
                return null;
            }

            // Defines the contents of the InfoWindow
            @Override
            public View getInfoWindow(Marker arg0) {
                View v;
                Object valor = arg0.getTag();

                if (!valor.equals("MyLocation")) {

                    v = getLayoutInflater().inflate(R.layout.windowlayout_llegar, null);

                    Alerta alerta = o_Observado_ListaAlertas.DevolverAlerta((Long) valor);

                        TextView tvAddress = (TextView) v.findViewById(R.id.txt_address);
                        TextView tvRango = (TextView) v.findViewById(R.id.txt_Alarma_Rango);
                        TextView tvDist = (TextView) v.findViewById(R.id.txtDistRestante);

                        LatLng origen = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                        LatLng destino = new LatLng(Double.valueOf(alerta.getLatitud()), Double.valueOf(alerta.getLongitud()));

                        tvDist.setText(utilidades.Km_Mt(utilidades.DistanceTo(origen, destino)));
                        tvAddress.setText(utilidades.DevolverDirecciones(Mapa.this, destino, TipoDireccion.DIRECCION));
                        tvRango.setText(utilidades.ObtenerTextoRango(alerta.getRango()));

                } else {
                    v = getLayoutInflater().inflate(R.layout.windowlayout, null);
                }

                return v;
            }
        });
    }

    public void MostrarInfoView(final Marker marker){
        Object valor    = marker.getTag();

        if (!valor.equals("MyLocation")) {
            MostrarAlertaEditable(valor);

        }
    }

    public void MoverCamaraTodosMarcadores(){

        if (o_Observado_ListaAlertas.ExistenAlertasActivas()) {
            cu = CameraUpdateFactory.newLatLngBounds(cameraBuilder.build(), MAPA_PADDING);
            mMap.moveCamera(cu);
            mMap.animateCamera(cu);
        }
        else
        {
            cameraBuilder.include(myLocationMarker.getPosition());
            cu = CameraUpdateFactory.newLatLngBounds(cameraBuilder.build(), MAPA_PADDING);

            mMap.moveCamera(cu);
            mMap.animateCamera(CameraUpdateFactory.zoomTo(MAPA_ZOOM));
        }

    }

    public void DibujarMiUbicacion(){
        LatLng latLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());

        Bitmap bitmap = getBitmap(this.getApplicationContext(), R.drawable.ic_marcadoractual);
        MarkerOptions markerOptions = new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(bitmap));

        markerOptions.position(latLng);

        myLocationMarker = mMap.addMarker(markerOptions);
        myLocationMarker.setTag("MyLocation");
        cameraBuilder.include(myLocationMarker.getPosition());

    }

    public void DibujarMarcadores(){

        cameraBuilder       = new LatLngBounds.Builder();

        if(mMap!=null)
        {
            lstMarcadores       = new ArrayList<Marker>();
            lstCirculos         = new ArrayList<c_Circulo>();
            alarmasActivas      = false;
            mMap.clear();

            DibujarMiUbicacion();

            if (lstAlerta != null) {
                if (lstAlerta.size() > 0) {


                    for (Alerta unaAlerta : lstAlerta) {
                        if (unaAlerta.getActiva().equals(SI)) {

                            MarkerOptions mO    = new MarkerOptions();
                            LatLng latLng2      = new LatLng(Double.valueOf(unaAlerta.getLatitud()), Double.valueOf(unaAlerta.getLongitud()));

                            mO.position(latLng2);

                            int areaColor   = COLOR_AREA_ALERTA;
                            int bordeColor  = utilidades.ColorOpacidad(areaColor, BORDER_OPACIDAD);

                            Bitmap bitmap = getBitmap(this.getApplicationContext(), R.drawable.ic_marcadoralerta);

                            if(unaAlerta.getEstado().equals(PROCESADA))
                            {
                                bitmap = getBitmap(this.getApplicationContext(), R.drawable.ic_marcadoralerta_procesada);
                            }

                            mO.icon(BitmapDescriptorFactory.fromBitmap(bitmap));

                            Marker m = mMap.addMarker(mO);

                            m.setTag(unaAlerta.get_ID());

                            if(unaAlerta.getEstado().equals(PENDIENTE)) {
                                CircleOptions circleOptions = new CircleOptions()
                                        .center(m.getPosition())
                                        .fillColor(areaColor)
                                        .strokeColor(bordeColor)
                                        .strokeWidth(BORDER_WIDTH)
                                        .radius(utilidades.ToMetro(unaAlerta.getRango()));
                                Circle circulo = mMap.addCircle(circleOptions);

                                lstCirculos.add(new c_Circulo(circulo, unaAlerta.get_ID()));
                            }

                            lstMarcadores.add(m);
                            cameraBuilder.include(m.getPosition());
                        }

                    }
                }
            }
        }
    }

    public void DibujarRecorrido(){

        //if(o_Observado_ListaAlertas.ServicioActivo()) {
            Viaje viaje = o_Observado_ListaAlertas.DevolverViaje();

            if (viaje != null) {
                // Instantiates a new Polyline object and adds points to define a rectangle
                PolylineOptions rectOptions = new PolylineOptions();

                if(viaje.getRecorrido() != null) {
                    if(polyline!=null){
                        polyline.remove();
                    }

                    for (ViajeRecorrido vr : viaje.getRecorrido()) {
                        LatLng latLng = vr.getLatitud_longitud();
                        rectOptions.add(latLng);
                    }

                    polyline = mMap.addPolyline(rectOptions);
                    polyline.setColor(MAPA_LINEA_COLOR);
                    polyline.setWidth(MAPA_LINEA_WIDTH);
                }

            }
       // }
    }

    public void ArmarBusquedas(){
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
                .build();

        autocompleteFragment.setFilter(typeFilter);

/*
        AutocompleteFilter dd = new AutocompleteFilter.Builder()
                .setTypeFilter(Place.TYPE_COUNTRY)
                .setCountry("US")
                .build();
*/

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.

                MoverCamara(place.getLatLng());

            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                utilidades.MostrarMensaje(TAG, "An error occurred: " + status);
            }
        });
    }
    //<<<<<------------------------------------------------------------------------------------------


    //-----------------------------------------------------------------------------------------------
    //INICIA EL CLIENTE DE GOOGLE, Y SOLICITA ACTUALIZACIONES DE UBICACION
    //-----------------------------------------------------------------------------------------------
    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        CambioUbicacion();
    }

    private synchronized void buildGoogleApiClient() {
        client = new GoogleApiClient.Builder(this)
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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Mapa.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            ActivityCompat.requestPermissions(Mapa.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

            utilidades.MostrarMensaje(TAG, "Sin permiso para ver ubicación");
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(client, mLocationRequest, this);
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(client, this);
    }

    private void CambioUbicacion() {

        if (mMap != null) {

            //Si por alguna razon fuera nula, pongo una por defecto
            if (mCurrentLocation == null) {
                LatLng lng = new LatLng(-34.907627, -56.175032);
                mCurrentLocation = new Location("Falso");
                mCurrentLocation.setLatitude(lng.latitude);
                mCurrentLocation.setLongitude(lng.longitude);
            }

            LatLng latLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());

            if (!ubicacionEncontrada) {

                ubicacionEncontrada = true;
                cameraBuilder       = new LatLngBounds.Builder();

                DibujarMarcadores();

                MoverCamaraTodosMarcadores();

                myLocationMarker.showInfoWindow();


            } else {
                myLocationMarker.setPosition(latLng);
            }
        }

        ActualizarAlertasDistancia();

    }
    //<<<<<------------------------------------------------------------------------------------------

    //-----------------------------------------------------------------------------------------------
    //OTRAS FUNCIONES
    //-----------------------------------------------------------------------------------------------
    public void ArmarMenu() {

        //--------------------------------------------------------------------------------
        //OPCIONES DE MENU ALERTAS
        //--------------------------------------------------------------------------------

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, R.string.drawer_open, R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                //Acciones que se ejecutan cuando se cierra el drawer
                MenuAbierto = false;
            }

            public void onDrawerOpened(View drawerView) {
                //Acciones que se ejecutan cuando se despliega el drawer
                CargarOpcionesMenu();
                MenuAbierto = true;
            }
        };
        //Seteamos la escucha
        drawerLayout.addDrawerListener(drawerToggle);


        //--------------------------------------------------------------------------------
        //OPCIONES DE MENU CONFIGURACION Y VIAJES
        //--------------------------------------------------------------------------------

        //RelativeLayout viajes           = (RelativeLayout) findViewById(R.id.menu_viajes);
        RelativeLayout configuracion    = (RelativeLayout) findViewById(R.id.menu_configuracion);

/*        viajes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Mapa.this, u_viajes.class);
                startActivity(intent);
                drawerLayout.closeDrawers();

            }
        });
*/
        configuracion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Mapa.this, u_Configuracion.class);
                startActivity(intent);
                drawerLayout.closeDrawers();

            }
        });




        //--------------------------------------------------------------------------------
        //CAPTURAR ELEMENTO CLICKEADO
        //--------------------------------------------------------------------------------
        drawerList = (ListView) findViewById(R.id.left_drawer);
        drawerList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                utilidades.Vibrar(VIBRAR_LONG, getApplicationContext());
                Alerta yourData = lstAlerta.get(position);
                MostrarAlertaEditable(yourData.get_ID());
                return false;
            }
        });

        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Alerta yourData = lstAlerta.get(position);
                LatLng latLng = new LatLng(Double.valueOf(yourData.getLatitud()), Double.valueOf(yourData.getLongitud()));
                MoverCamara(latLng);
                drawerLayout.closeDrawers();
            }

        });
        //-

    }

    public void CargarOpcionesMenu() {
        //--------------------------------------------------------------------------------
        //OPCIONES DE MENU
        //--------------------------------------------------------------------------------

        drawerList.setAdapter(new DrawerListAdapter(this, lstAlerta));

    }

    public void CrearAlerta(LatLng arg0) {
        Alerta alerta = new Alerta();
        alerta.setDireccion(utilidades.DevolverDirecciones(this, arg0, TipoDireccion.DIRECCION));

        LatLng origen = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());

        alerta.setRango(RANGO_STANDAR);
        alerta.setLongitud(Double.toString(arg0.longitude));
        alerta.setLatitud(Double.toString(arg0.latitude));
        alerta.setActiva(SI);
        alerta.setDistancia(utilidades.DistanceTo(origen, arg0));
        alerta.setEstado(PENDIENTE);
        alerta.set_ID(o_Observado_ListaAlertas.AddAlerta(alerta));

        DevolverMarcador(alerta.get_ID()).showInfoWindow();


    }

    public void MostrarAlertaEditable(Object valor){
        Alerta alerta           = o_Observado_ListaAlertas.DevolverAlerta((Long) valor);
        final Dialog yourDialog = new Dialog(Mapa.this);
        LayoutInflater inflater = (LayoutInflater) Mapa.this.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View layout       = inflater.inflate(R.layout.seekbar_range, (ViewGroup) findViewById(R.id.your_dialog_root_element));



        yourDialog.setContentView(layout);

        Button btnAlarmaAceptar         = (Button) layout.findViewById(R.id.btn_Alarma_Aceptar);
        Button btnAlarmaEliminar        = (Button) layout.findViewById(R.id.btn_Alarma_Eliminar);
        final SeekBar yourDialogSeekBar = (SeekBar) layout.findViewById(R.id.sb_rango);
        final TextView txtRango         = (TextView) layout.findViewById(R.id.txt_SeekRango);
        TextView txt_d_dst              = (TextView) layout.findViewById(R.id.txt_d_distancia);
        TextView txt_d_dir              = (TextView) layout.findViewById(R.id.txt_d_Direccion);
        Switch sw_activa                = (Switch) layout.findViewById(R.id.switch_d_activa);

        LatLng origen       = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
        LatLng destino      = new LatLng(Double.valueOf(alerta.getLatitud()), Double.valueOf(alerta.getLongitud()));

        Float  _distanciaA      = utilidades.DistanceTo(origen, destino);
        String tDistanciaA      = utilidades.Km_Mt(_distanciaA);

        txt_d_dst.setText(tDistanciaA);
        txtRango.setText("Rango: " + utilidades.ObtenerTextoRango(alerta.getRango()));
        txt_d_dir.setText(alerta.getDireccion());

        if(alerta.getEstado().equals(PENDIENTE))
        {
            sw_activa.setChecked(true);
        }
        else
        {
            sw_activa.setChecked(false);
        }

        yourDialogSeekBar.setMax(RANGO_MAXIMO);
        yourDialogSeekBar.setProgress(alerta.getRango());

        final Alerta finalAlerta = alerta;

        sw_activa.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    finalAlerta.setEstado(PENDIENTE);
                }
                else
                {
                    finalAlerta.setEstado(PROCESADA);
                }
            }
        });

        btnAlarmaAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for(c_Circulo unCirculo : lstCirculos)
                {
                    if(unCirculo.get_ID() == finalAlerta.get_ID())
                    {
                        Circle c;
                        c = unCirculo.getCirculo();
                        c.setRadius(utilidades.ToMetro(yourDialogSeekBar.getProgress()));
                        break;
                    }
                }
                finalAlerta.setRango(yourDialogSeekBar.getProgress());
                o_Observado_ListaAlertas.ModAlerta(finalAlerta, NOTIFICAR);

                yourDialog.cancel();
                DevolverMarcador(finalAlerta.get_ID()).hideInfoWindow();

            }
        });

        btnAlarmaEliminar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                o_Observado_ListaAlertas.DelAlerta(finalAlerta);
                yourDialog.cancel();
                //DevolverMarcador(finalAlerta.get_ID()).hideInfoWindow();
            }
        });

        yourDialogSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                String texto    = "Rango: " + utilidades.ObtenerTextoRango(seekBar.getProgress());
                txtRango.setText(texto);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(yourDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;

        yourDialog.show();

        yourDialog.getWindow().setAttributes(lp);

    }

    private void ManejarBoton(){

        Drawable fondo = ContextCompat.getDrawable(this, R.drawable.circulo);
        //Drawable icono = ContextCompat.getDrawable(this, R.drawable.ic_media_play);

        final Animation zoomin = AnimationUtils.loadAnimation(Mapa.this, R.anim.zoom_in);
        Animation zoomout = AnimationUtils.loadAnimation(Mapa.this, R.anim.zoom_out);
        if(o_Observado_ListaAlertas.ServicioActivo()) {
            fondo = ContextCompat.getDrawable(Mapa.this, R.drawable.circulo_cancelar);
            if (primerServicio)
            {
                primerServicio = false;
                btnIniciarViaje.startAnimation(zoomout);

                //icono = ContextCompat.getDrawable(Mapa.this, R.drawable.ic_stop);

                final Handler handler = new Handler();
                final Drawable finalFondo = fondo;
                //final Drawable finalIcono = icono;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something after 100ms

                        btnIniciarViaje.setBackground(finalFondo);
                        //btnIniciarViaje.setCompoundDrawablesWithIntrinsicBounds(null, finalIcono, null, null);
                        btnIniciarViaje.setPaddingRelative(0, 55, 0, 0);
                        btnIniciarViaje.startAnimation(zoomin);
                    }
                }, getResources().getInteger(R.integer.medio_segundo));
            }
            else
            {
                btnIniciarViaje.setBackground(fondo);
            }

        }
        else
        {

            primerServicio = true;

            if(primerArranque) {
                btnIniciarViaje.setBackground(fondo);
                //btnIniciarViaje.setCompoundDrawablesWithIntrinsicBounds(null, icono, null, null);
                //btnIniciarViaje.setPaddingRelative(0,40,0,0);
                btnIniciarViaje.startAnimation(zoomin);
                primerArranque = false;
            }
            else
            {
                btnIniciarViaje.startAnimation(zoomout);

                final Handler handler = new Handler();
                final Drawable finalFondo1 = fondo;
                //final Drawable finalIcono1 = icono;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something after 100ms
                        btnIniciarViaje.setBackground(finalFondo1);
                        //btnIniciarViaje.setCompoundDrawablesWithIntrinsicBounds(null, finalIcono1, null, null);
                        btnIniciarViaje.setPaddingRelative(0,40,0,0);
                        btnIniciarViaje.startAnimation(zoomin);
                    }
                }, getResources().getInteger(R.integer.medio_segundo));
            }


        }
    }

    public void BuscarDireccion() {
        /*
        LatLng latLng = utilidades.DevolverLatLangDeDireccion(this, etSearch.getText().toString());
        CrearAlerta(latLng);

        etSearch.setText("");
        etSearch.clearFocus();
        etSearch.setCursorVisible(false);

        hideSoftKeyboard();
*/
    }

    public void hideSoftKeyboard() {
        InputMethodManager inputMethodManager =
                (InputMethodManager) this.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                this.getCurrentFocus().getWindowToken(), 0);
    }

    public void ManejarServicio(){
        Intent mServiceIntent       = new Intent(this, AlarmaServicio.class);

        if (o_Observado_ListaAlertas.ServicioActivo())
        {
            utilidades.MostrarMensaje(TAG, "Detener servicio " + AlarmaServicio.class.getCanonicalName());

            o_Observado_ListaAlertas.SetServicioActivo(false);

        }
        else {

            utilidades.MostrarMensaje(TAG, "Iniciar servicio");
            if (o_Observado_ListaAlertas.ExistenAlertasActivasSinProcesar()) {

                Viaje viaje = new Viaje();
                viaje.setEstado(EstadoViaje.EN_PROCESO);
                viaje.setFecha(new Date());
                o_Observado_ListaAlertas.AddViaje(viaje);
                o_Observado_ListaAlertas.ViajeAgregarLatLang(o_Observado_ListaAlertas.DevolverViaje(), new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()));

                if(polyline!=null){
                    polyline.remove();
                }

                startService(mServiceIntent);
            }else
            {

                CharSequence text = "No tiene alertas activas";
                int duration = Toast.LENGTH_LONG;

                Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                toast.show();
            }

        }
    }

    public void ActualizarAlertasDistancia(){
        if(mCurrentLocation != null) {
            if (!o_Observado_ListaAlertas.ServicioActivo()) {
                LatLng origen = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());

                if (lstAlerta != null) {
                    for (Alerta unaAlerta : lstAlerta) {
                        LatLng destino = new LatLng(Double.valueOf(unaAlerta.getLatitud()), Double.valueOf(unaAlerta.getLongitud()));
                        unaAlerta.setDistancia(utilidades.DistanceTo(origen, destino));
                        o_Observado_ListaAlertas.ModAlerta(unaAlerta, SIN_NOTIFICAR);
                    }
                }
            }
        }
    }

    private Bitmap getBitmap(Context context, int drawableId) {

        Drawable drawable = ContextCompat.getDrawable(context, drawableId);

        Bitmap bmp = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);


        return bmp;

    }

    private Marker DevolverMarcador(long ID){
        for(Marker mrkr: lstMarcadores)
        {
            if (mrkr.getTag().equals(ID))
            {
                return mrkr;
            }
        }
        return null;
    }

    private void MoverCamara(LatLng latLang){
        cameraBuilder = new LatLngBounds.Builder();
        cameraBuilder.include(latLang);
        cu = CameraUpdateFactory.newLatLngBounds(cameraBuilder.build(), MAPA_PADDING);

        mMap.moveCamera(cu);
        mMap.animateCamera(CameraUpdateFactory.zoomTo(MAPA_ZOOM));
    }


    //<<<<<------------------------------------------------------------------------------------------

    //-----------------------------------------------------------------------------------------------
    //OBSERVADOR
    //-----------------------------------------------------------------------------------------------
    @Override
    public void update(Observable o, Object arg) {
        c_ListaAlerta = (ListaAlertas) arg;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                lstAlerta = c_ListaAlerta.getLstAlerta();

                alarmasActivas = o_Observado_ListaAlertas.ExistenAlertasActivas();

                CargarOpcionesMenu();
                DibujarMarcadores();
                ManejarBoton();
                DibujarRecorrido();
            }
        });
    }


}
