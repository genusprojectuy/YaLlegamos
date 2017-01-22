package com.genusproject.yallegamos.yallegamos.ui;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.style.CharacterStyle;
import android.widget.SearchView;

import com.genusproject.yallegamos.yallegamos.R;
import com.genusproject.yallegamos.yallegamos.utiles.Utilidades;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

public class Prueba extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    Utilidades utilidades;
    String TAG = this.getClass().getSimpleName().toUpperCase().trim();
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prueba);
        buildGoogleApiClient();

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) findViewById(R.id.prueba_search);
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

        utilidades = Utilidades.getInstance();

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            final String query = intent.getStringExtra(SearchManager.QUERY);

            utilidades.MostrarMensaje(TAG, "Hace algo");

            new Thread(new Runnable() {
                public void run() {
                    //Aquí ejecutamos nuestras tareas costosas
                    doMySearch(query);
                }
            }).start();

        }

    }

    @Override
    public void onStart() {
        super.onStart();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.disconnect();
    }

    private synchronized void buildGoogleApiClient() {
        client =  new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();


    }

    public void doMySearch(String query){
        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
                .build();

        PendingResult<AutocompletePredictionBuffer> result =
                Places.GeoDataApi.getAutocompletePredictions(client, query,
                        null, typeFilter);


        // This method should have been called off the main UI thread. Block and wait for at most 60s
        // for a result from the API.
        AutocompletePredictionBuffer autocompletePredictions = result.await(60, TimeUnit.SECONDS);


        // Confirm that the query completed successfully, otherwise return null
        final Status status = autocompletePredictions.getStatus();
        if (!status.isSuccess()) {
            autocompletePredictions.release();
            utilidades.MostrarMensaje(TAG, "Algo");
        }

        // Copy the results into our own data structure, because we can't hold onto the buffer.
        // AutocompletePrediction objects encapsulate the API response (place ID and description).
        Iterator<AutocompletePrediction> iterator = autocompletePredictions.iterator();
        //ArrayList resultList = new ArrayList<>(autocompletePredictions.getCount());
        while (iterator.hasNext()) {
            AutocompletePrediction prediction = iterator.next();
            // Get the details of this prediction and copy it into a new PlaceAutocomplete object.
            //resultList.add(new PlaceAutocomplete.(prediction.getPlaceId(),
            //        prediction.getPlaceId()));
            utilidades.MostrarMensaje(TAG, "aaa " + prediction.getPlaceId());

            utilidades.MostrarMensaje(TAG, "aaa " + prediction.getFullText(null));
        }

        // Release the buffer now that all data has been copied.
        autocompletePredictions.release();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
