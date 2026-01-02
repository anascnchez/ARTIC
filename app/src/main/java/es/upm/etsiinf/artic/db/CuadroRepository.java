package es.upm.etsiinf.artic.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import es.upm.etsiinf.artic.Cuadro;

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

    public List<Cuadro> getTodosLosCuadros() {
        List<Cuadro> cuadros = new ArrayList<>();
        Cursor cursor = db.query("cuadros", new String[]{"titulo", "imagen"},
                null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            // Como en la DB solo guardamos titulo e imagen,
            // usamos una URL base por defecto para el IIIF
            String titulo = cursor.getString(0);
            String imagenId = cursor.getString(1);
            String iiifUrl = "https://www.artic.edu/iiif/2"; // URL estándar del Art Institute of Chicago
            
            Cuadro cuadro = new Cuadro(null, titulo, imagenId, iiifUrl, null);
            cuadros.add(cuadro);
            cursor.moveToNext();
        }
        cursor.close();
        return cuadros;
    }
}
