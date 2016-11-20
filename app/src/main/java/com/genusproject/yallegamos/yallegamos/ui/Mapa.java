package com.genusproject.yallegamos.yallegamos.ui;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.genusproject.yallegamos.yallegamos.R;
import com.genusproject.yallegamos.yallegamos.adaptadores.DrawerListAdapter;
import com.genusproject.yallegamos.yallegamos.entidades.Alerta;
import com.genusproject.yallegamos.yallegamos.persistencia.alertaTabla;
import com.genusproject.yallegamos.yallegamos.utiles.AlarmaServicio;
import com.genusproject.yallegamos.yallegamos.utiles.Constantes;
import com.genusproject.yallegamos.yallegamos.utiles.Observado;
import com.genusproject.yallegamos.yallegamos.utiles.Utilidades;
import com.genusproject.yallegamos.yallegamos.utiles.c_Circulo;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
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

import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

import static com.genusproject.yallegamos.yallegamos.utiles.Constantes.AREA_OPACIDAD;
import static com.genusproject.yallegamos.yallegamos.utiles.Constantes.BORDER_OPACIDAD;
import static com.genusproject.yallegamos.yallegamos.utiles.Constantes.BORDER_WIDTH;
import static com.genusproject.yallegamos.yallegamos.utiles.Constantes.FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS;
import static com.genusproject.yallegamos.yallegamos.utiles.Constantes.PENDIENTE;
import static com.genusproject.yallegamos.yallegamos.utiles.Constantes.RANGO_MAXIMO;
import static com.genusproject.yallegamos.yallegamos.utiles.Constantes.RANGO_STANDAR;
import static com.genusproject.yallegamos.yallegamos.utiles.Constantes.UPDATE_INTERVAL_IN_MILLISECONDS;
import static com.genusproject.yallegamos.yallegamos.utiles.Constantes.VIBRAR_LONG;

public class Mapa extends FragmentActivity implements GoogleMap.OnInfoWindowClickListener, OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, Observer {

