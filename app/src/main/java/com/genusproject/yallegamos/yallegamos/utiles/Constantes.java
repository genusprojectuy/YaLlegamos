package com.genusproject.yallegamos.yallegamos.utiles;

import android.graphics.Color;

import java.text.DecimalFormat;

/**
 * Created by alvar on 24/09/2016.
 */

public final class Constantes {
    public static final int SUCCESS_RESULT          = 0;
    public static final int FAILURE_RESULT          = 1;
    public static final String PACKAGE_NAME         = "com.genusproject.yallegamos.yallegamos"; //com.google.android.gms.location.sample.locationaddress
    public static final String RECEIVER             = PACKAGE_NAME + ".RECEIVER";
    public static final String RESULT_DATA_KEY      = PACKAGE_NAME + ".RESULT_DATA_KEY";
    public static final String LOCATION_DATA_EXTRA  = PACKAGE_NAME + ".LOCATION_DATA_EXTRA";

    public static final String PENDIENTE    = "PENDIENTE";
    public static final String PROCESADA    = "PROCESADA";
    public static final String ACTIVA       = "SI";
    public static final String INACTIVA     = "NO";

    public static final int RANGO_MAXIMO    = 13;
    public static final int RANGO_STANDAR   = 4;
    public static long UPDATE_INTERVAL_IN_MILLISECONDS            = 10000;
    public static long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS    = UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    public static int AREA_OPACIDAD     = 50;
    public static int BORDER_WIDTH      = 3;
    public static int BORDER_OPACIDAD   = 255;

    public static int NOTIFICACION_ID   = 1234;
    public static int NOTIFICACION_DESTINO_ID   = 1235;

    public static int VIBRAR_LONG       = 100;
    public static long[] VIBRAR_PAT        = {0, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500};

    public static int COLOR_AREA_ALERTA   = Color.argb (AREA_OPACIDAD, 255, 204, 51);

    public static int MAPA_PADDING = 200;
    public static int MAPA_ZOOM = 15;

    public static DecimalFormat DOS_DECIMALES = new DecimalFormat("###.##");

    public static boolean SIN_NOTIFICAR     = false;
    public static boolean NOTIFICAR         = true;


}
