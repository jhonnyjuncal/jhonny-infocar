package com.jhonny.infocar.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.jhonny.infocar.Constantes;
import com.jhonny.infocar.model.DetalleItv;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by jhonny on 09/08/2015.
 */
public class ItvSQLiteHelper extends SQLiteOpenHelper {

    final String CREAR_TABLA_ITV = "CREATE TABLE Itv (" +
            "idItv integer primary key autoincrement, " +
            "idVehiculo integer, " +
            "fecha long, " +
            "fechaProxima long, " +
            "lugar text, " +
            "observaciones text);";
    final String BORRA_TABLA_ITV = "DROP TABLE IF EXISTS Itv";
    final String CONSULTA_TODAS_ITV = "SELECT * FROM Itv";


    public ItvSQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREAR_TABLA_ITV);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Se elimina la versión anterior de la tabla
        db.execSQL(BORRA_TABLA_ITV);
        // Se crea la nueva versión de la tabla
        db.execSQL(CONSULTA_TODAS_ITV);
    }

    public DetalleItv getDatosDeLaItv() {
        DetalleItv detalleItv = new DetalleItv();
        SQLiteDatabase db = null;

        try {
            db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(CONSULTA_TODAS_ITV, null);
            if(cursor.moveToFirst()) {
                do {
                    detalleItv.setIdDetalleItv(cursor.getInt(0));
                    detalleItv.setIdVehiculo(cursor.getInt(1));
                    detalleItv.setFecha(new Date(cursor.getLong(2)));
                    detalleItv.setFechaProxima(new Date(cursor.getLong(3)));
                    detalleItv.setLugar(cursor.getString(4));
                    detalleItv.setObservaciones(cursor.getString(5));

                }while(cursor.moveToNext());
            }
        }catch(Exception ex) {
            ex.printStackTrace();

        }finally {
            if(db != null) {
                db.close();
            }
        }
        return detalleItv;
    }

    public boolean borrarDatosDeLaItv(DetalleItv itv) {
        boolean resp = false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String[] argumentos = new String[]{String.valueOf(itv.getIdDetalleItv())};
            resp = (db.delete(Constantes.TABLA_ITV, "idItv = ?", argumentos) > 0);
        }catch(Exception ex) {
            ex.printStackTrace();
        }
        return resp;
    }

    public boolean guardarDatosItv(DetalleItv itv) {
        boolean resp = false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = creaContentValues(itv);
            if(itv.getIdDetalleItv() == null) {
                resp = (db.insert(Constantes.TABLA_ITV, null, values) > 0);
            }else {
                String[] argumentos = new String[]{itv.getIdDetalleItv().toString()};
                resp = (db.update(Constantes.TABLA_ITV, values, "idItv = ?", argumentos) > 0);
            }
        }catch(Exception ex) {
            ex.printStackTrace();
        }
        return resp;
    }

    private ContentValues creaContentValues(DetalleItv d) {
        ContentValues values = new ContentValues();
        values.put("idItv", d.getIdDetalleItv());
        values.put("idVehiculo", d.getIdVehiculo());
        values.put("fecha", d.getFecha().getTime());
        values.put("fechaProxima", d.getFechaProxima().getTime());
        values.put("lugar", d.getLugar());
        values.put("observaciones", d.getObservaciones());
        return values;
    }
}
