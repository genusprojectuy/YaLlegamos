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
import com.genusproject.yallegamos.yallegamos.persistencia.alertaTabla;
import com.genusproject.yallegamos.yallegamos.ui.Mapa;
import com.genusproject.yallegamos.yallegamos.utiles.Constantes;
import com.genusproject.yallegamos.yallegamos.utiles.Utilidades;

import java.util.List;

/**
 * Created by a.devotto on 09/09/2016.
 */

public class DrawerListAdapter extends ArrayAdapter {
    Utilidades utilidades;

    public DrawerListAdapter(Context context, List<Alerta> alertas) {
        super(context, 0, alertas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        utilidades = Utilidades.getInstance();

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)parent.getContext().
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.a_menu_lateral, null);
        }


        final alertaTabla alertaT = alertaTabla.getInstancia(getContext());

        TextView name   = (TextView) convertView.findViewById(R.id.lst_txt_name);
        TextView rango  = (TextView) convertView.findViewById(R.id.lst_txt_rango);
        Switch activa   = (Switch) convertView.findViewById(R.id.lst_switch);
        final Alerta item     = (Alerta) getItem(position);

        name.setText(item.getDireccion());
        rango.setText(ObtenerTextoRango(item.getRango()));

        if(item.getActiva().equals("SI"))
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
                    item.setActiva("SI");
                    //item.setEstado(Constantes.PENDIENTE);
                    alertaT.Update(item);
                }
                else
                {
                    item.setActiva("NO");
                    //item.setEstado(Constantes.PROCESADA);
                    alertaT.Update(item);
                }


            }
        });

        return convertView;
    }

    public String ObtenerTextoRango(int rango)
    {
        String tRango = "";

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
}