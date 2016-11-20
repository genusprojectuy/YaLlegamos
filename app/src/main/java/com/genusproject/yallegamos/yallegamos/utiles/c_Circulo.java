package com.genusproject.yallegamos.yallegamos.utiles;

import com.google.android.gms.maps.model.Circle;

import java.util.List;

/**
 * Created by alvar on 10/10/2016.
 */

public class c_Circulo {
    private Circle circulo;
    private long _ID;

    public c_Circulo(Circle circulo, long _ID) {
        this.circulo = circulo;
        this._ID = _ID;
    }

    public Circle getCirculo() {
        return circulo;
    }

    public void setCirculo(Circle circulo) {
        this.circulo = circulo;
    }

    public long get_ID() {
        return _ID;
    }

    public void set_ID(long _ID) {
        this._ID = _ID;
    }
}
