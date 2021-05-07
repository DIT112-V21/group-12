package com.example.android.dancecar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper  extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, "Userdata.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("create Table Newdancemoves(move_name TEXT primary key, direction TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int oldVersion, int newVersion) {
        DB.execSQL("drop Table if exists Newdancemoves");
    }

    public boolean insertMoveData(String move_name, String direction){
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("move_name", move_name);
        contentValues.put("direction", direction);
        long results = DB.insert("Newdancemoves", null, contentValues);
        if(results == -1){
            return false;
        }else {
            return true;
        }
    }

    public boolean updateMoveData(String move_name, String direction){
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("direction", direction);

        Cursor cursor = DB.rawQuery("Select * from Newdancemoves where move_name = ?", new String[] {move_name});
        if(cursor.getCount() > 0) {

            long results = DB.update("Newdancemoves", contentValues, "move_name=?", new String[]{move_name});
            if (results == -1) {
                return false;
            } else {
                return true;
            }
        }else {
            return false;
        }
    }

    public boolean deleteData(String move_name){
        SQLiteDatabase DB = this.getWritableDatabase();

        Cursor cursor = DB.rawQuery("Select * from Newdancemoves where move_name = ?", new String[] {move_name});
        if(cursor.getCount() > 0) {

            long results = DB.delete("Newdancemoves","move_name=?", new String[]{move_name});
            if (results == -1) {
                return false;
            } else {
                return true;
            }
        }else {
            return false;
        }
    }

    public Cursor getData(){
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from Newdancemoves", null);

        return cursor;
    }

}
