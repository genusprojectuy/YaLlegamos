package com.genusproject.yallegamos.yallegamos.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.media.Image;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.genusproject.yallegamos.yallegamos.R;
import com.genusproject.yallegamos.yallegamos.adaptadores.ViajesAdapter;
import com.genusproject.yallegamos.yallegamos.entidades.Viaje;
import com.genusproject.yallegamos.yallegamos.entidades.ViajeRecorrido;
import com.genusproject.yallegamos.yallegamos.logica.Observado_ListaAlertas;
import com.genusproject.yallegamos.yallegamos.utiles.Utilidades;
import com.google.android.gms.maps.model.LatLng;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.genusproject.yallegamos.yallegamos.utiles.Constantes.VIAJE_ID;
import static com.genusproject.yallegamos.yallegamos.utiles.Constantes.VIBRAR_LONG;

public class u_viajes extends AppCompatActivity {

    private List<Viaje> lstViajes;
    private Observado_ListaAlertas p;
    private Utilidades utilidades;
    private  ListView list;
    private ProgressBar progressBar;
    private String TAG = this.getClass().getSimpleName().toUpperCase().trim();
    private int anchoInicial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_viajes);

        utilidades = Utilidades.getInstance();

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Viajes");
        actionBar.show();

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(u_viajes.this, new String[]{Manifest.permission.INTERNET}, 1);

            utilidades.MostrarMensaje(TAG, "Sin permiso para ENTRAR A INTERNET");

            return;

        }

        progressBar = (ProgressBar) findViewById(R.id.progreso_cargar_viajes);
        progressBar.setVisibility(View.VISIBLE);

         p = Observado_ListaAlertas.getInstance(getApplicationContext());


        new Thread(new Runnable() {
            public void run() {
                //Aquí ejecutamos nuestras tareas costosas
                CargarLista();
            }
        }).start();


        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        anchoInicial   = size.x;


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

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, final View view, int position, long l) {

                utilidades.MostrarMensaje(TAG, "Ampliando layout");

                LinearLayout imagen = (LinearLayout) view.findViewById(R.id.layout_imagen);
                ViewGroup.LayoutParams params= imagen.getLayoutParams();


                int alturaInicial  = params.height;

                int alturaOcultar   = ((int) getResources().getDimension(R.dimen.ocultarImg_Height));
                int alturaMostrar   = ((int) getResources().getDimension(R.dimen.mostrarImg_Height));


                if(alturaInicial == alturaMostrar)
                {
                    params.height = alturaOcultar;

                    utilidades.MostrarMensaje(TAG, "Ocultar");
                }
                else
                {
                    utilidades.MostrarMensaje(TAG, "Mostrar");

                    final ProgressBar progreso = (ProgressBar) view.findViewById(R.id.progreso_cargando_imagen);
                    progreso.setVisibility(View.VISIBLE);
                    params.height = alturaMostrar;


                    //Mostrar mapa
                    final ImageView img = (ImageView) view.findViewById(R.id.mapa_img);
                    Viaje yourData = lstViajes.get(position);
                    final String surl = ArmarUrl(anchoInicial, alturaMostrar, yourData);

                    img.setVisibility(View.INVISIBLE);

                    new Thread(new Runnable() {
                        public void run() {
                            Bitmap bmp = null;
                            try {
                                URL url = new URL(surl);
                                utilidades.MostrarMensaje(TAG, "Buscando imagen: " + surl);
                                bmp     = BitmapFactory.decodeStream(url.openConnection().getInputStream());


                            } catch (Exception e) {
                                utilidades.MostrarMensaje(TAG, "Error al capturar imagen");
                                e.printStackTrace();

                            }


                            final Bitmap finalBmp = bmp;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    img.setImageBitmap(finalBmp);
                                    progreso.setVisibility(View.INVISIBLE);
                                    img.setVisibility(View.VISIBLE);
                                }});


                        }
                    }).start();








                }

                imagen.setLayoutParams(params);
            }
        });

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
/*
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Viaje yourData = lstViajes.get(position);
                Intent intent = new Intent(getApplicationContext(), VerViaje.class);
                intent.putExtra(VIAJE_ID, yourData.get_ID());
                startActivity(intent);
            }

            });
*/
    }

    public void CargarDatos(){
        lstViajes = p.ViajeDevolverLista();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                list.setAdapter(new ViajesAdapter(getApplicationContext(), lstViajes));
                progressBar.setVisibility(View.INVISIBLE);
            }});
    }

    private void EliminarTodosLosRecorridos(){
        p.DelTodosViajes();
        CargarDatos();
    }

    private String ArmarUrl(int ancho, int alto, Viaje viaje){
        String latitudesLongitudes = "";

        for(ViajeRecorrido viajeRecorrido : viaje.getRecorrido())
        {
            if(latitudesLongitudes.isEmpty()){
                latitudesLongitudes += viajeRecorrido.getLatitud_longitud().latitude + "," + viajeRecorrido.getLatitud_longitud().longitude;
            }
            else
            {
                latitudesLongitudes += "|" + viajeRecorrido.getLatitud_longitud().latitude + "," + viajeRecorrido.getLatitud_longitud().longitude;
            }


        }
        String url ="https://maps.googleapis.com/maps/api/staticmap?size=" + ancho + "x" + alto + "&path=color:0xff0000ff|weight:5|" + latitudesLongitudes + "&key=" + (getResources().getString(R.string.google_maps_key)) ;
        return  url;
    }
}
