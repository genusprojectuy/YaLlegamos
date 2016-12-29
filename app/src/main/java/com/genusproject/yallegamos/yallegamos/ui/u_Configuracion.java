package com.genusproject.yallegamos.yallegamos.ui;

import android.app.Activity;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.genusproject.yallegamos.yallegamos.R;
import com.genusproject.yallegamos.yallegamos.entidades.Configuracion;
import com.genusproject.yallegamos.yallegamos.logica.Observado_ListaAlertas;
import com.genusproject.yallegamos.yallegamos.utiles.Utilidades;

import static com.genusproject.yallegamos.yallegamos.utiles.Constantes.NO;
import static com.genusproject.yallegamos.yallegamos.utiles.Constantes.SI;

public class u_Configuracion extends AppCompatActivity {

    private String TAG = this.getClass().getSimpleName().toUpperCase();
    private TextView txt_cfg_select_sound;
    private Switch swc_ActSound;
    private Switch swc_ActVibr;
    private Utilidades utilidades;
    private Observado_ListaAlertas observado;
    private RingtoneManager rm;
    private Ringtone ringtone;
    private Configuracion cfg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_u__configuracion);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Configuración");
        actionBar.show();

        rm          = new RingtoneManager(getApplicationContext());
        utilidades  = Utilidades.getInstance();
        observado   = Observado_ListaAlertas.getInstance(getApplicationContext());
        cfg         = observado.DevolverConfiguracion();

        txt_cfg_select_sound = (TextView) findViewById(R.id.txt_cfg_select_sound);
        swc_ActSound = (Switch) findViewById(R.id.swc_ActSound);
        swc_ActVibr= (Switch) findViewById(R.id.swc_ActVibr);

        CargarNombreSonido();

        if(cfg.getSonidoActivo().equals(SI))
        {
            swc_ActSound.setChecked(true);
        }
        if(cfg.getVibracionActiva().equals(SI))
        {
            swc_ActVibr.setChecked(true);
        }


        txt_cfg_select_sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Seleccione un tono");
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, cfg.getSonido());
                startActivityForResult(intent, 5);
            }
        });

        swc_ActSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    cfg.setSonidoActivo(SI);
                }
                else
                {
                    cfg.setSonidoActivo(NO);
                }

                observado.ModificarConfiguracion(cfg);
            }
        });

        swc_ActVibr.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    cfg.setVibracionActiva(SI);
                }
                else
                {
                    cfg.setVibracionActiva(NO);
                }

                observado.ModificarConfiguracion(cfg);
            }
        });

    }

    public void CargarNombreSonido(){
        String titulo   = "Ninguno";

        if(cfg.getSonido()!=null)
        {
            ringtone = rm.getRingtone(getApplicationContext(), cfg.getSonido());
            titulo   = ringtone.getTitle(getApplicationContext());
        }

        txt_cfg_select_sound.setText(titulo);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent intent)
    {
        if (resultCode == Activity.RESULT_OK && requestCode == 5)
        {
            Uri uri = intent.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            cfg.setSonido(uri);

            observado.ModificarConfiguracion(cfg);
            CargarNombreSonido();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
