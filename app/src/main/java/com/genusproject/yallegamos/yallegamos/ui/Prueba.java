package com.genusproject.yallegamos.yallegamos.ui;

import android.app.SearchManager;
import android.content.Context;
import android.database.MatrixCursor;
import android.os.Handler;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.CursorAdapter;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;

import com.genusproject.yallegamos.yallegamos.R;
import com.genusproject.yallegamos.yallegamos.utiles.Utilidades;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

public class Prueba extends AppCompatActivity {

    Utilidades utilidades;
    String TAG = this.getClass().getSimpleName().toUpperCase().trim();
    private GoogleApiClient client;
    MatrixCursor c = new MatrixCursor(new String[]{ BaseColumns._ID, "cityName", "cityCode"});
    private Place lugar;
    private boolean infoVisible;
    private SimpleCursorAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prueba);

        utilidades = Utilidades.getInstance();
        //Load animation
        final Animation slide_down = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_down);

        final Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up);

        // Start animation

        final RelativeLayout relative_layout = (RelativeLayout) findViewById(R.id.layout_padre);



        infoVisible = true;

        relative_layout.setOnClickListener(new  View.OnClickListener(){
            @Override
            public void onClick(View v){

                final ViewGroup.LayoutParams params= relative_layout.getLayoutParams();

                if (infoVisible)
                {

                    infoVisible = false;

                    int alturaInicial   = params.height;
                    int alturaFinal     = ((int) getResources().getDimension(R.dimen.ocultarInfo_Height));

                    relative_layout.animate()
                            .translationY((alturaInicial - alturaFinal))
                            .setDuration(getResources().getInteger(R.integer.medio_segundo))
                            .start();


                }
                else
                {
                    infoVisible = true;

                    int alturaInicial   = params.height;
                    int alturaFinal     = ((int) getResources().getDimension(R.dimen.mostrarInfo_Height));

                    relative_layout.animate()
                            .translationY((alturaFinal - alturaInicial))
                            .setDuration(getResources().getInteger(R.integer.medio_segundo))
                            .start();


                }
            }
        });

    }
}
