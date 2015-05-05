package com.jhonny.infocar.sql;

import java.util.ArrayList;
import java.util.Date;
import com.jhonny.infocar.Constantes;
import com.jhonny.infocar.model.DetalleVehiculo;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;


public class VehiculosSQLiteHelper extends SQLiteOpenHelper {

	final String CREAR_TABLA_VEHICULOS = "CREATE TABLE Vehiculos (" +
			"idVehiculo integer primary key autoincrement, " +
			"marca integer, " +
			"modelo text, " +
			"kms double, " +
			"fecha long, " +
			"matricula text, " +
			"tipoVehiculo integer, " +
			"tipoCarburante integer);";
	final String CONSULTA_TODOS_VEHICULOS = "SELECT * FROM Vehiculos";
	
	
	public VehiculosSQLiteHelper(Context context, String name, CursorFactory factory, int version) {
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
        // Se crea la nueva versión de la tabla
        db.execSQL(CREAR_TABLA_VEHICULOS);
	}
	
	public ArrayList<DetalleVehiculo> getVehiculos() {
		ArrayList<DetalleVehiculo> lista = new ArrayList<DetalleVehiculo>();
		try {
			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(CONSULTA_TODOS_VEHICULOS, null);
			if(cursor.moveToFirst()) {
				do {
					DetalleVehiculo dv = new DetalleVehiculo();
					dv.setIdVehiculo(cursor.getInt(0));
					dv.setMarca(cursor.getInt(1));
					dv.setModelo(cursor.getString(2));
					dv.setKilometros(cursor.getDouble(3));
					dv.setFechaCompra(new Date(cursor.getLong(4)));
					dv.setMatricula(cursor.getString(5));
					dv.setTipoVehiculo(cursor.getInt(6));
					dv.setTipoCarburante(cursor.getInt(7));
					lista.add(dv);
				}while(cursor.moveToNext());
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return lista;
	}

    public boolean borrarVehiculo(DetalleVehiculo dv) {
        boolean resp = false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String[] argumentos = new String[]{String.valueOf(dv.getIdVehiculo())};
            resp = (db.delete(Constantes.TABLA_VEHICULOS, "idVehiculo = ?", argumentos) > 0);

        }catch(Exception ex) {
            ex.printStackTrace();
        }
        return resp;
    }

    public boolean actualizarVehiculo(DetalleVehiculo dv) {
        boolean result = false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = creaContentValues(dv);
            String[] argumentos = new String[]{String.valueOf(dv.getIdVehiculo())};
            result = (db.update(Constantes.TABLA_VEHICULOS, values, "idVehiculo = ?", argumentos) > 0);
        }catch(Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public boolean insertarVehiculo(DetalleVehiculo dv) {
        boolean resp = false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = creaContentValues(dv);
            resp = (db.insert(Constantes.TABLA_VEHICULOS, null, values) > 0);
        }catch(Exception ex) {
            ex.printStackTrace();
        }
        return resp;
    }

    private ContentValues creaContentValues(DetalleVehiculo dv) {
        ContentValues values = new ContentValues();
        values.put("marca", dv.getMarca());
        values.put("modelo", dv.getModelo());
        values.put("kms", dv.getKilometros());
        values.put("fecha", dv.getFechaCompra().getTime());
        values.put("matricula", dv.getMatricula());
        values.put("tipoVehiculo", dv.getTipoVehiculo());
        values.put("tipoCarburante", dv.getTipoCarburante());
        return values;
    }
}
