package com.genusproject.yallegamos.yallegamos.persistencia;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.BaseColumns;


import com.genusproject.yallegamos.yallegamos.entidades.Alerta;
import com.genusproject.yallegamos.yallegamos.entidades.Configuracion;
import com.genusproject.yallegamos.yallegamos.entidades.Viaje;
import com.genusproject.yallegamos.yallegamos.entidades.ViajeRecorrido;
import com.genusproject.yallegamos.yallegamos.enumerados.EstadoViaje;
import com.genusproject.yallegamos.yallegamos.enumerados.TipoDireccion;
import com.genusproject.yallegamos.yallegamos.utiles.Utilidades;
import com.google.android.gms.maps.model.LatLng;

import java.sql.Date;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static com.genusproject.yallegamos.yallegamos.utiles.Constantes.FECHA_HORA;
import static com.genusproject.yallegamos.yallegamos.utiles.Constantes.FECHA_HORA_S;

/**
 * Created by alvar on 28/09/2016.
 */

public final class PersistenciaBD {

    private static PersistenciaBD instancia;
    private Utilidades utilidades;
    private String TAG = this.getClass().getSimpleName().toUpperCase();
    private ManejadorDBHelper mDbHelper;
    private static final String TEXT_TYPE           = " TEXT";
    private static final String NUM_TYPE            = " INTEGER";
    private static final String COMMA_SEP           = ",";
    private Context contexto;

    /*ALERTA*/
    private static final String SQL_DELETE_ALERTA = "DROP TABLE IF EXISTS " + alertaReg.ALERTA_TABLE_NAME;
    private static final String SQL_CREATE_ALERTA =
            "CREATE TABLE " + alertaReg.ALERTA_TABLE_NAME + " (" +
                    alertaReg._ID + " INTEGER PRIMARY KEY," +
                    alertaReg.ALERTA_COL__ACTIVA + TEXT_TYPE + COMMA_SEP +
                    alertaReg.ALERTA_COL_LATITUD + TEXT_TYPE + COMMA_SEP +
                    alertaReg.ALERTA_COL__LONGITUD + TEXT_TYPE + COMMA_SEP +
                    alertaReg.ALERTA_COL__DIR + TEXT_TYPE + COMMA_SEP +
                    alertaReg.ALERTA_COL__ESTADO + TEXT_TYPE + COMMA_SEP +
                    alertaReg.ALERTA_COL__DIST + NUM_TYPE + COMMA_SEP +
                    alertaReg.ALERTA_COL__RANGO + NUM_TYPE + " )";

    /*VIAJE*/
    private static final String SQL_DELETE_VIAJE = "DROP TABLE IF EXISTS " + viajeReg.VIAJE_TABLE_NAME;
    private static final String SQL_CREATE_VIAJE =
            "CREATE TABLE " + viajeReg.VIAJE_TABLE_NAME + " (" +
                    viajeReg._ID + " INTEGER PRIMARY KEY," +
                    viajeReg.VIAJE_COL_FECHA + TEXT_TYPE + COMMA_SEP +
                    viajeReg.VIAJE_COL_ESTADO + TEXT_TYPE + " )";

    /*RECORRIDO*/
    private static final String SQL_DELETE_RECORRIDO = "DROP TABLE IF EXISTS " + recorridoReg.RECORRIDO_TABLE_NAME;
    private static final String SQL_CREATE_RECORRIDO =
            "CREATE TABLE " + recorridoReg.RECORRIDO_TABLE_NAME + " (" +
                    recorridoReg._ID + " INTEGER PRIMARY KEY," +
                    recorridoReg.RECORRIDO_COL_VIAJE + NUM_TYPE + COMMA_SEP +
                    recorridoReg.RECORRIDO_COL_LAT + TEXT_TYPE + COMMA_SEP +
                    recorridoReg.RECORRIDO_COL_FECHA + TEXT_TYPE + COMMA_SEP +
                    recorridoReg.RECORRIDO_COL_LONG + TEXT_TYPE + " )";

