package com.genusproject.yallegamos.yallegamos.adaptadores;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.genusproject.yallegamos.yallegamos.R;
import com.genusproject.yallegamos.yallegamos.entidades.Alerta;
import com.genusproject.yallegamos.yallegamos.entidades.Viaje;
import com.genusproject.yallegamos.yallegamos.enumerados.TipoDireccion;
import com.genusproject.yallegamos.yallegamos.logica.Observado_ListaAlertas;
import com.genusproject.yallegamos.yallegamos.ui.Mapa;
import com.genusproject.yallegamos.yallegamos.ui.VerViaje;
import com.genusproject.yallegamos.yallegamos.utiles.Utilidades;
import com.google.android.gms.maps.model.LatLng;

import java.util.Date;
import java.util.List;

import static com.genusproject.yallegamos.yallegamos.utiles.Constantes.FECHA_HORA;
import static com.genusproject.yallegamos.yallegamos.utiles.Constantes.HORA;
import static com.genusproject.yallegamos.yallegamos.utiles.Constantes.NO;
import static com.genusproject.yallegamos.yallegamos.utiles.Constantes.NOTIFICAR;
import static com.genusproject.yallegamos.yallegamos.utiles.Constantes.SI;
import static com.genusproject.yallegamos.yallegamos.utiles.Constantes.VIAJE_ID;

/**
 * Created by alvar on 18/12/2016.
 */

public class ViajesAdapter extends ArrayAdapter {
    private Utilidades utilidades;
    private String TAG = this.getClass().getSimpleName().toUpperCase();

    public ViajesAdapter(Context context, List<Viaje> viajes) {
        super(context, 0, viajes);
        utilidades = Utilidades.getInstance();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {

            LayoutInflater inflater = ((Activity) parent.getContext()).getLayoutInflater();
            convertView             = inflater.inflate(R.layout.viajes_item, null);

            holder = new ViewHolder();

            holder.fecha      = (TextView) convertView.findViewById(R.id.v_txt_fecha);
            holder.verViaje   = (ImageView) convertView.findViewById(R.id.v_img_btn_map);
            holder.origen     = (TextView) convertView.findViewById(R.id.v_txt_origen);
            holder.o_hora     = (TextView) convertView.findViewById(R.id.v_txt_o_hora);
            holder.destino    = (TextView) convertView.findViewById(R.id.v_txt_Destino);
            holder.d_hora     = (TextView) convertView.findViewById(R.id.v_txt_d_hora);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Viaje item     = (Viaje) getItem(position);

        LatLng  l_origen;
        Date    l_o_hora;

        LatLng  l_destino;
        Date    l_d_hora;


            holder.fecha.setText(FECHA_HORA.format(item.getFecha()));

            holder.verViaje.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(getContext(), VerViaje.class);
                    intent.putExtra(VIAJE_ID, item.get_ID());
                    getContext().startActivity(intent);


                }
            });

            holder.origen.setText(item.getDireccion_origen());

            if(item.getH_origen() != null)
            {
                holder.o_hora.setText(HORA.format(item.getH_origen()));
            }
            else
            {
                holder.o_hora.setText("-");
            }

            holder.destino.setText(item.getDireccion_destino());

            if(item.getH_destino() != null)
            {
                holder.d_hora.setText(HORA.format(item.getH_destino()));
            }
            else
            {
                holder.d_hora.setText("-");
            }

        return convertView;
    }

    private static class ViewHolder {
        public TextView fecha;
        public ImageView verViaje;
        public TextView origen;
        public TextView o_hora;
        public TextView destino;
        public TextView d_hora;
    }


    /*SEGUNDO METODO, COMPROBANDO OPTIMIZACION*/
/*
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)parent.getContext().
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.viajes_item, null);
        }


        TextView fecha      = (TextView) convertView.findViewById(R.id.v_txt_fecha);
        ImageView verViaje   = (ImageView) convertView.findViewById(R.id.v_img_btn_map);
        TextView origen     = (TextView) convertView.findViewById(R.id.v_txt_origen);
        TextView o_hora     = (TextView) convertView.findViewById(R.id.v_txt_o_hora);
        TextView destino    = (TextView) convertView.findViewById(R.id.v_txt_Destino);
        TextView d_hora     = (TextView) convertView.findViewById(R.id.v_txt_d_hora);


        final Viaje item     = (Viaje) getItem(position);

        LatLng  l_origen;
        Date    l_o_hora;

        LatLng  l_destino;
        Date    l_d_hora;


        if(item.getRecorrido().size() > 0)
        {

            l_origen    = item.getRecorrido().get(0).getLatitud_longitud();
            l_o_hora    = item.getRecorrido().get(0).getFecha();

            l_destino   = item.getRecorrido().get(item.getRecorrido().size() - 1).getLatitud_longitud();
            l_d_hora    = item.getRecorrido().get(item.getRecorrido().size() - 1).getFecha();

            fecha.setText(FECHA_HORA.format(item.getFecha()));

            verViaje.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(getContext(), VerViaje.class);
                    intent.putExtra(VIAJE_ID, item.get_ID());
                    getContext().startActivity(intent);


                }
            });

            origen.setText(utilidades.DevolverDirecciones(getContext(), l_origen, TipoDireccion.DIRECCION));
            o_hora.setText(HORA.format(l_o_hora));

            destino.setText(utilidades.DevolverDirecciones(getContext(), l_destino, TipoDireccion.DIRECCION));
            d_hora.setText(HORA.format(l_d_hora));

        }
        else
        {
            utilidades.MostrarMensaje(TAG, "No se recibio recorrido");
        }


        return convertView;
    }

    */
}