package com.jhonny.infocar.sql;

import java.util.ArrayList;
import java.util.Date;
import com.jhonny.infocar.Constantes;
import com.jhonny.infocar.model.DetalleMantenimiento;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;


public class MantenimientosSQLiteHelper extends SQLiteOpenHelper {
	
	final String CREAR_TABLA_MANTENIMIENTOS = "CREATE TABLE Mantenimientos (" +
			"idMantenimiento integer primary key autoincrement, " +
			"fecha long, " +
			"kms double, " +
			"precio double, " +
			"taller text, " +
			"idTipoMantenimiento integer, " +
			"observaciones text, " +
			"idVehiculo integer);";
	final String CONSULTA_TODOS_MANTENIMIENTOS = "SELECT * FROM Mantenimientos";
	final String ELIMINA_TABLA_MANTENIMIENTOS = "DROP TABLE IF EXISTS Mantenimientos";


	public MantenimientosSQLiteHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREAR_TABLA_MANTENIMIENTOS);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Se elimina la versión anterior de la tabla
        db.execSQL(ELIMINA_TABLA_MANTENIMIENTOS);
        // Se crea la nueva versión de la tabla
        db.execSQL(CREAR_TABLA_MANTENIMIENTOS);
	}
	
	public ArrayList<DetalleMantenimiento> getMantenimientos() {
		ArrayList<DetalleMantenimiento> lista = new ArrayList<DetalleMantenimiento>();
        SQLiteDatabase db = null;

        try {
			db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(CONSULTA_TODOS_MANTENIMIENTOS, null);
			if(cursor.moveToFirst()) {
				do {
					DetalleMantenimiento dm = new DetalleMantenimiento();
					dm.setIdDetalleMantenimiento(cursor.getInt(0));
					dm.setFecha(new Date(cursor.getLong(1)));
					dm.setKilometros(cursor.getDouble(2));
					dm.setPrecio(cursor.getDouble(3));
					dm.setTaller(cursor.getString(4));
					dm.setTipoMantenimiento(cursor.getInt(5));
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

    public boolean borrarMantenimiento(DetalleMantenimiento dm) {
        boolean resp = false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String[] argumentos = new String[]{String.valueOf(dm.getIdDetalleMantenimiento())};
            resp = (db.delete(Constantes.TABLA_MANTENIMIENTOS, "idMantenimiento = ?", argumentos) > 0);
        }catch(Exception ex) {
            ex.printStackTrace();
        }
        return resp;
    }

    public boolean actualizarMantenimiento(DetalleMantenimiento dm) {
        boolean result = false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = creaContentValues(dm);
            String[] argumentos = new String[]{String.valueOf(dm.getIdDetalleMantenimiento())};
            result = (db.update(Constantes.TABLA_MANTENIMIENTOS, values, "idMantenimiento = ?", argumentos) > 0);
        }catch(Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public boolean insertarMantenimiento(DetalleMantenimiento dm) {
        boolean resp = false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = creaContentValues(dm);
            resp = (db.insert(Constantes.TABLA_MANTENIMIENTOS, null, values) > 0);
        }catch(Exception ex) {
            ex.printStackTrace();
        }
        return resp;
    }

    private ContentValues creaContentValues(DetalleMantenimiento dm) {
        ContentValues values = new ContentValues();
        values.put("idMantenimiento", dm.getIdDetalleMantenimiento());
        values.put("fecha", dm.getFecha().getTime());
        values.put("kms", dm.getKilometros());
        values.put("precio", dm.getPrecio());
        values.put("taller", dm.getTaller());
        values.put("idTipoMantenimiento", dm.getTipoMantenimiento());
        values.put("observaciones", dm.getObservaciones());
        values.put("idVehiculo", dm.getIdVehiculo());
        return values;
    }
}
