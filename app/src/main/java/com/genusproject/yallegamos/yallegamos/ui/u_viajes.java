package com.genusproject.yallegamos.yallegamos.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.genusproject.yallegamos.yallegamos.R;
import com.genusproject.yallegamos.yallegamos.adaptadores.ViajesAdapter;
import com.genusproject.yallegamos.yallegamos.entidades.Viaje;
import com.genusproject.yallegamos.yallegamos.logica.Observado_ListaAlertas;
import com.genusproject.yallegamos.yallegamos.utiles.Utilidades;

import java.util.ArrayList;
import java.util.List;

import static com.genusproject.yallegamos.yallegamos.utiles.Constantes.VIAJE_ID;
import static com.genusproject.yallegamos.yallegamos.utiles.Constantes.VIBRAR_LONG;

public class u_viajes extends AppCompatActivity {

    private List<Viaje> lstViajes;
    private Observado_ListaAlertas p;
    private Utilidades utilidades;
    private  ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_viajes);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Viajes");
        actionBar.show();

        utilidades = Utilidades.getInstance();
        p = Observado_ListaAlertas.getInstance(getApplicationContext());


        new Thread(new Runnable() {
            public void run() {
                //Aquí ejecutamos nuestras tareas costosas
                CargarLista();
            }
        }).start();




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_viajes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.btn_elim_viajes:
                AlertDialog.Builder builder = new AlertDialog.Builder(u_viajes.this);
                // Add the buttons
                builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        EliminarTodosLosRecorridos();
                        dialog.cancel();
                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        dialog.cancel();
                    }
                });

                builder.setTitle("Eliminar viajes");
                builder.setMessage("Desea eliminar todos los recorridos?");

                // Create the AlertDialog
                AlertDialog dialog = builder.create();

                dialog.show();
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void CargarLista(){



       list = (ListView) findViewById(R.id.lst_viajes);

        CargarDatos();

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                utilidades.Vibrar(VIBRAR_LONG, getApplicationContext());
                final Viaje yourData = lstViajes.get(position);


                AlertDialog.Builder builder = new AlertDialog.Builder(u_viajes.this);
                // Add the buttons
                builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        p.DelViaje(yourData);
                        CargarDatos();
                        dialog.cancel();
                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        dialog.cancel();
                    }
                });

                builder.setTitle("Eliminar viaje");
                builder.setMessage("Desea eliminar el recorrido?");

                // Create the AlertDialog
                AlertDialog dialog = builder.create();

                dialog.show();

                return false;
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Viaje yourData = lstViajes.get(position);
                Intent intent = new Intent(getApplicationContext(), VerViaje.class);
                intent.putExtra(VIAJE_ID, yourData.get_ID());
                startActivity(intent);
            }

            });

    }

    public void CargarDatos(){
        lstViajes = p.ViajeDevolverLista();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                list.setAdapter(new ViajesAdapter(getApplicationContext(), lstViajes));
            }});
    }

    private void EliminarTodosLosRecorridos(){
        p.DelTodosViajes();
        CargarDatos();
    }
}
