package com.genusproject.yallegamos.yallegamos.utiles;

import android.graphics.Color;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

/**
 * Created by alvar on 24/09/2016.
 */

public final class Constantes {

    public static final String PENDIENTE    = "PENDIENTE";
    public static final String PROCESADA    = "PROCESADA";
    public static final String SI = "SI";
    public static final String NO = "NO";
    public static final String VIAJE_ID = "VIAJE_ID";

    public static final int RANGO_MAXIMO    = 13;
    public static final int RANGO_STANDAR   = 4;
    public static long UPDATE_INTERVAL_IN_MILLISECONDS              = 10 * 1000;
    public static long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS      = UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    public static float MAPA_PRESISION_MINIMA                       = 40;

    public static int AREA_OPACIDAD     = 50;
    public static int BORDER_WIDTH      = 3;
    public static int BORDER_OPACIDAD   = 255;

    public static int NOTIFICACION_ID   = 1234;
    public static int NOTIFICACION_DESTINO_ID   = 1235;

    public static int VIBRAR_LONG       = 100;
    public static long[] VIBRAR_PAT        = {0, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500};

    public static int COLOR_AREA_ALERTA = Color.argb (AREA_OPACIDAD, 255, 204, 51);
    public static int COLOR_ALERTA     = Color.rgb(249, 153, 0);


    public static int MAPA_PADDING      = 200;
    public static int MAPA_ZOOM         = 15;
    public static int MAPA_LINEA_WIDTH  = 10;
    public static int MAPA_LINEA_COLOR  = Color.BLUE;
    public static int BOTON_DURACION    = 1000;

    public static DecimalFormat DOS_DECIMALES = new DecimalFormat("###.##");
    public static SimpleDateFormat HORA = new SimpleDateFormat("HH:mm");
    public static SimpleDateFormat FECHA_HORA = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public static boolean SIN_NOTIFICAR     = false;
    public static boolean NOTIFICAR         = true;


}
