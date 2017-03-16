package com.genusproject.yallegamos.yallegamos.ui;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.genusproject.yallegamos.yallegamos.R;
import com.genusproject.yallegamos.yallegamos.entidades.Viaje;
import com.genusproject.yallegamos.yallegamos.entidades.ViajeRecorrido;
import com.genusproject.yallegamos.yallegamos.logica.Observado_ListaAlertas;
import com.genusproject.yallegamos.yallegamos.utiles.Utilidades;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import static com.genusproject.yallegamos.yallegamos.utiles.Constantes.MAPA_LINEA_COLOR;
import static com.genusproject.yallegamos.yallegamos.utiles.Constantes.MAPA_LINEA_WIDTH;
import static com.genusproject.yallegamos.yallegamos.utiles.Constantes.MAPA_PADDING;
import static com.genusproject.yallegamos.yallegamos.utiles.Constantes.MAPA_ZOOM;
import static com.genusproject.yallegamos.yallegamos.utiles.Constantes.VIAJE_ID;

public class VerViaje extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private Viaje viaje;
    private Polyline polyline;
    private LatLngBounds.Builder cameraBuilder;
    private CameraUpdate cu;
    private Utilidades utilidades;
    private String TAG = this.getClass().getSimpleName().toUpperCase().trim();
    private boolean infoVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_viaje);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Observado_ListaAlertas o = Observado_ListaAlertas.getInstance(getApplicationContext());
        Intent intent = getIntent();
        viaje = o.ViajeDevolver(intent.getLongExtra(VIAJE_ID,0));

        utilidades = Utilidades.getInstance();


        //Load animation
        final Animation slide_down = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_down);

        final Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up);

        // Start animation

        final RelativeLayout relative_layout = (RelativeLayout) findViewById(R.id.layout_mas_info);

        relative_layout.setOnClickListener(new  View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (infoVisible)
                {
                    relative_layout.startAnimation(slide_down);
                    infoVisible = false;
                }
                else
                {
                    relative_layout.startAnimation(slide_up);
                    infoVisible = true;
                }
            }
        });


    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.getUiSettings().setMapToolbarEnabled(false);

        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                DibujarRecorrido();
                MoverCamaraTodosMarcadores();
            }
        });


    }


    public void DibujarRecorrido(){


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



    public void MoverCamaraTodosMarcadores(){

        if (viaje != null) {
            if(!viaje.getRecorrido().isEmpty())
            {
                cameraBuilder = new LatLngBounds.Builder();


                for(ViajeRecorrido vr: viaje.getRecorrido()){
                    cameraBuilder.include(vr.getLatitud_longitud());
                }

                cu = CameraUpdateFactory.newLatLngBounds(cameraBuilder.build(), MAPA_PADDING);

                mMap.moveCamera(cu);
                mMap.animateCamera(cu);

            }
        }

    }
}
