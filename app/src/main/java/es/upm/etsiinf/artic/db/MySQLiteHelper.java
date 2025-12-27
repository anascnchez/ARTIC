package es.upm.etsiinf.artic.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteHelper extends SQLiteOpenHelper {

    public MySQLiteHelper(Context context){
        super(context, "cuadros", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE cuadros (_id INTEGER PRIMARY KEY AUTOINCREMENT, titulo TEXT NOT NULL, imagen TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + "cuadros");
        onCreate(db);
    }

}