    /*CONFIGURACION*/
    private static final String SQL_DELETE_CONFIGURACION = "DROP TABLE IF EXISTS " + configuracionReg.CONFIGURACION_TABLE_NAME;
    private static final String SQL_CREATE_CONFIGURACION =
            "CREATE TABLE " + configuracionReg.CONFIGURACION_TABLE_NAME + " (" +
                    configuracionReg._ID + " INTEGER PRIMARY KEY," +
                    configuracionReg.CONFIGURACION_COL_SONACT + TEXT_TYPE + COMMA_SEP +
                    configuracionReg.CONFIGURACION_COL_SONIDO + TEXT_TYPE + COMMA_SEP +
                    configuracionReg.CONFIGURACION_COL_VIBACT + TEXT_TYPE +  " )";

    /*VIAJE_ALARMAS*/
 /*
    private static final String SQL_DELETE_RECORRIDO = "DROP TABLE IF EXISTS " + recorridoReg.RECORRIDO_TABLE_NAME;
    private static final String SQL_CREATE_RECORRIDO =
            "CREATE TABLE " + recorridoReg.RECORRIDO_TABLE_NAME + " (" +
                    recorridoReg._ID + " INTEGER PRIMARY KEY," +
                    recorridoReg.RECORRIDO_COL_VIAJE + NUM_TYPE + COMMA_SEP +
                    recorridoReg.RECORRIDO_COL_LAT + TEXT_TYPE + COMMA_SEP +
                    recorridoReg.RECORRIDO_COL_FECHA + TEXT_TYPE + COMMA_SEP +
                    recorridoReg.RECORRIDO_COL_LONG + TEXT_TYPE + " )";
  */

    //----------------------------------------------------------------------------------------


    //----------------------------------------------------------------------------------------
    //MANEJADOR
    //----------------------------------------------------------------------------------------
    private PersistenciaBD(Context context) {
        utilidades      = Utilidades.getInstance();
        this.contexto   = context;
        mDbHelper       = new ManejadorDBHelper(context);
    }
    //-----------------------------------------------------------------------
    public static final PersistenciaBD getInstancia(Context context){
        if (instancia == null)
        {
            instancia = new PersistenciaBD(context);
        }

        return  instancia;
    }
    //-----------------------------------------------------------------------
    public class ManejadorDBHelper extends SQLiteOpenHelper {
        // If you change the database schema, you must increment the database version.
        public static final int DATABASE_VERSION = 7;
        public static final String DATABASE_NAME = "alertas.db";

        //----------------------------------------------------------------------------------------

