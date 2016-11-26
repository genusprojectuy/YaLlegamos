package com.genusproject.yallegamos.yallegamos.utiles;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.os.Vibrator;
import android.util.Log;

import com.genusproject.yallegamos.yallegamos.ui.Mapa;
import com.google.android.gms.maps.model.LatLng;

import java.util.Random;

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
            case 1:
                valor = 100;
                break;
            case 2:
                valor = 200;
                break;
            case 3:
                valor = 400;
                break;
            case 4:
                valor = 800;
                break;
            case 5:
                valor = 1000;
                break;
            case 6:
                valor = 1500;
                break;
            case 7:
                valor = 2000;
                break;
            case 8:
                valor = 2500;
                break;
            case 9:
                valor = 3000;
                break;
            case 10:
                valor = 3500;
                break;
            case 11:
                valor = 4000;
                break;
            case 12:
                valor = 4500;
                break;
            case 13:
                valor = 5000;
                break;
            case 14:
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

    public int RandomColor(int opacidad){
        int rColor  = 0;
        int START   = 1;
        int END     = 10;

        Random randomGenerator  = new Random();

        long range          = (long) END - (long) START + 1;
        long fraction       = (long)(range * randomGenerator.nextDouble());
        int randomNumber    =  (int)(fraction + START);

        switch(randomNumber)
        {
            case 1:
                rColor = Color.BLACK;
                break;
            case 2:
                rColor = Color.BLUE;
                break;
            case 3:
                rColor = Color.CYAN;
                break;
            case 4:
                rColor = Color.DKGRAY;
                break;
            case 5:
                rColor = Color.GRAY;
                break;
            case 6:
                rColor = Color.GREEN;
                break;
            case 7:
                rColor = Color.LTGRAY;
                break;
            case 8:
                rColor = Color.MAGENTA;
                break;
            case 9:
                rColor = Color.RED;
                break;
            case 10:
                rColor = Color.YELLOW;
                break;
        }

        rColor = Color.argb(opacidad, Color.red(rColor), Color.green(rColor), Color.blue(rColor));

        return rColor;
    }

    public int ColorOpacidad(int rColor, int opacidad){

        rColor = Color.argb(opacidad, Color.red(rColor), Color.green(rColor), Color.blue(rColor));

        return rColor;
    }

    public float ColorToHue(int pColor){
        float rColor[] = new float[3];
        Color.RGBToHSV(Color.red(pColor), Color.green(pColor), Color.blue(pColor), rColor);
        return rColor[0];
    }

    public void Vibrar(int intervalo, Context context){
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(intervalo);

    }

    public void MostrarMensaje(String t, String msg)
    {
        Log.e(TAG + t, msg);
    }

}
