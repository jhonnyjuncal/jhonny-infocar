package com.jhonny.infocar.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.jhonny.infocar.Constantes;
import com.jhonny.infocar.model.DetalleFichaTecnica;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by jhonny on 08/08/2015.
 */
public class FichaTecnicaSQLiteHelper extends SQLiteOpenHelper {

    final String CREAR_TABLA_FICHA_TECNICA = "CREATE TABLE FichaTecnica (" +
            "idFichaTecnica integer primary key autoincrement, " +
            "idVehiculo integer, " +
            "fechaMatriculacion long, " +
            "lugar text, " +
            "neumaticos text, " +
            "potencia boolean, " +
            "bastidor text);";
    final String BORRA_TABLA_FICHA_TECNICA = "DROP TABLE IF EXISTS FichaTecnica";
    final String CONSULTA_FICHAS_TECNICAS = "SELECT * FROM FichaTecnica";

    public FichaTecnicaSQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREAR_TABLA_FICHA_TECNICA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Se elimina la versión anterior de la tabla
        db.execSQL(BORRA_TABLA_FICHA_TECNICA);
        // Se crea la nueva versión de la tabla
        db.execSQL(CREAR_TABLA_FICHA_TECNICA);
    }

    public ArrayList<DetalleFichaTecnica> getFichasTecnicas() {
        ArrayList<DetalleFichaTecnica> lista = new ArrayList<DetalleFichaTecnica>();
        SQLiteDatabase db = null;

        try {
            db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(CONSULTA_FICHAS_TECNICAS, null);
            if(cursor.moveToFirst()) {
                do {
                    DetalleFichaTecnica dft = new DetalleFichaTecnica();
                    dft.setIdFichaTecnica(cursor.getInt(0));
                    dft.setIdVehiculo(cursor.getInt(1));
                    dft.setFechaMatriculacion(new Date(cursor.getLong(2)));
                    dft.setLugar(cursor.getString(3));
                    dft.setNeumaticos(cursor.getString(4));
                    dft.setPotencia(cursor.getString(5));
                    dft.setBastidor(cursor.getString(6));

                    lista.add(dft);
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

    public boolean guardarDatosFichaTecnica(DetalleFichaTecnica dft) {
        boolean resp = false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = creaContentValues(dft);
            if(dft.getIdFichaTecnica() == null) {
                resp = (db.insert(Constantes.TABLA_FICHA_TECNICA, null, values) > 0);
            }else {
                String[] argumentos = new String[]{dft.getIdFichaTecnica().toString()};
                resp = (db.update(Constantes.TABLA_FICHA_TECNICA, values, "idFichaTecnica = ?", argumentos) > 0);
            }
        }catch(Exception ex) {
            ex.printStackTrace();
        }
        return resp;
    }

    public boolean borrarDatosFichaTecnica(DetalleFichaTecnica dft) {
        boolean resp = false;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String[] argumentos = new String[]{String.valueOf(dft.getIdFichaTecnica())};
            resp = (db.delete(Constantes.TABLA_FICHA_TECNICA, "idFichaTecnica = ?", argumentos) > 0);
        }catch(Exception ex) {
            ex.printStackTrace();
        }
        return resp;
    }

    private ContentValues creaContentValues(DetalleFichaTecnica dft) {
        ContentValues values = new ContentValues();
        values.put("idFichaTecnica", dft.getIdFichaTecnica());
        values.put("idVehiculo", dft.getIdVehiculo());
        values.put("fechaMatriculacion", dft.getFechaMatriculacion().getTime());
        values.put("lugar", dft.getLugar());
        values.put("neumaticos", dft.getNeumaticos());
        values.put("potencia", dft.getPotencia());
        values.put("bastidor", dft.getBastidor());
        return values;
    }
}
