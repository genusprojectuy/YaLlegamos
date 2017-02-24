package com.genusproject.yallegamos.yallegamos.ui;

import android.app.SearchManager;
import android.content.Context;
import android.database.MatrixCursor;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CursorAdapter;
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

public class Prueba extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    Utilidades utilidades;
    String TAG = this.getClass().getSimpleName().toUpperCase().trim();
    private GoogleApiClient client;
    MatrixCursor c = new MatrixCursor(new String[]{ BaseColumns._ID, "cityName", "cityCode"});
    private Place lugar;

    private SimpleCursorAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prueba);

        buildGoogleApiClient();

        final SearchView searchView = (SearchView) findViewById(R.id.prueba_search);

        utilidades = Utilidades.getInstance();

        final String[] from = new String[] {"cityName"};
        final int[] to = new int[] {R.id.listTextView};
        mAdapter = new SimpleCursorAdapter(getApplication(),
                R.layout.layout_sugerencias,
                null,
                from,
                to,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        searchView.setSuggestionsAdapter(mAdapter);
        searchView.setIconifiedByDefault(false);
        // Getting selected (clicked) item suggestion
        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionClick(int position) {
                // Your code here
                utilidades.MostrarMensaje(TAG, "Click " + Integer.toString(position));
                return true;
            }

            @Override
            public boolean onSuggestionSelect(int position) {
                // Your code here
                utilidades.MostrarMensaje(TAG, "Selected " + Integer.toString(position));
                return true;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                populateAdapter(s);
                return false;
            }
        });


        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionClick(int position) {
                // Your code here

                if(c != null)
                {
                    if(c.getCount() > 0)
                    {
                        c.move(position);

                        utilidades.MostrarMensaje(TAG, "Lugar seleccionado: " + c.getString(c.getColumnIndex("cityCode")));

                        ReturnPlaceFromID(c.getString(c.getColumnIndex("cityCode")));
                        searchView.setQuery("", false);
                        searchView.clearFocus();

                    }
                }

                return true;
            }

            @Override
            public boolean onSuggestionSelect(int position) {
                // Your code here
                return true;
            }
        });

    }

    // You must implements your logic to get data using OrmLite
    private void populateAdapter(final String query) {


        new Thread(new Runnable() {
            public void run() {
                //Aquí ejecutamos nuestras tareas costosas
                BuscarSugerencias(query);
            }
        }).start();




    }


    //BUSCAR
    private void BuscarSugerencias(String query) {

        c = new MatrixCursor(new String[]{ BaseColumns._ID, "cityName", "cityCode"});

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

        int i = 0;
        while (iterator.hasNext()) {
            AutocompletePrediction prediction = iterator.next();
            // Get the details of this prediction and copy it into a new PlaceAutocomplete object.
            //resultList.add(new PlaceAutocomplete.(prediction.getPlaceId(),
            //        prediction.getPlaceId()));

            utilidades.MostrarMensaje(TAG, "bbb " + prediction.getFullText(null));
            //SUGGESTIONS.add(prediction.getFullText(null).toString());
            c.addRow(new Object[] {i, prediction.getFullText(null).toString(), prediction.getPlaceId()});

            i +=1;

        }

        // Release the buffer now that all data has been copied.
        autocompletePredictions.release();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {


                utilidades.MostrarMensaje(TAG, c.getCount() + " -----");
                mAdapter.changeCursor(c);




            }
        });
    }




    //----------------------------------------------------------------------------------------------------------------
    //Google api
    //----------------------------------------------------------------------------------------------------------------

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

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void ReturnPlaceFromID(String ID)
    {

        Places.GeoDataApi.getPlaceById(client, ID)
                .setResultCallback(new ResultCallback<PlaceBuffer>() {
                    @Override
                    public void onResult(PlaceBuffer places) {
                        if (places.getStatus().isSuccess()) {
                            lugar = places.get(0);
                            LatLng queriedLocation = lugar.getLatLng();
                            utilidades.MostrarMensaje(TAG, "Latitude is"  + queriedLocation.latitude);
                            utilidades.MostrarMensaje(TAG, "Longitude is" + queriedLocation.longitude);

                        }
                        places.release();
                    }
                });
    }
}