        public ManejadorDBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        //----------------------------------------------------------------------------------------

        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_ALERTA);
            db.execSQL(SQL_CREATE_VIAJE);
            db.execSQL(SQL_CREATE_RECORRIDO);
            db.execSQL(SQL_CREATE_CONFIGURACION);
        }

        //----------------------------------------------------------------------------------------

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(SQL_DELETE_ALERTA);
            db.execSQL(SQL_DELETE_VIAJE);
            db.execSQL(SQL_DELETE_RECORRIDO);
            db.execSQL(SQL_DELETE_CONFIGURACION);
            onCreate(db);
        }

        //----------------------------------------------------------------------------------------

        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }

        //----------------------------------------------------------------------------------------
    }
    //<<<<<<----------------------------------------------------------------------------------


    //----------------------------------------------------------------------------------------
    //MANTENIMIENTO DE ALERTA
    //----------------------------------------------------------------------------------------
    public long AlertaAgregar(Alerta alerta){

        SQLiteDatabase db       = mDbHelper.getWritableDatabase();
        ContentValues values    = new ContentValues();

        values.put(alertaReg.ALERTA_COL__ACTIVA, alerta.getActiva());
        values.put(alertaReg.ALERTA_COL_LATITUD, alerta.getLatitud());
        values.put(alertaReg.ALERTA_COL__LONGITUD, alerta.getLongitud());
        values.put(alertaReg.ALERTA_COL__RANGO, alerta.getRango());
        values.put(alertaReg.ALERTA_COL__DIR, alerta.getDireccion());
        values.put(alertaReg.ALERTA_COL__DIST, alerta.getDistancia());
        values.put(alertaReg.ALERTA_COL__ESTADO, alerta.getEstado());

        alerta.set_ID(db.insert(alertaReg.ALERTA_TABLE_NAME, null, values));

        return alerta.get_ID();
    }
    //-----------------------------------------------
    public Alerta AlertaDevolver(long _ID){

        Alerta alerta       = new Alerta();
        SQLiteDatabase db   = mDbHelper.getReadableDatabase();

        String[] projection = {
                alertaReg._ID,
                alertaReg.ALERTA_COL__RANGO,
                alertaReg.ALERTA_COL__LONGITUD,
                alertaReg.ALERTA_COL_LATITUD,
                alertaReg.ALERTA_COL__ACTIVA,
                alertaReg.ALERTA_COL__DIR,
                alertaReg.ALERTA_COL__DIST,
                alertaReg.ALERTA_COL__ESTADO
        };

        String selection        = alertaReg._ID + " = ?";
        String[] selectionArgs  = {Long.toString(_ID)};

        Cursor c = db.query(
                alertaReg.ALERTA_TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );


        if (c.moveToFirst()) {

            alerta.set_ID(c.getLong(c.getColumnIndexOrThrow(alertaReg._ID)));
            alerta.setActiva(c.getString(c.getColumnIndexOrThrow(alertaReg.ALERTA_COL__ACTIVA)));
            alerta.setLatitud(c.getString(c.getColumnIndexOrThrow(alertaReg.ALERTA_COL_LATITUD)));
            alerta.setLongitud(c.getString(c.getColumnIndexOrThrow(alertaReg.ALERTA_COL__LONGITUD)));
            alerta.setRango(c.getInt(c.getColumnIndexOrThrow(alertaReg.ALERTA_COL__RANGO)));
            alerta.setDireccion(c.getString(c.getColumnIndexOrThrow(alertaReg.ALERTA_COL__DIR)));
            alerta.setDistancia(c.getFloat(c.getColumnIndexOrThrow(alertaReg.ALERTA_COL__DIST)));
            alerta.setEstado(c.getString(c.getColumnIndexOrThrow(alertaReg.ALERTA_COL__ESTADO)));

        }
        else
        {
            utilidades.MostrarMensaje(TAG, "No se encontro registro");
        }

        return alerta;
    }
    //-----------------------------------------------
    public List<Alerta> AlertaDevolverLista(){

        List<Alerta> alerta     = new ArrayList<Alerta>();
        SQLiteDatabase db       = mDbHelper.getReadableDatabase();

        String[] projection = {
                alertaReg._ID,
                alertaReg.ALERTA_COL__RANGO,
                alertaReg.ALERTA_COL__LONGITUD,
                alertaReg.ALERTA_COL_LATITUD,
                alertaReg.ALERTA_COL__ACTIVA,
                alertaReg.ALERTA_COL__DIR,
                alertaReg.ALERTA_COL__ESTADO,
                alertaReg.ALERTA_COL__DIST
        };


        Cursor c = db.query(
                alertaReg.ALERTA_TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );


        if (c.moveToFirst()) {
            do
            {
                Alerta a = new Alerta();
                a.set_ID(c.getLong(c.getColumnIndexOrThrow(alertaReg._ID)));
                a.setActiva(c.getString(c.getColumnIndexOrThrow(alertaReg.ALERTA_COL__ACTIVA)));
                a.setLatitud(c.getString(c.getColumnIndexOrThrow(alertaReg.ALERTA_COL_LATITUD)));
                a.setLongitud(c.getString(c.getColumnIndexOrThrow(alertaReg.ALERTA_COL__LONGITUD)));
                a.setRango(c.getInt(c.getColumnIndexOrThrow(alertaReg.ALERTA_COL__RANGO)));
                a.setDireccion(c.getString(c.getColumnIndexOrThrow(alertaReg.ALERTA_COL__DIR)));
                a.setDistancia(c.getFloat(c.getColumnIndexOrThrow(alertaReg.ALERTA_COL__DIST)));
                a.setEstado(c.getString(c.getColumnIndexOrThrow(alertaReg.ALERTA_COL__ESTADO)));

                alerta.add(a);
            } while (c.moveToNext());



        }
        else
        {
            utilidades.MostrarMensaje(TAG, "No se encontro registro");
        }

        return alerta;
    }
    //-----------------------------------------------
    public void AlertaEliminar(long _ID){

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        String selection = alertaReg._ID + " = ?";
        String[] selectionArgs = {Long.toString(_ID)};
        db.delete(alertaReg.ALERTA_TABLE_NAME, selection, selectionArgs);
    }
    //-----------------------------------------------
    public void AlertaModificar(Alerta alerta){
        SQLiteDatabase db       = mDbHelper.getReadableDatabase();
        ContentValues values    = new ContentValues();

        values.put(alertaReg.ALERTA_COL__ACTIVA, alerta.getActiva());
        values.put(alertaReg.ALERTA_COL_LATITUD, alerta.getLatitud());
        values.put(alertaReg.ALERTA_COL__LONGITUD, alerta.getLongitud());
        values.put(alertaReg.ALERTA_COL__RANGO, alerta.getRango());
        values.put(alertaReg.ALERTA_COL__DIR, alerta.getDireccion());
        values.put(alertaReg.ALERTA_COL__DIST, alerta.getDistancia());
        values.put(alertaReg.ALERTA_COL__ESTADO, alerta.getEstado());

        String selection = alertaReg._ID + " = ?";
        String[] selectionArgs = {Long.toString(alerta.get_ID())};

        int count = db.update(
                alertaReg.ALERTA_TABLE_NAME,
                values,
                selection,
                selectionArgs);

    }
    //<<<<<<----------------------------------------------------------------------------------


    //----------------------------------------------------------------------------------------
    //MANTENIMIENTO DE VIAJES
    //----------------------------------------------------------------------------------------
    public long ViajeAgregar(Viaje viaje){

        SQLiteDatabase db       = mDbHelper.getWritableDatabase();
        ContentValues values    = new ContentValues();

        values.put(viajeReg.VIAJE_COL_ESTADO, String.valueOf(viaje.getEstado()));

        String fechaAuxiliar = FECHA_HORA.format(viaje.getFecha());
        values.put(viajeReg.VIAJE_COL_FECHA,  fechaAuxiliar);


        viaje.set_ID(db.insert(viajeReg.VIAJE_TABLE_NAME, null, values));

        return viaje.get_ID();
    }
    //-----------------------------------------------
    public void ViajeAgregarLatLang(long ID, LatLng latLng){
        SQLiteDatabase db           = mDbHelper.getWritableDatabase();
        ContentValues ubicacionReg  = new ContentValues();

        java.util.Date fecha = new java.util.Date();

        ubicacionReg.put(recorridoReg.RECORRIDO_COL_VIAJE, String.valueOf(ID));
        ubicacionReg.put(recorridoReg.RECORRIDO_COL_LAT, String.valueOf(latLng.latitude));
        ubicacionReg.put(recorridoReg.RECORRIDO_COL_LONG, String.valueOf(latLng.longitude));
        ubicacionReg.put(recorridoReg.RECORRIDO_COL_FECHA, FECHA_HORA_S.format(fecha));

        db.insert(recorridoReg.RECORRIDO_TABLE_NAME, null, ubicacionReg);

    }
    //-----------------------------------------------
    public Viaje ViajeDevolver(long _ID){

        Viaje viaje         = new Viaje();
        SQLiteDatabase db   = mDbHelper.getReadableDatabase();

        String[] projection = {
                viajeReg._ID,
                viajeReg.VIAJE_COL_ESTADO,
                viajeReg.VIAJE_COL_FECHA
        };

        String selection        = viajeReg._ID + " = ?";
        String[] selectionArgs  = {Long.toString(_ID)};

        Cursor c = db.query(
                viajeReg.VIAJE_TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );


        if (c.moveToFirst()) {

            viaje.set_ID(c.getLong(c.getColumnIndexOrThrow(viajeReg._ID)));
            viaje.setEstado(EstadoViaje.valueOf(c.getString(c.getColumnIndexOrThrow(viajeReg.VIAJE_COL_ESTADO))));

            String f = c.getString(c.getColumnIndexOrThrow(viajeReg.VIAJE_COL_FECHA));

            java.util.Date fecha = null;
            try {

                fecha = FECHA_HORA.parse(f);
            } catch (ParseException e) {
                utilidades.MostrarMensaje(TAG, "ERROR " +  e.getLocalizedMessage());
            }

            viaje.setFecha(fecha);

            viaje.setRecorrido(RecorridoDevolverLista(viaje.get_ID()));

        }
        else
        {
            utilidades.MostrarMensaje(TAG, "No se encontro registro");
        }




        return viaje;
    }
    //-----------------------------------------------
    public List<Viaje> ViajeDevolverLista(){

        List<Viaje> viaje     = new ArrayList<Viaje>();
        SQLiteDatabase db       = mDbHelper.getReadableDatabase();

        String[] projection = {
                viajeReg._ID,
                viajeReg.VIAJE_COL_ESTADO,
                viajeReg.VIAJE_COL_FECHA
        };


        Cursor c = db.query(
                viajeReg.VIAJE_TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                viajeReg.VIAJE_COL_FECHA+" DESC"
        );


        if (c.moveToFirst()) {
            do
            {
                Viaje v = new Viaje();
                v.set_ID(c.getLong(c.getColumnIndexOrThrow(viajeReg._ID)));
                v.setEstado(EstadoViaje.valueOf(c.getString(c.getColumnIndexOrThrow(viajeReg.VIAJE_COL_ESTADO))));

                String f = c.getString(c.getColumnIndexOrThrow(viajeReg.VIAJE_COL_FECHA));

                java.util.Date fecha = null;
                try {

                    fecha = FECHA_HORA.parse(f);
                } catch (ParseException e) {
                    utilidades.MostrarMensaje(TAG, "ERROR " +  e.getLocalizedMessage());
                }

                v.setFecha(fecha);

                v.setRecorrido(RecorridoDevolverLista(v.get_ID()));

                viaje.add(v);
            } while (c.moveToNext());



        }
        else
        {
            utilidades.MostrarMensaje(TAG, "No se encontro registro");
        }

        return viaje;
    }
    //-----------------------------------------------
    public void ViajeEliminar(long _ID){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        String selection = viajeReg._ID + " = ?";
        String[] selectionArgs = {Long.toString(_ID)};
        db.delete(viajeReg.VIAJE_TABLE_NAME, selection, selectionArgs);

        this.RecorridoEliminar(_ID);

    }
    //-----------------------------------------------
    public void ViajeModificar(Viaje viaje){
        SQLiteDatabase db       = mDbHelper.getReadableDatabase();
        ContentValues values    = new ContentValues();

        values.put(viajeReg.VIAJE_COL_ESTADO, String.valueOf(viaje.getEstado()));

        //String fechaAuxiliar = FECHA_HORA.format(viaje.getFecha());
        //values.put(viajeReg.VIAJE_COL_FECHA, viaje.getFecha().toString());

        String selection = viajeReg._ID + " = ?";
        String[] selectionArgs = {Long.toString(viaje.get_ID())};

        int count = db.update(
                viajeReg.VIAJE_TABLE_NAME,
                values,
                selection,
                selectionArgs);


    }
    //-----------------------------------------------
    private List<ViajeRecorrido> RecorridoDevolverLista(long viaje){
        SQLiteDatabase db           = mDbHelper.getReadableDatabase();
        List<ViajeRecorrido> listaLatLang   = new ArrayList<ViajeRecorrido>();

        String[] r_projection = {
                recorridoReg._ID,
                recorridoReg.RECORRIDO_COL_VIAJE,
                recorridoReg.RECORRIDO_COL_LAT,
                recorridoReg.RECORRIDO_COL_LONG,
                recorridoReg.RECORRIDO_COL_FECHA
        };

        String r_selection        = recorridoReg.RECORRIDO_COL_VIAJE + " = ?";
        String[] r_selectionArgs  = {Long.toString(viaje)};

        Cursor r_c = db.query(
                recorridoReg.RECORRIDO_TABLE_NAME,
                r_projection,
                r_selection,
                r_selectionArgs,
                null,
                null,
                null
        );

        if(r_c.moveToFirst()) {
            do
            {
                ViajeRecorrido viajeRecorrido = new ViajeRecorrido();
                LatLng latLng = new LatLng(r_c.getDouble(r_c.getColumnIndexOrThrow(recorridoReg.RECORRIDO_COL_LAT)), r_c.getDouble(r_c.getColumnIndexOrThrow(recorridoReg.RECORRIDO_COL_LONG)));

                String f = r_c.getString(r_c.getColumnIndexOrThrow(recorridoReg.RECORRIDO_COL_FECHA));

                java.util.Date fecha = null;
                try {
                    fecha = FECHA_HORA_S.parse(f);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                viajeRecorrido.setFecha(fecha);
                viajeRecorrido.setLatitud_longitud(latLng);

                listaLatLang.add(viajeRecorrido);
            } while (r_c.moveToNext());

        }

        return listaLatLang;
    }
    //-----------------------------------------------
    private void RecorridoEliminar(long viaje_ID){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        String selection = recorridoReg.RECORRIDO_COL_VIAJE + " = ?";
        String[] selectionArgs = {Long.toString(viaje_ID)};
        db.delete(recorridoReg.RECORRIDO_TABLE_NAME, selection, selectionArgs);

    }
    //<<<<<<----------------------------------------------------------------------------------


    //----------------------------------------------------------------------------------------
    //MANTENIMIENTO DE CONFIGURACION
    //----------------------------------------------------------------------------------------
    public long ConfiguracionAgregar(Configuracion configuracion){

        SQLiteDatabase db       = mDbHelper.getWritableDatabase();
        ContentValues values    = new ContentValues();

        values.put(configuracionReg.CONFIGURACION_COL_SONACT, configuracion.getSonidoActivo());
        values.put(configuracionReg.CONFIGURACION_COL_SONIDO, configuracion.getSonido().toString());
        values.put(configuracionReg.CONFIGURACION_COL_VIBACT, configuracion.getVibracionActiva());

        configuracion.set_ID(db.insert(configuracionReg.CONFIGURACION_TABLE_NAME, null, values));

        return configuracion.get_ID();
    }
    //-----------------------------------------------
    public Configuracion ConfiguracionDevolver(long _ID){

        Configuracion configuracion       = new Configuracion();
        SQLiteDatabase db   = mDbHelper.getReadableDatabase();

        String[] projection = {
                configuracionReg._ID,
                configuracionReg.CONFIGURACION_COL_VIBACT,
                configuracionReg.CONFIGURACION_COL_SONIDO,
                configuracionReg.CONFIGURACION_COL_SONACT
        };

        String selection        = configuracionReg._ID + " = ?";
        String[] selectionArgs  = {Long.toString(_ID)};

        Cursor c = db.query(
                configuracionReg.CONFIGURACION_TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );


        if (c.moveToFirst()) {

            configuracion.set_ID(c.getLong(c.getColumnIndexOrThrow(configuracionReg._ID)));

            String ur = c.getString(c.getColumnIndexOrThrow(configuracionReg.CONFIGURACION_COL_SONIDO));
            if(!ur.equals(""))
            {
                configuracion.setSonido(Uri.parse(ur));
            }
            configuracion.setSonidoActivo(c.getString(c.getColumnIndexOrThrow(configuracionReg.CONFIGURACION_COL_SONACT)));
            configuracion.setVibracionActiva(c.getString(c.getColumnIndexOrThrow(configuracionReg.CONFIGURACION_COL_VIBACT)));
        }
        else
        {
            utilidades.MostrarMensaje(TAG, "No se encontro registro");
        }

        return configuracion;
    }
    //-----------------------------------------------
    public void ConfiguracionModificar(Configuracion configuracion){
        SQLiteDatabase db       = mDbHelper.getReadableDatabase();
        ContentValues values    = new ContentValues();

        values.put(configuracionReg.CONFIGURACION_COL_VIBACT, configuracion.getVibracionActiva());

        if(configuracion.getSonido() != null)
        {
            values.put(configuracionReg.CONFIGURACION_COL_SONIDO, configuracion.getSonido().toString());
        }
        else
        {
            values.put(configuracionReg.CONFIGURACION_COL_SONIDO, "");
        }

        values.put(configuracionReg.CONFIGURACION_COL_SONACT, configuracion.getSonidoActivo());

        String selection = configuracionReg._ID + " = ?";
        String[] selectionArgs = {Long.toString(configuracion.get_ID())};

        int count = db.update(
                configuracionReg.CONFIGURACION_TABLE_NAME,
                values,
                selection,
                selectionArgs);

    }
    //<<<<<<----------------------------------------------------------------------------------


    //----------------------------------------------------------------------------------------
    //ESTRUCTURAS
    //----------------------------------------------------------------------------------------
    /*ALERTAS*/
    public static class alertaReg implements BaseColumns {
        public static final String ALERTA_TABLE_NAME    = "t_alerta";
        public static final String ALERTA_COL_LATITUD   = "latitud";
        public static final String ALERTA_COL__LONGITUD = "longitud";
        public static final String ALERTA_COL__ACTIVA   = "activa";
        public static final String ALERTA_COL__RANGO    = "rango";
        public static final String ALERTA_COL__DIR      = "direccion";
        public static final String ALERTA_COL__ESTADO   = "estado";
        public static final String ALERTA_COL__DIST     = "distancia";

    }
    //--------------------------------------------------------------
    /*VIAJE*/
    public static class viajeReg implements BaseColumns {
        public static final String VIAJE_TABLE_NAME   = "t_viaje";
        public static final String VIAJE_COL_FECHA    = "fecha";
        public static final String VIAJE_COL_ESTADO   = "estado";
    }
    //--------------------------------------------------------------
    /*VIAJE RECORRIDO*/
    public static class recorridoReg implements BaseColumns {
        public static final String RECORRIDO_TABLE_NAME = "t_recorrido";
        public static final String RECORRIDO_COL_VIAJE  = "viaje_id";
        public static final String RECORRIDO_COL_LAT    = "latitud";
        public static final String RECORRIDO_COL_LONG   = "longitud";
        public static final String RECORRIDO_COL_FECHA  = "fecha";
    }
    //<<<<<----------------------------------------------------------------------------------
    /*CONFIGURACION*/
    public static class configuracionReg implements BaseColumns {
        public static final String CONFIGURACION_TABLE_NAME  = "t_configuracion";
        public static final String CONFIGURACION_COL_SONIDO  = "sonido_uri";
        public static final String CONFIGURACION_COL_SONACT  = "son_act";
        public static final String CONFIGURACION_COL_VIBACT  = "vib_act";
    }
    //<<<<<----------------------------------------------------------------------------------

}
