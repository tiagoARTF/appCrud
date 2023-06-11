package com.example.appventas2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.appventas2.DatabaseContract.UserEntry;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "appventas.db";
    private static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_USERS_TABLE =
            "CREATE TABLE " + UserEntry.TABLE_NAME + " (" +
                    UserEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    UserEntry.COLUMN_FULL_NAME + " TEXT," +
                    UserEntry.COLUMN_USERNAME + " TEXT," +
                    UserEntry.COLUMN_PASSWORD + " TEXT," +
                    UserEntry.COLUMN_KEYWORD + " TEXT)";

    private static final String SQL_DELETE_USERS_TABLE =
            "DROP TABLE IF EXISTS " + UserEntry.TABLE_NAME;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_USERS_TABLE);
        onCreate(db);
    }


}
