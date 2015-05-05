package com.jhonny.infocar.sql;

import java.util.Date;
import com.jhonny.infocar.Constantes;
import com.jhonny.infocar.model.DetalleDatos;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;


public class DatosSQLiteHelper extends SQLiteOpenHelper {
	
	final String CREAR_TABLA_DATOS = "CREATE TABLE Datos (" +
			"idDetalleDatos integer primary key autoincrement, " +
			"nombre text, " +
			"telefono text, " +
			"edad integer, " +
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
        // Se crea la nueva versión de la tabla
        db.execSQL(CREAR_TABLA_DATOS);
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
                if(cursor.getInt(4) == 1)
				    dd.setHombre(true);
                else
                    dd.setHombre(false);
				dd.setEmail(cursor.getString(5));
				dd.setFechaAlta(new Date(cursor.getLong(6)));
				
			}while(cursor.moveToNext());
		}
		return dd;
	}

    public boolean borrarDatos(DetalleDatos dd) {
        boolean resp = false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String[] argumentos = new String[]{String.valueOf(dd.getIdDetalleDatos())};
            resp = (db.delete(Constantes.TABLA_DATOS, "idDetalleDatos = ?", argumentos) > 0);
        }catch(Exception ex) {
            ex.printStackTrace();
        }
        return resp;
    }

    public boolean actualizarDatos(DetalleDatos dd) {
        boolean result = false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = creaContentValues(dd);
            String[] argumentos = new String[]{String.valueOf(dd.getIdDetalleDatos())};
            result = (db.update(Constantes.TABLA_DATOS, values, "idDetalleDatos = ?", argumentos) > 0);
        }catch(Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public boolean insertarDatos(DetalleDatos dd) {
        boolean resp = false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = creaContentValues(dd);
            resp = (db.insert(Constantes.TABLA_DATOS, null, values) > 0);
        }catch(Exception ex) {
            ex.printStackTrace();
        }
        return resp;
    }

    private ContentValues creaContentValues(DetalleDatos dd) {
        ContentValues values = new ContentValues();
        values.put("idDetalleDatos", dd.getIdDetalleDatos());
        values.put("nombre", dd.getNombre());
        values.put("telefono", dd.getTelefono());
        values.put("edad", dd.getEdad());
        values.put("hombre", dd.isHombre());
        values.put("email", dd.getEmail());
        values.put("fecha", dd.getFechaAlta().getTime());
        return values;
    }
}
