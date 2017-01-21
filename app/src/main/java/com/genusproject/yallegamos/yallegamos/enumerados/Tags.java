package com.genusproject.yallegamos.yallegamos.enumerados;

/**
 * Created by alvar on 21/01/2017.
 */

public enum Tags {
    FECHA ("[%=FECHA]", "-"),
    DIRECCION ("[%=DIRECCION]", "-"),
    HORA ("[%=HORA]", "00:00"),
    DISTANCIA("[%=DISTANCIA]", "-"),
    TIEMPO ("[%=TIEMPO]", "00:00");

    private final String TAG;
    private final String VALOR_DEFECTO;

    Tags (String TAG, String VALOR_DEFECTO) {
        this.TAG = TAG;
        this.VALOR_DEFECTO = VALOR_DEFECTO;
    }

    public String getTag() { return TAG; }
    public String getValorPorDefecto() { return VALOR_DEFECTO; }
}
