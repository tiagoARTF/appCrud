package com.example.appventas2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class ClsVentas extends SQLiteOpenHelper {
    String tblProducts = "CREATE TABLE Products(Referencia text primary key, Descripcion text,Costo interger,Existencia interger,Valoriva interger)";

    public ClsVentas(Context context,String name,SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(tblProducts);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE Products");
        db.execSQL(tblProducts);

    }
}

