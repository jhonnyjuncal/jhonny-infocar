package com.jhonny.infocar.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DatosSQLiteHelper extends SQLiteOpenHelper {
	
	final String CREAR_TABLA_DATOS = "CREATE TABLE Datos (id integer primary key autoincrement, " +
			"nombre text, edad number, sexo boolean, telefono text);";
	
	
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
        db.execSQL("DROP TABLE IF EXISTS Datos");
        Log.d("DatosSQLiteHelper", "Se elimina la versión anterior de la tabla");
 
        // Se crea la nueva versión de la tabla
        db.execSQL(CREAR_TABLA_DATOS);
        Log.d("DatosSQLiteHelper", "Se crea la nueva versión de la tabla");
	}
}
