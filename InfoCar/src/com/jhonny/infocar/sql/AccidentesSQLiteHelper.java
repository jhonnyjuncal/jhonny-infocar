package com.jhonny.infocar.sql;

import java.util.ArrayList;
import java.util.Date;
import com.jhonny.infocar.Constantes;
import com.jhonny.infocar.model.DetalleAccidente;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;


public class AccidentesSQLiteHelper extends SQLiteOpenHelper {
	
	final String CREAR_TABLA_ACCIDENTES = "CREATE TABLE Accidentes (" +
			"idDetalleAccidente integer primary key autoincrement, " +
			"fecha Long, " +
			"lugar text, " +
			"kilometros double, " +
			"observaciones text, " +
			"idVehiculo integer);";
	final String BORRA_TABLA_ACCIDENTES = "DROP TABLE IF EXISTS Accidentes";
	final String CONSULTA_TODOS_ACCIDENTES = "SELECT * FROM Accidentes";

	
	public AccidentesSQLiteHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREAR_TABLA_ACCIDENTES);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Se elimina la versión anterior de la tabla
        db.execSQL(BORRA_TABLA_ACCIDENTES);
        // Se crea la nueva versión de la tabla
        db.execSQL(CREAR_TABLA_ACCIDENTES);
	}
	
	public ArrayList<DetalleAccidente> getAccidentes() {
		ArrayList<DetalleAccidente> lista = new ArrayList<DetalleAccidente>();
		try {
			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(CONSULTA_TODOS_ACCIDENTES, null);
			
			if(cursor.moveToFirst()) {
				do {
					DetalleAccidente da = new DetalleAccidente();
					da.setIdDetalleAccidente(cursor.getInt(0));
					da.setFecha(new Date(cursor.getLong(1)));
					da.setLugar(cursor.getString(2));
					da.setKilometros(cursor.getDouble(3));
					da.setObservaciones(cursor.getString(4));
					da.setIdVehiculo(cursor.getInt(5));
					lista.add(da);
				}while(cursor.moveToNext());
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return lista;
	}

    public boolean borrarAccidente(DetalleAccidente da) {
        boolean resp = false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String[] argumentos = new String[]{String.valueOf(da.getIdDetalleAccidente())};
            resp = (db.delete(Constantes.TABLA_ACCIDENTES, "idDetalleAccidente = ?", argumentos) > 0);
        }catch(Exception ex) {
            ex.printStackTrace();
        }
        return resp;
    }

    public boolean actualizarAccidente(DetalleAccidente da) {
        boolean result = false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = creaContentValues(da);
            String[] argumentos = new String[]{String.valueOf(da.getIdDetalleAccidente())};
            result = (db.update(Constantes.TABLA_ACCIDENTES, values, "idDetalleAccidente = ?", argumentos) > 0);
        }catch(Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public boolean insertarAccidente(DetalleAccidente da) {
        boolean resp = false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = creaContentValues(da);
            resp = (db.insert(Constantes.TABLA_ACCIDENTES, null, values) > 0);
        }catch(Exception ex) {
            ex.printStackTrace();
        }
        return resp;
    }

    private ContentValues creaContentValues(DetalleAccidente da) {
        ContentValues values = new ContentValues();
        values.put("idDetalleAccidente", da.getIdDetalleAccidente());
        values.put("fecha", da.getFecha().getTime());
        values.put("lugar", da.getLugar());
        values.put("kilometros", da.getKilometros());
        values.put("observaciones", da.getObservaciones());
        values.put("idVehiculo", da.getIdVehiculo());
        return values;
    }
}
