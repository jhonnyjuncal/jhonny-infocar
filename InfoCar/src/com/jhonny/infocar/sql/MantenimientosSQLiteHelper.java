package com.jhonny.infocar.sql;

import java.util.ArrayList;
import java.util.Date;
import com.jhonny.infocar.model.DetalleMantenimiento;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;


public class MantenimientosSQLiteHelper extends SQLiteOpenHelper {
	
	final String CREAR_TABLA_MANTENIMIENTOS = "CREATE TABLE Mantenimientos (" +
			"idMantenimiento integer primary key autoincrement, " +
			"fecha long, " +
			"kms double, " +
			"precio double, " +
			"taller text, " +
			"tipoMantenimiento integer, " +
			"observaciones text, " +
			"idVehiculo integer);";
	final String CONSULTA_TODOS_MANTENIMIENTOS = "SELECT * FROM Mantenimientos";
	
	
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
        db.execSQL("DROP TABLE IF EXISTS Mantenimientos");
        Log.d("MantenimientosSQLiteHelper", "Se elimina la versión anterior de la tabla");
 
        // Se crea la nueva versión de la tabla
        db.execSQL(CREAR_TABLA_MANTENIMIENTOS);
        Log.d("MantenimientosSQLiteHelper", "Se crea la nueva versión de la tabla");
	}
	
	public ArrayList<DetalleMantenimiento> getMantenimientos() {
		ArrayList<DetalleMantenimiento> lista = new ArrayList<DetalleMantenimiento>();
		
		try {
			SQLiteDatabase db = this.getWritableDatabase();
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
		}
		
		return lista;
	}
}
