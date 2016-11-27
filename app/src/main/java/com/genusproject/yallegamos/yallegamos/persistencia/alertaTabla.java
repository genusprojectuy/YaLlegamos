package com.genusproject.yallegamos.yallegamos.persistencia;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import com.genusproject.yallegamos.yallegamos.entidades.Alerta;
import com.genusproject.yallegamos.yallegamos.utiles.Utilidades;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alvar on 28/09/2016.
 */

public final class alertaTabla {

    private static alertaTabla instancia;

    private alertasDbHelper mDbHelper;
    private static final String TEXT_TYPE           = " TEXT";
    private static final String NUM_TYPE            = " INTEGER";
    private static final String COMMA_SEP           = ",";
    private static final String SQL_DELETE_ENTRIES  = "DROP TABLE IF EXISTS " + alertaReg.TABLE_NAME;
    private static final String SQL_CREATE_ENTRIES  =
            "CREATE TABLE " + alertaReg.TABLE_NAME + " (" +
                    alertaReg._ID + " INTEGER PRIMARY KEY," +
                    alertaReg.COLUMN_NAME_ACTIVA + TEXT_TYPE + COMMA_SEP +
                    alertaReg.COLUMN_NAME_LATITUD + TEXT_TYPE + COMMA_SEP +
                    alertaReg.COLUMN_NAME_LONGITUD + TEXT_TYPE + COMMA_SEP +
                    alertaReg.COLUMN_NAME_DIR + TEXT_TYPE + COMMA_SEP +
                    alertaReg.COLUMN_NAME_ESTADO + TEXT_TYPE + COMMA_SEP +
                    alertaReg.COLUMN_NAME_DIST + NUM_TYPE + COMMA_SEP +
                    alertaReg.COLUMN_NAME_RANGO + NUM_TYPE + " )";


    //----------------------------------------------------------------------------------------

    private alertaTabla(Context context) {
        mDbHelper = new alertasDbHelper(context);
    }

    //----------------------------------------------------------------------------------------

    public static final alertaTabla getInstancia(Context context){
        if (instancia == null)
        {
            instancia = new alertaTabla(context);
        }

        return  instancia;
    }

    //----------------------------------------------------------------------------------------

    public long AgregarRegistro(Alerta alerta){

        SQLiteDatabase db       = mDbHelper.getWritableDatabase();
        ContentValues values    = new ContentValues();

        values.put(alertaReg.COLUMN_NAME_ACTIVA, alerta.getActiva());
        values.put(alertaReg.COLUMN_NAME_LATITUD, alerta.getLatitud());
        values.put(alertaReg.COLUMN_NAME_LONGITUD, alerta.getLongitud());
        values.put(alertaReg.COLUMN_NAME_RANGO, alerta.getRango());
        values.put(alertaReg.COLUMN_NAME_DIR, alerta.getDireccion());
        values.put(alertaReg.COLUMN_NAME_DIST, alerta.getDistancia());
        values.put(alertaReg.COLUMN_NAME_ESTADO, alerta.getEstado());

        alerta.set_ID(db.insert(alertaReg.TABLE_NAME, null, values));

        Log.e("A", "Nueva alerta: " + alerta.toString());

        return alerta.get_ID();
    }

    //----------------------------------------------------------------------------------------

    public Alerta DevolverUnRegistro(long _ID){

        Alerta alerta       = new Alerta();
        SQLiteDatabase db   = mDbHelper.getReadableDatabase();

        String[] projection = {
                alertaReg._ID,
                alertaReg.COLUMN_NAME_RANGO,
                alertaReg.COLUMN_NAME_LONGITUD,
                alertaReg.COLUMN_NAME_LATITUD,
                alertaReg.COLUMN_NAME_ACTIVA,
                alertaReg.COLUMN_NAME_DIR,
                alertaReg.COLUMN_NAME_DIST,
                alertaReg.COLUMN_NAME_ESTADO
        };

        String selection        = alertaReg._ID + " = ?";
        String[] selectionArgs  = {Long.toString(_ID)};

        Cursor c = db.query(
                alertaReg.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );


        if (c.moveToFirst()) {

            alerta.set_ID(c.getLong(c.getColumnIndexOrThrow(alertaReg._ID)));
            alerta.setActiva(c.getString(c.getColumnIndexOrThrow(alertaReg.COLUMN_NAME_ACTIVA)));
            alerta.setLatitud(c.getString(c.getColumnIndexOrThrow(alertaReg.COLUMN_NAME_LATITUD)));
            alerta.setLongitud(c.getString(c.getColumnIndexOrThrow(alertaReg.COLUMN_NAME_LONGITUD)));
            alerta.setRango(c.getInt(c.getColumnIndexOrThrow(alertaReg.COLUMN_NAME_RANGO)));
            alerta.setDireccion(c.getString(c.getColumnIndexOrThrow(alertaReg.COLUMN_NAME_DIR)));
            alerta.setDistancia(c.getFloat(c.getColumnIndexOrThrow(alertaReg.COLUMN_NAME_DIST)));
            alerta.setEstado(c.getString(c.getColumnIndexOrThrow(alertaReg.COLUMN_NAME_ESTADO)));

            Log.e("A", "Leyendo alerta:" + alerta.toString());
        }
        else
        {
            Log.e("A", "No se encontro registro");
        }

        return alerta;
    }

