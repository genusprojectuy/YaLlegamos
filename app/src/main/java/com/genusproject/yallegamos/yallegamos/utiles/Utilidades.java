package com.genusproject.yallegamos.yallegamos.utiles;

import android.content.Context;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Vibrator;
import android.util.Log;

import com.genusproject.yallegamos.yallegamos.ui.Mapa;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.Locale;
import java.util.Random;

import static com.genusproject.yallegamos.yallegamos.utiles.Constantes.DOS_DECIMALES;

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

    public float ToKm(int numero)
    {
        float valor = ToMetro(numero) / 1000;
        return  valor;
    }

    public float ToMetro(int numero){
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

    public float DistanceTo(LatLng origen, LatLng destino){
        Location locOrigen = new Location("Location Origen");
        locOrigen.setLatitude(origen.latitude);
        locOrigen.setLongitude(origen.longitude);

        Location locDestino = new Location("Location Destino");
        locDestino.setLatitude(destino.latitude);
        locDestino.setLongitude(destino.longitude);

        return locOrigen.distanceTo(locDestino);
    }

    public int ColorOpacidad(int rColor, int opacidad){

        rColor = Color.argb(opacidad, Color.red(rColor), Color.green(rColor), Color.blue(rColor));

        return rColor;
    }

    public void Vibrar(int intervalo, Context context){
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if(v.hasVibrator())
        {
            v.vibrate(intervalo);
        }

    }


    public void MostrarMensaje(String t, String msg)
    {
        Log.e(TAG + t, msg);
    }


    public String Km_Mt(float valor){

        Double distanciaA       = (double) Math.round(valor);

        if(valor < 1000)
        {
            return DOS_DECIMALES.format(distanciaA) + "mt";
        }
        else
        {
            return DOS_DECIMALES.format(distanciaA / 1000) + "km";
        }
    }

    public String DevolverDirecciones(Context context, LatLng latLng, TipoDireccion tDir){
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        String retorno = "";
        List<Address> addresses;

        try {

            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            switch (tDir)
            {
                case DIRECCION: retorno = addresses.get(0).getAddressLine(0);
                    break;
                case CIUDAD: retorno = addresses.get(0).getLocality();
                    break;
                case ESTADO: retorno = addresses.get(0).getAdminArea();
                    break;
                case CODIGO_POSTAL: retorno = addresses.get(0).getPostalCode();
                    break;
                case KNOWN_NAME: retorno = addresses.get(0).getFeatureName();
                    break;
                case PAIS: retorno = addresses.get(0).getCountryName();
                    break;
            }

        }
        catch (Exception ex)
        {
            this.MostrarMensaje(TAG, ex.getLocalizedMessage());
        }

        return retorno;
    }

    public LatLng DevolverLatLangDeDireccion(Context context, String direccion){
        LatLng latLng = new LatLng(0,0);
        try {
            Geocoder geocoder = new Geocoder(context);
            List<Address> addresses;
            String address = direccion;
            addresses = geocoder.getFromLocationName(address, 1);
            if (addresses.size() > 0) {
                double latitude = addresses.get(0).getLatitude();
                double longitude = addresses.get(0).getLongitude();
                latLng = new LatLng(latitude, longitude);
            }
        }
        catch (Exception ex)
        {
            this.MostrarMensaje(TAG, ex.getLocalizedMessage());
        }
        return  latLng;
    }
}
