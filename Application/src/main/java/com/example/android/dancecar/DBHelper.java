package com.example.android.dancecar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/* public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, "Userdata.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("create Table Newdancemoves(move_name TEXT, instruction TEXT primary key, duration INT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int oldVersion, int newVersion) {
        DB.execSQL("drop Table if exists Newdancemoves");
    }

    public boolean saveMove(String move_name, String instruction, int duration){
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("move_name", move_name);
        contentValues.put("instruction", instruction);
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

 */

import android.annotation.SuppressLint;
import android.database.sqlite.SQLiteStatement;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;

//Source code : https://www.geeksforgeeks.org/how-to-create-and-add-data-to-sqlite-database-in-android/
//Source code : https://github.com/DIT112-V20/group-04/blob/master/app/src/main/java/se/healthrover/conectivity/SqlHelper.java


public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "dancemoves.sqlite";
    /*
    private static final String DATABASE_TABLE_1 = "individualMove_table";
    private static final String DATABASE_COL_INDIVIDUAL_ID = "individual_id";
    private static final String DATABASE_COL_NAME = "name";
    private static final String DATABASE_COL_INSTRUCTION = "instruction";
    private static final String DATABASE_COL_DURATION = "duration";
     */
    private static final String DATABASE_TABLE_2 = "dancemoves_table";
    private static final String DATABASE_COL_ID = "id";
    private static final String DATABASE_COL_DANCE_NAME = "dance_name";
    //private static final String DATABASE_COL_MOVES = "move";
    private static final String  DATABASE_COL_INSTRUCTIONS = "instructions";

    private SQLiteDatabase database;

    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(SQLiteDatabase db) {
        //SQLiteStatement stmt = db.compileStatement("CREATE TABLE "+DATABASE_TABLE_1 +" ("+ DATABASE_COL_INDIVIDUAL_ID + " INTEGER PRIMARY KEY, " + DATABASE_COL_NAME +" VARCHAR, "+ DATABASE_COL_INSTRUCTION +" VARCHAR, "+ DATABASE_COL_DURATION+" INTEGER);");
        SQLiteStatement moveData = db.compileStatement("CREATE TABLE "+DATABASE_TABLE_2 +" ("+ DATABASE_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + DATABASE_COL_DANCE_NAME +" VARCHAR, " + DATABASE_COL_INSTRUCTIONS + " VARCHAR);");

        moveData.execute();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // this method is called to check if the table exists already.
        SQLiteStatement moveData = db.compileStatement("DROP TABLE IF EXISTS " + DATABASE_TABLE_2 + ";");
        moveData.execute();
        onCreate(db);
    }

    /*
    // Creates a list of instances of move objects for
    // all available dance moves from the database
    public List<CreatedDanceMove> getSavedMoves(){
        database = this.getReadableDatabase();
        ArrayList<CreatedDanceMove> createdDanceMoves = ObjectFactory.getInstance().createList();
        @SuppressLint("Recycle") Cursor cursor = database.rawQuery("Select * from " + DATABASE_TABLE_NAME + ";", null);
        if (cursor.getCount() == 0){
            return null;
        }
        else {
            while (cursor.moveToNext()){
                CreatedDanceMove createdDanceMove = ObjectFactory.getInstance().makeCar( cursor.getString(1),cursor.getString(2));
                createdDanceMove.setLocalDomainName(cursor.getString(0));
                createdDanceMove.add(createdDanceMove);
            }
            return createdDanceMoves;
        }
    }
     */

    /*
    public void insertData(CreatedDanceMove createdDanceMove){
        database = this.getWritableDatabase();
        database.beginTransaction();
        SQLiteStatement statement = database.compileStatement("INSERT INTO "+ DATABASE_TABLE_2 +" ("+ DATABASE_COL_ID + ", " + DATABASE_COL_DANCE_NAME + ") VALUES (?, ?)");
        statement.clearBindings();
        statement.bindString(1,createdDanceMove.newDanceName);
        //statement.bindString(2, String.valueOf(createdDanceMove.individualMoves));
        statement.executeInsert();
        database.setTransactionSuccessful();
        database.endTransaction();
    }
     */



    // this method is use to add new course to our sqlite database.
    public void insertData(String danceName, ArrayList<IndividualMove> individualMoves) {

        // on below line we are creating a variable for
        // our sqlite database and calling writable method
        // as we are writing data in our database.
        SQLiteDatabase db = this.getWritableDatabase();

        // on below line we are creating a
        // variable for content values.
        ContentValues values = new ContentValues();

        // on below line we are passing all values
        // along with its key and value pair.
        values.put(DATABASE_COL_DANCE_NAME, danceName);
        values.put(DATABASE_COL_INSTRUCTIONS, String.valueOf(individualMoves));
        System.out.println("Hi");



        // after adding all values we are passing
        // content values to our table.
        db.insert(DATABASE_TABLE_2, null, values);

        // at last we are closing our
        // database after adding database.
        db.close();
    }

/*
    // This method will return a move by given name, if and only
    // if there's only one instance of that move name in the database
    public CreatedDanceMove getMoveByName(String name){
        database = this.getReadableDatabase();
        //ArrayList<CreatedDanceMove> createdDanceMoves = ObjectFactory.getInstance().createList();
        @SuppressLint("Recycle") Cursor cursor = database.rawQuery("Select * from " + DATABASE_TABLE_2 + " WHERE "+ DATABASE_COL_DANCE_NAME +"=?", new String[]{name});
        if (cursor.getCount() == 0){
            return null;
        }
        else {
            while (cursor.moveToNext()){
                CreatedDanceMove createdDanceMove = CreatedDanceMove.getInstance().makeCar( cursor.getString(1),cursor.getString(2));
                createdDanceMove.setLocalDomainName(cursor.getString(0));
                createdDanceMove.add(createdDanceMove);
            }
            if (createdDanceMoves.size()!=1){
                return null;
            }
            else {
                return createdDanceMoves.get(0);
            }
        }
    }

    // This method currently hardCodes the database to work with only one SmartCar with local domain name
    public void insertIntoDataBase() {
        CreatedDanceMove newMove = ObjectFactory.getInstance().makeCar("", CAR_NAME);
        newMove.setLocalDomainName(LOCAL_DOMAIN_NAME);
        insertData(newMove);
    }
 */

}