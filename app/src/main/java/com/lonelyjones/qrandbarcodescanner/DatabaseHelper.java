package com.lonelyjones.qrandbarcodescanner;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "HistoryCodesDatabase";

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlQuery ="CREATE TABLE CodeItem (id INTEGER PRIMARY KEY AUTOINCREMENT, codeString TEXT, dateScanned TEXT, qr_image BLOB)";
        db.execSQL(sqlQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sqlQuery = "DROP TABLE IF EXISTS CodeItem";
        db.execSQL(sqlQuery);
        onCreate(db);
    }
}
