package com.jhonny.infocar.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.jhonny.infocar.Constantes;
import com.jhonny.infocar.model.DetalleSeguro;
import java.util.Date;

/**
 * Created by jhonny on 09/08/2015.
 */
public class SeguroSQLiteHelper extends SQLiteOpenHelper {

    final String CREAR_TABLA_SEGURO = "CREATE TABLE Seguro (" +
            "idSeguro integer primary key autoincrement, " +
            "idVehiculo integer, " +
            "fecha long, " +
            "compania text, " +
            "tipoSeguro integer, " +
            "numeroPoliza text, " +
            "alerta boolean);";
    final String BORRA_TABLA_SEGURO = "DROP TABLE IF EXISTS Seguro";
    final String CONSULTA_TODAS_SEGURO = "SELECT * FROM Seguro";


    public SeguroSQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREAR_TABLA_SEGURO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Se elimina la versión anterior de la tabla
        db.execSQL(BORRA_TABLA_SEGURO);
        // Se crea la nueva versión de la tabla
        db.execSQL(CONSULTA_TODAS_SEGURO);
    }

    public DetalleSeguro getDatosDelSeguro() {
        DetalleSeguro detalleSeguro = new DetalleSeguro();
        SQLiteDatabase db = null;

        try {
            db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(CONSULTA_TODAS_SEGURO, null);
            if(cursor.moveToFirst()) {
                do {
                    detalleSeguro.setIdSeguro(cursor.getInt(0));
                    detalleSeguro.setIdVehiculo(cursor.getInt(1));
                    detalleSeguro.setFecha(new Date(cursor.getLong(2)));
                    detalleSeguro.setCompania(cursor.getString(3));
                    detalleSeguro.setTipoSeguro(cursor.getInt(4));
                    detalleSeguro.setNumeroPoliza(cursor.getString(5));
                    detalleSeguro.setAlerta(cursor.getInt(6) > 0);

                }while(cursor.moveToNext());
            }
        }catch(Exception ex) {
            ex.printStackTrace();

        }finally {
            if(db != null) {
                db.close();
            }
        }
        return detalleSeguro;
    }

    public boolean borrarDatosDelSeguro(DetalleSeguro detalleSeguro) {
        boolean resp = false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String[] argumentos = new String[]{String.valueOf(detalleSeguro.getIdSeguro())};
            resp = (db.delete(Constantes.TABLA_SEGURO, "idSeguro = ?", argumentos) > 0);
        }catch(Exception ex) {
            ex.printStackTrace();
        }
        return resp;
    }

    public boolean guardarDatosDelSeguro(DetalleSeguro detalleSeguro) {
        boolean resp = false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = creaContentValues(detalleSeguro);
            if(detalleSeguro.getIdSeguro() == null) {
                resp = (db.insert(Constantes.TABLA_SEGURO, null, values) > 0);
            }else {
                String[] argumentos = new String[]{detalleSeguro.getIdSeguro().toString()};
                resp = (db.update(Constantes.TABLA_SEGURO, values, "idSeguro = ?", argumentos) > 0);
            }
        }catch(Exception ex) {
            ex.printStackTrace();
        }
        return resp;
    }

    private ContentValues creaContentValues(DetalleSeguro detalleSeguro) {
        ContentValues values = new ContentValues();
        values.put("idSeguro", detalleSeguro.getIdSeguro());
        values.put("idVehiculo", detalleSeguro.getIdVehiculo());
        values.put("fecha", detalleSeguro.getFecha().getTime());
        values.put("compania", detalleSeguro.getCompania());
        values.put("tipoSeguro", detalleSeguro.getTipoSeguro());
        values.put("numeroPoliza", detalleSeguro.getNumeroPoliza());
        values.put("alerta", detalleSeguro.getAlerta());
        return values;
    }
}