    //----------------------------------------------------------------------------------------

    public List<Alerta> DevolverAlertas(){

        List<Alerta> alerta     = new ArrayList<Alerta>();
        SQLiteDatabase db       = mDbHelper.getReadableDatabase();

        String[] projection = {
                alertaReg._ID,
                alertaReg.COLUMN_NAME_RANGO,
                alertaReg.COLUMN_NAME_LONGITUD,
                alertaReg.COLUMN_NAME_LATITUD,
                alertaReg.COLUMN_NAME_ACTIVA,
                alertaReg.COLUMN_NAME_DIR,
                alertaReg.COLUMN_NAME_ESTADO,
                alertaReg.COLUMN_NAME_DIST
        };


        Cursor c = db.query(
                alertaReg.TABLE_NAME,
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
                a.setActiva(c.getString(c.getColumnIndexOrThrow(alertaReg.COLUMN_NAME_ACTIVA)));
                a.setLatitud(c.getString(c.getColumnIndexOrThrow(alertaReg.COLUMN_NAME_LATITUD)));
                a.setLongitud(c.getString(c.getColumnIndexOrThrow(alertaReg.COLUMN_NAME_LONGITUD)));
                a.setRango(c.getInt(c.getColumnIndexOrThrow(alertaReg.COLUMN_NAME_RANGO)));
                a.setDireccion(c.getString(c.getColumnIndexOrThrow(alertaReg.COLUMN_NAME_DIR)));
                a.setDistancia(c.getFloat(c.getColumnIndexOrThrow(alertaReg.COLUMN_NAME_DIST)));
                a.setEstado(c.getString(c.getColumnIndexOrThrow(alertaReg.COLUMN_NAME_ESTADO)));

                Log.e("A", "Leyendo alerta:" + a.toString());

                alerta.add(a);
            } while (c.moveToNext());



        }
        else
        {
            Log.e("A", "No se encontro registro");
        }

        return alerta;
    }

    //----------------------------------------------------------------------------------------

    public void EliminarRegistro(long _ID){

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        String selection = alertaReg._ID + " = ?";
        String[] selectionArgs = {Long.toString(_ID)};
        db.delete(alertaReg.TABLE_NAME, selection, selectionArgs);
    }

    //----------------------------------------------------------------------------------------

    public void Update(Alerta alerta){
        SQLiteDatabase db       = mDbHelper.getReadableDatabase();
        ContentValues values    = new ContentValues();

        values.put(alertaReg.COLUMN_NAME_ACTIVA, alerta.getActiva());
        values.put(alertaReg.COLUMN_NAME_LATITUD, alerta.getLatitud());
        values.put(alertaReg.COLUMN_NAME_LONGITUD, alerta.getLongitud());
        values.put(alertaReg.COLUMN_NAME_RANGO, alerta.getRango());
        values.put(alertaReg.COLUMN_NAME_DIR, alerta.getDireccion());
        values.put(alertaReg.COLUMN_NAME_DIST, alerta.getDistancia());
        values.put(alertaReg.COLUMN_NAME_ESTADO, alerta.getEstado());

        String selection = alertaReg._ID + " = ?";
        String[] selectionArgs = {Long.toString(alerta.get_ID())};

        int count = db.update(
                alertaReg.TABLE_NAME,
                values,
                selection,
                selectionArgs);

        Log.e("A", "Alertas modificadas: " + count);

    }

    //----------------------------------------------------------------------------------------

    public static class alertaReg implements BaseColumns {
        public static final String TABLE_NAME           = "t_alerta";
        public static final String COLUMN_NAME_LATITUD  = "latitud";
        public static final String COLUMN_NAME_LONGITUD = "longitud";
        public static final String COLUMN_NAME_ACTIVA   = "activa";
        public static final String COLUMN_NAME_RANGO    = "rango";
        public static final String COLUMN_NAME_DIR      = "direccion";
        public static final String COLUMN_NAME_ESTADO   = "estado";
        public static final String COLUMN_NAME_DIST     = "distancia";

    }

    //----------------------------------------------------------------------------------------

    public class alertasDbHelper extends SQLiteOpenHelper {
        // If you change the database schema, you must increment the database version.
        public static final int DATABASE_VERSION = 4;
        public static final String DATABASE_NAME = "alertas.db";

        //----------------------------------------------------------------------------------------

        public alertasDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        //----------------------------------------------------------------------------------------

        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_ENTRIES);
        }

        //----------------------------------------------------------------------------------------

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(SQL_DELETE_ENTRIES);
            onCreate(db);
        }

        //----------------------------------------------------------------------------------------

        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }

        //----------------------------------------------------------------------------------------
    }

    //----------------------------------------------------------------------------------------

}
