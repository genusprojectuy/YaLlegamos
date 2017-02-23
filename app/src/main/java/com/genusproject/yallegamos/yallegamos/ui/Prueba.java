package com.genusproject.yallegamos.yallegamos.ui;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.MatrixCursor;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.style.CharacterStyle;
import android.widget.CursorAdapter;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;

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

public class Prueba extends AppCompatActivity {

    Utilidades utilidades;
    String TAG = this.getClass().getSimpleName().toUpperCase().trim();

    private static final ArrayList<String> SUGGESTIONS = new ArrayList<String>();


    private SimpleCursorAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prueba);

        SUGGESTIONS.add("Bauru");
        SUGGESTIONS.add("Sao Paulo");
        SUGGESTIONS.add("Rio de Janeiro");
        SUGGESTIONS.add("Bahia");
        SUGGESTIONS.add("Mato Grosso");
        SUGGESTIONS.add("Minas Gerais");
        SUGGESTIONS.add("Tocantins");
        SUGGESTIONS.add("Rio Grande do Sul");

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) findViewById(R.id.prueba_search);

        utilidades = Utilidades.getInstance();

        final String[] from = new String[] {"cityName"};
        final int[] to = new int[] {android.R.id.text1};
        mAdapter = new SimpleCursorAdapter(getApplication(),
                android.R.layout.simple_list_item_1,
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

    }

    // You must implements your logic to get data using OrmLite
    private void populateAdapter(String query) {
        final MatrixCursor c = new MatrixCursor(new String[]{ BaseColumns._ID, "cityName" });
        for (int i=0; i<SUGGESTIONS.size(); i++) {
            if (SUGGESTIONS.get(i).toLowerCase().startsWith(query.toLowerCase()))
                c.addRow(new Object[] {i, SUGGESTIONS.get(i)});
        }
        mAdapter.changeCursor(c);
    }

}
