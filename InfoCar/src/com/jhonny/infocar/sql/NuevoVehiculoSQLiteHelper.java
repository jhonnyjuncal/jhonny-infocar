package com.jhonny.infocar.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class NuevoVehiculoSQLiteHelper extends SQLiteOpenHelper {

	final String CREAR_TABLA_VEHICULOS = "CREATE TABLE Vehiculos (id integer primary key autoincrement, " +
			"marca integer, modelo text, kms double, fecha date, tipoVehiculo integer, tipoCarburante integer);";
	
	
	public NuevoVehiculoSQLiteHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREAR_TABLA_VEHICULOS);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Se elimina la versión anterior de la tabla
        db.execSQL("DROP TABLE IF EXISTS Vehiculos");
        Log.d("NuevoVehiculoSQLiteHelper", "Se elimina la versión anterior de la tabla");
 
        // Se crea la nueva versión de la tabla
        db.execSQL(CREAR_TABLA_VEHICULOS);
        Log.d("NuevoVehiculoSQLiteHelper", "Se crea la nueva versión de la tabla");
	}
}
