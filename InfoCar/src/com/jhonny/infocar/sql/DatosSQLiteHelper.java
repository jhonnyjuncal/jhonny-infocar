package com.jhonny.infocar.sql;

import java.util.Date;
import com.jhonny.infocar.model.DetalleDatos;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DatosSQLiteHelper extends SQLiteOpenHelper {
	
	final String CREAR_TABLA_DATOS = "CREATE TABLE Datos (" +
			"idDetalleDatos integer primary key autoincrement, " +
			"nombre text, " +
			"telefono text, " +
			"edad number, " +
			"hombre boolean, " +
			"email text, " +
			"fecha long);";
	final String BORRA_TABLA_DATOS = "DROP TABLE IF EXISTS Datos";
	final String CONSULTA_DATOS = "SELECT * FROM Datos";
	
	
	public DatosSQLiteHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREAR_TABLA_DATOS);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Se elimina la versión anterior de la tabla
        db.execSQL(BORRA_TABLA_DATOS);
        Log.d("DatosSQLiteHelper", "Se elimina la versión anterior de la tabla");
 
        // Se crea la nueva versión de la tabla
        db.execSQL(CREAR_TABLA_DATOS);
        Log.d("DatosSQLiteHelper", "Se crea la nueva versión de la tabla");
	}
	
	public DetalleDatos getDatos() {
		DetalleDatos dd = null;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(CONSULTA_DATOS, null);
		
		if(cursor.moveToFirst()) {
			do {
				dd = new DetalleDatos();
				dd.setIdDetalleDatos(cursor.getInt(0));
				dd.setNombre(cursor.getString(1));
				dd.setTelefono(cursor.getString(2));
				dd.setEdad(cursor.getInt(3));
				dd.setHombre(Boolean.valueOf(cursor.getString(4)));
				dd.setEmail(cursor.getString(5));
				dd.setFechaAlta(new Date(cursor.getLong(6)));
				
			}while(cursor.moveToNext());
		}
		return dd;
	}
}
