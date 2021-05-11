package com.example.android.dancecar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, "Userdata.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("create Table Newdancemoves(move_name TEXT, instructions TEXT primary key, duration INT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int oldVersion, int newVersion) {
        DB.execSQL("drop Table if exists Newdancemoves");
    }

    public boolean saveMove(String move_name, String instructions, int duration){
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("move_name", move_name);
        contentValues.put("instructions", instructions);
        contentValues.put("duration", duration);

        long results = DB.insert("Newdancemoves", null, contentValues);
        if(results == -1){
            return false;
        }else {
            return true;
        }
    }

    public Cursor getData(){
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from Newdancemoves", null);
        return cursor;
    }
}
