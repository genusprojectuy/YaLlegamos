package com.genusproject.yallegamos.yallegamos.utiles;

import android.graphics.Color;

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
    public static final String INACTIVA     = "INACTIVA";

    public static final int RANGO_MAXIMO    = 14;
    public static final int RANGO_STANDAR   = 5;
    public static long UPDATE_INTERVAL_IN_MILLISECONDS            = 10000;
    public static long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS    = UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    public static int AREA_OPACIDAD     = 50;
    public static int BORDER_WIDTH      = 3;
    public static int BORDER_OPACIDAD   = 255;

    public static int NOTIFICACION_ID   = 1234;

    public static int VIBRAR_LONG       = 100;
    //public static long[] VIBRAR_PAT        = 100;

    public static int COLOR_AREA_ALERTA   = Color.argb (AREA_OPACIDAD, 255, 204, 51);

    public static int MARCADOR_W       = 40;
    public static int MARCADOR_H       = 40;

}
