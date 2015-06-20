package com.jhonny.infocar.sql;

import java.util.ArrayList;
import java.util.Date;
import com.jhonny.infocar.Constantes;
import com.jhonny.infocar.model.DetalleReparacion;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;


public class ReparacionesSQLiteHelper extends SQLiteOpenHelper {
	
	final String CREAR_TABLA_REPARACIONES = "CREATE TABLE Reparaciones (" +
			"idReparacion integer primary key autoincrement, " +
			"fecha long, " +
			"kms double, " +
			"precio double, " +
			"taller text, " +
			"idTipoReparacion integer, " +
			"observaciones text, " +
			"idVehiculo integer);";
	final String CONSULTA_TODAS_REPARACIONES = "SELECT * FROM Reparaciones";
	final String ELIMINA_TABLA_REPARACIONES = "DROP TABLE IF EXISTS Reparaciones";

	
	public ReparacionesSQLiteHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREAR_TABLA_REPARACIONES);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Se elimina la versión anterior de la tabla
        db.execSQL(ELIMINA_TABLA_REPARACIONES);
        // Se crea la nueva versión de la tabla
        db.execSQL(CREAR_TABLA_REPARACIONES);
	}
	
	public ArrayList<DetalleReparacion> getReparaciones() {
		ArrayList<DetalleReparacion> lista = new ArrayList<DetalleReparacion>();
        SQLiteDatabase db = null;

		try {
			db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(CONSULTA_TODAS_REPARACIONES, null);
			if(cursor.moveToFirst()) {
				do {
					DetalleReparacion dm = new DetalleReparacion();
					dm.setIdDetalleReparacion(cursor.getInt(0));
					dm.setFecha(new Date(cursor.getLong(1)));
					dm.setKilometros(cursor.getDouble(2));
					dm.setPrecio(cursor.getDouble(3));
					dm.setTaller(cursor.getString(4));
					dm.setIdTipoReparacion(cursor.getInt(5));
					dm.setObservaciones(cursor.getString(6));
					dm.setIdVehiculo(cursor.getInt(7));
					
					lista.add(dm);
				}while(cursor.moveToNext());
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
            if(db != null) {
                db.close();
            }
        }
		return lista;
	}

    public boolean borrarReparacion(DetalleReparacion dr) {
        boolean resp = false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String[] argumentos = new String[]{String.valueOf(dr.getIdDetalleReparacion())};
            resp = (db.delete(Constantes.TABLA_REPARACIONES, "idReparacion = ?", argumentos) > 0);
        }catch(Exception ex) {
            ex.printStackTrace();
        }
    return resp;
}

    public boolean actualizarReparacion(DetalleReparacion dr) {
        boolean result = false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = creaContentValues(dr);
            String[] argumentos = new String[]{String.valueOf(dr.getIdVehiculo())};
            result = (db.update(Constantes.TABLA_REPARACIONES, values, "idVehiculo = ?", argumentos) > 0);
        }catch(Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public boolean insertarReparacion(DetalleReparacion dr) {
        boolean resp = false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = creaContentValues(dr);
            resp = (db.insert(Constantes.TABLA_REPARACIONES, null, values) > 0);
        }catch(Exception ex) {
            ex.printStackTrace();
        }
        return resp;
    }

    private ContentValues creaContentValues(DetalleReparacion dr) {
        ContentValues values = new ContentValues();
        values.put("idReparacion", dr.getIdDetalleReparacion());
        values.put("fecha", dr.getFecha().getTime());
        values.put("kms", dr.getKilometros());
        values.put("precio", dr.getPrecio());
        values.put("taller", dr.getTaller());
        values.put("idTipoReparacion", dr.getIdTipoReparacion());
        values.put("observaciones", dr.getObservaciones());
        values.put("idVehiculo", dr.getIdVehiculo());
        return values;
    }
}
