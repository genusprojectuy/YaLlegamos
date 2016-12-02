package com.genusproject.yallegamos.yallegamos.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.genusproject.yallegamos.yallegamos.R;
import com.genusproject.yallegamos.yallegamos.entidades.Alerta;
import com.genusproject.yallegamos.yallegamos.logica.Observado_ListaAlertas;
import com.genusproject.yallegamos.yallegamos.persistencia.alertaTabla;
import com.genusproject.yallegamos.yallegamos.ui.Mapa;
import com.genusproject.yallegamos.yallegamos.utiles.Constantes;
import com.genusproject.yallegamos.yallegamos.utiles.Utilidades;

import java.util.List;
import java.util.Observer;

import static com.genusproject.yallegamos.yallegamos.utiles.Constantes.ACTIVA;
import static com.genusproject.yallegamos.yallegamos.utiles.Constantes.INACTIVA;
import static com.genusproject.yallegamos.yallegamos.utiles.Constantes.NOTIFICAR;

/**
 * Created by a.devotto on 09/09/2016.
 */

public class DrawerListAdapter extends ArrayAdapter{
    private Utilidades utilidades;
    private Observado_ListaAlertas o_Observado_ListaAlertas;

    public DrawerListAdapter(Context context, List<Alerta> alertas) {
        super(context, 0, alertas);
        o_Observado_ListaAlertas = Observado_ListaAlertas.getInstance(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        utilidades = Utilidades.getInstance();

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)parent.getContext().
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.a_menu_lateral, null);
        }


        TextView name   = (TextView) convertView.findViewById(R.id.lst_txt_name);
        TextView rango  = (TextView) convertView.findViewById(R.id.lst_txt_rango);
        Switch activa   = (Switch) convertView.findViewById(R.id.lst_switch);
        final Alerta item     = (Alerta) getItem(position);

        name.setText(item.getDireccion());
        rango.setText(utilidades.ObtenerTextoRango(item.getRango()));

        if(item.getActiva().equals(ACTIVA))
        {
            activa.setChecked(true);
        }
        else
        {
            activa.setChecked(false);
        }

        activa.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    item.setActiva(ACTIVA);
                }
                else
                {
                    item.setActiva(INACTIVA);
                }

                o_Observado_ListaAlertas.ModAlerta(item, NOTIFICAR);


            }
        });

        return convertView;
    }

}