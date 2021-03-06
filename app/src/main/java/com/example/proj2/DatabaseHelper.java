package com.example.proj2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "proj2_database";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.e("DATABASE", "creating");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS comment (id INTEGER PRIMARY KEY," +
                "post_id INTEGER, name TEXT, body TEXT, email TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS post (id INTEGER PRIMARY KEY, " +
                "title TEXT, body TEXT, user_id INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}