    private GoogleMap mMap;
    private ListView drawerList;
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;
    private Marker myLocationMarker;
    private String TAG                                      = "SERVICIO A";
    private String LOCATION_KEY                             = "location-key";
    private String LAST_UPDATED_TIME_STRING_KEY             = "last-updated-time-string-key";
    private DecimalFormat df                                = new DecimalFormat("###.##");
    private LocationRequest mLocationRequest;
    private Location mCurrentLocation;
    private String mLastUpdateTime;
    private alertaTabla alertaT;
    private List<Alerta> lstAlerta;
    private boolean ubicacionEncontrada;
    private boolean alarmasActivas;
    private LatLngBounds.Builder cameraBuilder;
    private ArrayList<Marker> lstMarcadores;
    private ArrayList<c_Circulo> lstCirculos;
    private int padding         = 300;
    private Boolean MenuAbierto = false;
    private CameraUpdate cu;
    private EditText etSearch;
    private Utilidades utilidades;
    private Button btnIniciarViaje;
    private boolean servicioActivo = false;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapa_matias);

        alertaT     = alertaTabla.getInstancia(this);
        utilidades  = Utilidades.getInstance();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        etSearch = (EditText) findViewById(R.id.et_search);

        etSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etSearch.setCursorVisible(true);
            }
        });

        ImageButton btnSearch = (ImageButton) findViewById(R.id.mapa_btnSearch);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BuscarDireccion();
            }
        });

        btnIniciarViaje = (Button) findViewById(R.id.btn_IniciarViaje);

        updateValuesFromBundle(savedInstanceState);

        buildGoogleApiClient();

        CargarMenu();

        btnIniciarViaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IniciarServicio();
                MoverCamaraTodosMarcadores();
            }
        });


        Observado observado = Observado.getInstancia();
        observado.addObserver(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.getUiSettings().setMapToolbarEnabled(false);

        CargarInfoView();

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener()
        {

            @Override
            public void onMapLongClick(LatLng arg0)
            {
                Vibrar(VIBRAR_LONG);
                CrearAlerta(arg0);
            }
        });

        mMap.clear();
        mMap.setOnInfoWindowClickListener(this);

    }

    @Override
    public void onBackPressed() {
        if(MenuAbierto && drawerLayout != null)
        {
            drawerLayout.closeDrawers();
        }
        else
        {
            finish();
        }
    }

    @Override
    public void onInfoWindowClick(final Marker marker) {
        MostrarInfoView(marker);
    }

    public Action getIndexApiAction() {
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

    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {

            if (savedInstanceState.keySet().contains(LOCATION_KEY)) {
                mCurrentLocation = savedInstanceState.getParcelable(LOCATION_KEY);
            }

            if (savedInstanceState.keySet().contains(LAST_UPDATED_TIME_STRING_KEY)) {
                mLastUpdateTime = savedInstanceState.getString(LAST_UPDATED_TIME_STRING_KEY);
            }

            updateUI();
        }
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
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(client, mLocationRequest, this);
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(client, this);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (client.isConnected()) {
            startLocationUpdates();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop location updates to save battery, but don't disconnect the GoogleApiClient object.
        if (client.isConnected()) {
            stopLocationUpdates();
        }
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
            mCurrentLocation    = LocationServices.FusedLocationApi.getLastLocation(client);
            mLastUpdateTime     = DateFormat.getTimeInstance().format(new Date());

            updateUI();
        }

        startLocationUpdates();

    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation    = location;
        mLastUpdateTime     = DateFormat.getTimeInstance().format(new Date());
        updateUI();
    }

    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i("A", "Connection suspended");
        client.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i("A", "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        //savedInstanceState.putBoolean(REQUESTING_LOCATION_UPDATES_KEY, mRequestingLocationUpdates);
        savedInstanceState.putParcelable(LOCATION_KEY, mCurrentLocation);
        savedInstanceState.putString(LAST_UPDATED_TIME_STRING_KEY, mLastUpdateTime);
        super.onSaveInstanceState(savedInstanceState);
    }

    private void updateUI() {

        if(mMap != null) {
            LatLng latLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());

            if (!ubicacionEncontrada) {

                ubicacionEncontrada = true;
                cameraBuilder       = new LatLngBounds.Builder();

                CargarAlertas();

                DibujarMarcadores();

                MoverCamaraTodosMarcadores();

                myLocationMarker.showInfoWindow();


            } else {
                myLocationMarker.setPosition(latLng);
            }
        }

        ActualizarAlertasDistancia();

    }

    public void CargarMenu(){
        //--------------------------------------------------------------------------------
        //OPCIONES DE MENU
        //--------------------------------------------------------------------------------
        drawerLayout    = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle    = new ActionBarDrawerToggle(
                this, drawerLayout, R.string.drawer_open, R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                //Acciones que se ejecutan cuando se cierra el drawer
                MenuAbierto = false;

                DibujarMarcadores();
            }

            public void onDrawerOpened(View drawerView) {
                //Acciones que se ejecutan cuando se despliega el drawer
                CargarOpcionesMenu();
                MenuAbierto = true;
            }
        };
        //Seteamos la escucha
        drawerLayout.addDrawerListener(drawerToggle);
        //-

        ImageButton boton = (ImageButton) findViewById(R.id.btn_DrawOpen);
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

    }

    public void CargarOpcionesMenu(){
        //--------------------------------------------------------------------------------
        //OPCIONES DE MENU
        //--------------------------------------------------------------------------------
        drawerList      = (ListView) findViewById(R.id.left_drawer);

        drawerList.setAdapter(new DrawerListAdapter(this, lstAlerta));

        //--------------------------------------------------------------------------------
        //CAPTURAR ELEMENTO CLICKEADO
        //--------------------------------------------------------------------------------

        drawerList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Vibrar(VIBRAR_LONG);
                Alerta yourData = lstAlerta.get(position);
                MostrarAlertaEditable(yourData);
                return false;
            }
        });

        //--------------------------------------------------------------------------------


    }

    public void CargarInfoView(){

        //--------------------------------------------------------------------------------------------------
        //-InfoView de Marcadores
        //--------------------------------------------------------------------------------------------------
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter()
        {
            // Use default InfoWindow frame
            @Override
            public View getInfoContents(Marker arg0) {
                return null;
            }

            // Defines the contents of the InfoWindow
            @Override
            public View getInfoWindow(Marker arg0) {
                View v;
                Object valor    = arg0.getTag();
                Alerta alerta;

                if (!valor.equals("MyLocation")) {
                    alerta  = alertaT.DevolverUnRegistro((Long) valor);
                    v       = getLayoutInflater().inflate(R.layout.windowlayout_llegar, null);

                    LatLng latLng       = arg0.getPosition();
                    Geocoder geocoder   = new Geocoder(Mapa.this, Locale.getDefault());

                    List<Address> addresses;

                    try {

                        addresses           = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                        String address      = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                        String city         = addresses.get(0).getLocality();
                        String state        = addresses.get(0).getAdminArea();
                        String country      = addresses.get(0).getCountryName();
                        String postalCode   = addresses.get(0).getPostalCode();
                        String knownName    = addresses.get(0).getFeatureName();

                        TextView tvAddress  = (TextView) v.findViewById(R.id.txt_address);
                        TextView tvRango    = (TextView) v.findViewById(R.id.txt_Alarma_Rango);
                        TextView tvDist     = (TextView) v.findViewById(R.id.txtDistRestante);

                        LatLng origen       = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                        LatLng destino      = new LatLng(Double.valueOf(alerta.getLatitud()), Double.valueOf(alerta.getLongitud()));

                        String tDistanciaA      = "";
                        Float  _distanciaA      = utilidades.DistanceTo(origen, destino);
                        Double distanciaA       = (double) Math.round(_distanciaA);

                        if(distanciaA < 1000)
                        {
                            tDistanciaA = df.format(distanciaA) + "mt";
                        }
                        else
                        {
                            tDistanciaA = df.format(distanciaA / 1000) + "km";
                        }

                        tvDist.setText(tDistanciaA);
                        tvAddress.setText(address);
                        tvRango.setText(ObtenerTextoRango(alerta.getRango()));

                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
                else
                {
                    v = getLayoutInflater().inflate(R.layout.windowlayout, null);
                }

                return v;
            }
        });
    }

    public void CrearAlerta(LatLng  arg0){
        Alerta alerta       = new Alerta();
        Geocoder geocoder   = new Geocoder(Mapa.this, Locale.getDefault());

        List<Address> addresses;

        try {

            addresses           = geocoder.getFromLocation(arg0.latitude, arg0.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address      = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city         = addresses.get(0).getLocality();
            String state        = addresses.get(0).getAdminArea();
            String country      = addresses.get(0).getCountryName();
            String postalCode   = addresses.get(0).getPostalCode();
            String knownName    = addresses.get(0).getFeatureName();

            alerta.setDireccion(address);

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        LatLng origen = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());

        alerta.setRango(RANGO_STANDAR);
        alerta.setLongitud(Double.toString(arg0.longitude));
        alerta.setLatitud(Double.toString(arg0.latitude));
        alerta.setActiva("SI");
        alerta.setDistancia(utilidades.DistanceTo(origen, arg0));
        alerta.setEstado(PENDIENTE);
        alerta.set_ID(alertaT.AgregarRegistro(alerta));

        MarkerOptions markerOptions = new MarkerOptions();

        markerOptions.position(arg0);
        mMap.animateCamera(CameraUpdateFactory.newLatLng(arg0));

        int areaColor   = utilidades.RandomColor(AREA_OPACIDAD);
        int bordeColor  = utilidades.ColorOpacidad(areaColor, BORDER_OPACIDAD);

        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(utilidades.ColorToHue(areaColor)));

        Marker marker = mMap.addMarker(markerOptions);
        marker.setTag(alerta.get_ID());
        marker.showInfoWindow();


        CircleOptions circleOptions = new CircleOptions()
                .center(marker.getPosition())
                .fillColor(areaColor)
                .strokeColor(bordeColor)
                .strokeWidth(BORDER_WIDTH)
                .radius(utilidades.ToMetro(alerta.getRango()));

        Circle circulo = mMap.addCircle(circleOptions);

        lstCirculos.add(new c_Circulo(circulo, alerta.get_ID()));
        lstMarcadores.add(marker);

        lstAlerta.add(alerta);
        CargarOpcionesMenu();

    }

    public void MostrarInfoView(final Marker marker){

        Object valor    = marker.getTag();
        Alerta alerta;

        if (!valor.equals("MyLocation")) {

            alerta                  = alertaT.DevolverUnRegistro((Long) valor);
            final Dialog yourDialog = new Dialog(Mapa.this);
            LayoutInflater inflater = (LayoutInflater) Mapa.this.getSystemService(LAYOUT_INFLATER_SERVICE);
            final View layout       = inflater.inflate(R.layout.seekbar_range, (ViewGroup) findViewById(R.id.your_dialog_root_element));

            yourDialog.setContentView(layout);

            Button btnAlarmaAceptar         = (Button) layout.findViewById(R.id.btn_Alarma_Aceptar);
            Button btnAlarmaEliminar        = (Button) layout.findViewById(R.id.btn_Alarma_Eliminar);
            final SeekBar yourDialogSeekBar = (SeekBar) layout.findViewById(R.id.sb_rango);
            final TextView txtRango         = (TextView) layout.findViewById(R.id.txt_SeekRango);

            String texto    = "Rango: " + ObtenerTextoRango(alerta.getRango());
            texto           += " Distancia del destino: " + df.format(alerta.getDistancia());
            texto           += " Estado: " + alerta.getEstado();

            txtRango.setText(texto);
            yourDialogSeekBar.setMax(RANGO_MAXIMO);
            yourDialogSeekBar.setProgress(alerta.getRango());

            final Alerta finalAlerta = alerta;

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
                    alertaT.Update(finalAlerta);

                    yourDialog.cancel();
                    marker.hideInfoWindow();

                    CargarAlertas();
                    CargarOpcionesMenu();

                }
            });

            btnAlarmaEliminar.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {

                    alertaT.EliminarRegistro(finalAlerta.get_ID());

                    yourDialog.cancel();
                    marker.hideInfoWindow();
                    DibujarMarcadores();
                }
            });

            yourDialogSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    String texto    = "Rango: " + ObtenerTextoRango(seekBar.getProgress());
                    texto           += " Distancia del destino: " + df.format(finalAlerta.getDistancia());
                    texto           += " Estado: " + finalAlerta.getEstado();

                    txtRango.setText(texto);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });

            yourDialog.show();
        }
    }

    public void MostrarAlertaEditable(final Alerta alerta){
        final Dialog yourDialog = new Dialog(Mapa.this);
        LayoutInflater inflater = (LayoutInflater) Mapa.this.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View layout       = inflater.inflate(R.layout.seekbar_range, (ViewGroup) findViewById(R.id.your_dialog_root_element));

        yourDialog.setContentView(layout);

        Button btnAlarmaAceptar         = (Button) layout.findViewById(R.id.btn_Alarma_Aceptar);
        Button btnAlarmaEliminar        = (Button) layout.findViewById(R.id.btn_Alarma_Eliminar);
        final SeekBar yourDialogSeekBar = (SeekBar) layout.findViewById(R.id.sb_rango);
        final TextView txtRango         = (TextView) layout.findViewById(R.id.txt_SeekRango);

        yourDialogSeekBar.setMax(RANGO_MAXIMO);
        yourDialogSeekBar.setProgress(alerta.getRango());

        String texto    = "Rango: " + ObtenerTextoRango(alerta.getRango());
        texto           += " Distancia del destino: " + df.format(alerta.getDistancia());
        texto           += " Estado: " + alerta.getEstado();

        txtRango.setText(texto);

        final Alerta finalAlerta = alerta;

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
                alertaT.Update(finalAlerta);

                yourDialog.cancel();

                CargarAlertas();
                CargarOpcionesMenu();

            }
        });

        btnAlarmaEliminar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                alertaT.EliminarRegistro(finalAlerta.get_ID());

                yourDialog.cancel();
                DibujarMarcadores();

            }
        });

        yourDialogSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                String texto    = "Rango: " + ObtenerTextoRango(seekBar.getProgress());
                texto           += " Distancia del destino: " + df.format(finalAlerta.getDistancia());
                texto           += " Estado: " + finalAlerta.getEstado();

                txtRango.setText(texto);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        yourDialog.show();
    }

    public void CargarAlertas(){
        lstAlerta = alertaT.DevolverAlertas();
    }

    public void Vibrar(int intervalo){
        utilidades.Vibrar(intervalo, getApplicationContext());
    }

    public void MoverCamaraTodosMarcadores(){

        DibujarMarcadores();

        if (alarmasActivas) {
            cu = CameraUpdateFactory.newLatLngBounds(cameraBuilder.build(), padding);
            mMap.moveCamera(cu);
            mMap.animateCamera(cu);
        }
        else
        {
            cameraBuilder.include(myLocationMarker.getPosition());
            cu = CameraUpdateFactory.newLatLngBounds(cameraBuilder.build(), padding);

            mMap.moveCamera(cu);
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        }


    }

    public void DibujarMiUbicacion(){
        LatLng latLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());

        MarkerOptions markerOptions = new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        markerOptions.position(latLng);

        myLocationMarker = mMap.addMarker(markerOptions);
        myLocationMarker.setTag("MyLocation");
        cameraBuilder.include(myLocationMarker.getPosition());

    }

    public void DibujarMarcadores(){

        cameraBuilder       = new LatLngBounds.Builder();

        CargarAlertas();

        if(mMap!=null)
        {
            lstMarcadores       = new ArrayList<Marker>();
            lstCirculos         = new ArrayList<c_Circulo>();
            alarmasActivas      = false;
            mMap.clear();
            DibujarMiUbicacion();

            if (lstAlerta != null) {
                if (lstAlerta.size() > 0) {
                    CargarOpcionesMenu();

                    for (Alerta unaAlerta : lstAlerta) {
                        if (unaAlerta.getActiva().equals("SI")) {
                            alarmasActivas      = true;
                            MarkerOptions mO    = new MarkerOptions();
                            LatLng latLng2      = new LatLng(Double.valueOf(unaAlerta.getLatitud()), Double.valueOf(unaAlerta.getLongitud()));

                            mO.position(latLng2);

                            int areaColor   = utilidades.RandomColor(AREA_OPACIDAD);
                            int bordeColor  = utilidades.ColorOpacidad(areaColor, BORDER_OPACIDAD);

                            mO.icon(BitmapDescriptorFactory.defaultMarker(utilidades.ColorToHue(areaColor)));
                            Marker m = mMap.addMarker(mO);
                            m.setTag(unaAlerta.get_ID());



                            CircleOptions circleOptions = new CircleOptions()
                                    .center(m.getPosition())
                                    .fillColor(areaColor)
                                    .strokeColor(bordeColor)
                                    .strokeWidth(BORDER_WIDTH)
                                    .radius(utilidades.ToMetro(unaAlerta.getRango()));
                            Circle circulo = mMap.addCircle(circleOptions);

                            lstCirculos.add(new c_Circulo(circulo, unaAlerta.get_ID()));
                            lstMarcadores.add(m);
                            cameraBuilder.include(m.getPosition());
                        }

                    }
                }
            }

        }
    }

    public void BuscarDireccion() {
        try{
            Geocoder geocoder = new Geocoder(this);
            List<Address> addresses;
            String address = etSearch.getText().toString();
            addresses = geocoder.getFromLocationName(address, 1);
            if(addresses.size() > 0) {
                double latitude= addresses.get(0).getLatitude();
                double longitude= addresses.get(0).getLongitude();
                LatLng latLng = new LatLng(latitude, longitude);

                CrearAlerta(latLng);

                etSearch.setText("");
                etSearch.clearFocus();
                etSearch.setCursorVisible(false);

                hideSoftKeyboard();


            }
        }
        catch (IOException io)
        {
            Log.e(TAG, io.getLocalizedMessage());
        }

    }

    public void hideSoftKeyboard() {
        InputMethodManager inputMethodManager =
                (InputMethodManager) this.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        try
        {
            inputMethodManager.hideSoftInputFromWindow(
                    this.getCurrentFocus().getWindowToken(), 0);
        }
        catch (Exception e)
        {
            Log.e(TAG, e.getLocalizedMessage());
        }
    }

    public void IniciarServicio(){
        Intent mServiceIntent = new Intent(this, AlarmaServicio.class);

        if (servicioActivo)
        {
            Log.e(TAG, "Detener servicio");
            stopService(mServiceIntent);
        }
        else
        {
            Log.e(TAG, "Iniciar servicio");
            startService(mServiceIntent);
        }

        //btnIniciarViaje.setText("Cancelar");
    }

    public String ObtenerTextoRango(int rango){
        String tRango = new String();

        if(rango < 5)
        {
            tRango =  Float.toString(utilidades.ToMetro(rango)) + " mt";
        }
        else
        {
            tRango =  Float.toString(utilidades.ToKm(rango)) + " km";
        }

        return tRango;
    }

    public void ActualizarAlertasDistancia(){
        if(mCurrentLocation != null)
        {
            LatLng origen = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());

            if (lstAlerta != null) {
                for (Alerta unaAlerta : lstAlerta) {
                    LatLng destino = new LatLng(Double.valueOf(unaAlerta.getLatitud()), Double.valueOf(unaAlerta.getLongitud()));
                    unaAlerta.setDistancia(utilidades.DistanceTo(origen, destino));
                    alertaT.Update(unaAlerta);
                }
            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        Log.e(TAG, "Valor observer cambiado");
        final boolean var   = (boolean) arg;
        servicioActivo      = var;
        Mapa.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // This code will always run on the UI thread, therefore is safe to modify UI elements.
                if(var)
                {
                    btnIniciarViaje.setText(R.string.Cancelar);
                }
                else
                {
                    btnIniciarViaje.setText(R.string.Iniciar_Viaje);
                }
            }
        });
    }

}
