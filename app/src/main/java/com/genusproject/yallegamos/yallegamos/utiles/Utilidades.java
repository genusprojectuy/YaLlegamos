package com.genusproject.yallegamos.yallegamos.utiles;

import android.content.Context;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Switch;

import com.genusproject.yallegamos.yallegamos.entidades.ViajeRecorrido;
import com.genusproject.yallegamos.yallegamos.enumerados.Tags;
import com.genusproject.yallegamos.yallegamos.enumerados.TipoDireccion;
import com.google.android.gms.maps.model.LatLng;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import static com.genusproject.yallegamos.yallegamos.utiles.Constantes.DOS_DECIMALES;
import static com.genusproject.yallegamos.yallegamos.utiles.Constantes.FECHA_HORA;
import static com.genusproject.yallegamos.yallegamos.utiles.Constantes.HORA;

/**
 * Created by alvar on 10/10/2016.
 */
public class Utilidades {
    private static Utilidades ourInstance = new Utilidades();
    private String TAG = "SERVICIO - ";

    public static Utilidades getInstance() {
        return ourInstance;
    }

    private Utilidades() {
    }

    public float ToKm(int numero) {
        float valor = ToMetro(numero) / 1000;
        return valor;
    }

    public float ToMetro(int numero) {
        float valor = 0;

        switch (numero) {
            case 0:
                valor = 100;
                break;
            case 1:
                valor = 200;
                break;
            case 2:
                valor = 400;
                break;
            case 3:
                valor = 800;
                break;
            case 4:
                valor = 1000;
                break;
            case 5:
                valor = 1500;
                break;
            case 6:
                valor = 2000;
                break;
            case 7:
                valor = 2500;
                break;
            case 8:
                valor = 3000;
                break;
            case 9:
                valor = 3500;
                break;
            case 10:
                valor = 4000;
                break;
            case 11:
                valor = 4500;
                break;
            case 12:
                valor = 5000;
                break;
            case 13:
                valor = 10000;
                break;

        }

        return valor;
    }

    public float DistanceTo(LatLng origen, LatLng destino) {
        Location locOrigen = new Location("Location Origen");
        locOrigen.setLatitude(origen.latitude);
        locOrigen.setLongitude(origen.longitude);

        Location locDestino = new Location("Location Destino");
        locDestino.setLatitude(destino.latitude);
        locDestino.setLongitude(destino.longitude);

        return locOrigen.distanceTo(locDestino);
    }

    public int ColorOpacidad(int rColor, int opacidad) {

        rColor = Color.argb(opacidad, Color.red(rColor), Color.green(rColor), Color.blue(rColor));

        return rColor;
    }

    public void Vibrar(int intervalo, Context context) {
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (v.hasVibrator()) {
            v.vibrate(intervalo);
        }

    }


    public void MostrarMensaje(String t, String msg) {
        Log.e(TAG + t, msg);
    }


    public String Km_Mt(float valor) {

        Double distanciaA = (double) Math.round(valor);

        if (valor < 1000) {
            return DOS_DECIMALES.format(distanciaA) + "mt";
        } else {
            return DOS_DECIMALES.format(distanciaA / 1000) + "km";
        }
    }

    public String DevolverDirecciones(Context context, LatLng latLng, TipoDireccion tDir) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        String retorno = "";
        List<Address> addresses;

        try {

            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            switch (tDir) {
                case DIRECCION:
                    retorno = addresses.get(0).getAddressLine(0);
                    break;
                case CIUDAD:
                    retorno = addresses.get(0).getLocality();
                    break;
                case ESTADO:
                    retorno = addresses.get(0).getAdminArea();
                    break;
                case CODIGO_POSTAL:
                    retorno = addresses.get(0).getPostalCode();
                    break;
                case KNOWN_NAME:
                    retorno = addresses.get(0).getFeatureName();
                    break;
                case PAIS:
                    retorno = addresses.get(0).getCountryName();
                    break;
            }

        } catch (Exception ex) {
            this.MostrarMensaje(TAG, ex.getLocalizedMessage());
        }

        return retorno;
    }

    public String ObtenerTextoRango(int rango) {
        String tRango = "";

        if (rango < 4) {
            tRango = Float.toString(ToMetro(rango)) + " mt";
        } else {
            tRango = Float.toString(ToKm(rango)) + " km";
        }

        return tRango;
    }


    public String ReemplazarTags(String contenido, Object valor, Tags etiqueta) {
        String reemplazo = "";

        if (valor == null) {
            reemplazo = etiqueta.getValorPorDefecto();
        } else {
            switch (etiqueta) {
                case FECHA:
                    reemplazo = FECHA_HORA.format(valor);
                    break;
                case HORA:
                    reemplazo = HORA.format(valor);
                    break;
                case DIRECCION:
                    reemplazo = (String) valor;
                    break;
                case DISTANCIA:
                    reemplazo = this.Km_Mt((float) valor);
                    break;
                case TIEMPO:
                    long time = (long) valor;
                    reemplazo = CalcularDuracion(time);
                    break;
                default:
                    try {
                        reemplazo = (String) valor;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }

        }


        return contenido.replace(etiqueta.getTag(), reemplazo);


    }

    private String CalcularDuracion(long time)
    {

        String retorno = "";
        float seconds = (time / 1000);
        float minuts = seconds / 60;
        float hour = minuts / 60;

        int entero = (int) hour;
        minuts = (entero - hour) * 60;
        entero = (int) minuts;
        seconds = (entero - minuts) * 60;

        //return (Integer.toString((int) hour) + ":" + Integer.toString((int) minuts) + ":" + Integer.toString((int) seconds));
        return (CalcularDuracion_2((int) hour) + ":" + CalcularDuracion_2((int) minuts) + ":" + CalcularDuracion_2((int) seconds));

    }

    private String CalcularDuracion_2(int valor)
    {
        if(valor < 10)
        {
            return "0" + Integer.toString(valor);
        }
        else
        {
            return Integer.toString(valor);
        }
    }

}
