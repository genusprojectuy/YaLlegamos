package com.genusproject.yallegamos.yallegamos.logica;

import android.content.Context;

import com.genusproject.yallegamos.yallegamos.entidades.Alerta;
import com.genusproject.yallegamos.yallegamos.persistencia.alertaTabla;
import com.genusproject.yallegamos.yallegamos.utiles.Utilidades;

import java.util.List;

/**
 * Created by a.devotto on 02/12/2016.
 */

public class ListaAlertas {
    private List<Alerta> lstAlerta;
    private boolean ExistenAlertasActivasSinProcesar;
    private boolean ExistenAlertasActivas;
    private static ListaAlertas ourInstance;
    private boolean ServicioActivo;

    public static ListaAlertas getInstance() {
        if (ourInstance == null)
        {
            ourInstance = new ListaAlertas();
        }
        return ourInstance;
    }

    private ListaAlertas() {
    }

    public List<Alerta> getLstAlerta() {
        return lstAlerta;
    }

    public void setLstAlerta(List<Alerta> lstAlerta) {
        this.lstAlerta = lstAlerta;
    }

    public boolean isExistenAlertasActivasSinProcesar() {
        return ExistenAlertasActivasSinProcesar;
    }

    public void setExistenAlertasActivasSinProcesar(boolean existenAlertasActivasSinProcesar) {
        ExistenAlertasActivasSinProcesar = existenAlertasActivasSinProcesar;
    }

    public boolean isExistenAlertasActivas() {
        return ExistenAlertasActivas;
    }

    public void setExistenAlertasActivas(boolean existenAlertasActivas) {
        ExistenAlertasActivas = existenAlertasActivas;
    }

    public boolean isServicioActivo() {
        return ServicioActivo;
    }

    public void setServicioActivo(boolean servicioActivo) {
        ServicioActivo = servicioActivo;
    }
}
