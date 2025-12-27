package es.upm.etsiinf.artic.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class CuadroRepository {

    private SQLiteDatabase db;
    private MySQLiteHelper dbHelper;

    public CuadroRepository(Context context){
        dbHelper = new MySQLiteHelper(context);
    }

    public void open(){
        db = dbHelper.getWritableDatabase();
    }

    public void close(){
        dbHelper.close();
    }

    public void insertarCuadro(String titulo, String imagen){
        ContentValues values = new ContentValues();
        values.put("titulo", titulo);
        values.put("imagen", imagen);
        db.insert("cuadros", null, values);
    }
}
