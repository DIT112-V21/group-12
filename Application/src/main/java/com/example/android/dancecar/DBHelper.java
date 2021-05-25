package com.example.android.dancecar;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

//Source code : https://www.geeksforgeeks.org/how-to-create-and-add-data-to-sqlite-database-in-android/
//Source code : https://github.com/DIT112-V20/group-04/blob/master/app/src/main/java/se/healthrover/conectivity/SqlHelper.java

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "dancemoves.sqlite";

    private static final String DATABASE_TABLE_1 = "dancemoves_table";
    private static final String DATABASE_COL_ID = "id";
    private static final String DATABASE_COL_DANCE_NAME = "dance_name";

    private static final String DATABASE_TABLE_2 = "individualMove_table";
    private static final String DATABASE_COL_INDIVIDUAL_ID = "individual_id";
    private static final String DATABASE_COL_INSTRUCTION = "instruction";
    private static final String DATABASE_COL_DURATION = "individual_duration";
    private static final String DATABASE_COL_ORDER = "instruction_order";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        SQLiteStatement moveData1 = db.compileStatement("CREATE TABLE "+DATABASE_TABLE_1 +" ("
                + DATABASE_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                + DATABASE_COL_DANCE_NAME +" VARCHAR );");


        SQLiteStatement individualMoveData2 = db.compileStatement("CREATE TABLE "+DATABASE_TABLE_2 +" ("
                + DATABASE_COL_INDIVIDUAL_ID + " INTEGER PRIMARY KEY NOT NULL, "
                + DATABASE_COL_INSTRUCTION +" VARCHAR, "
                + DATABASE_COL_DURATION +" INTEGER NOT NULL, "
                + DATABASE_COL_ORDER +" INTEGER NOT NULL, "
                + DATABASE_COL_ID + " INTEGER, " +
                "FOREIGN KEY ("+ DATABASE_COL_ID +") REFERENCES "+ DATABASE_TABLE_1 + " ("+DATABASE_COL_ID+"));");

        moveData1.execute();
        individualMoveData2.execute();
    }

    @Override
    // this method is called to check if the table exists already.
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_1);
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_2);

        onCreate(db);
    }

    // this method is use to add new move to our sqlite database.
    public void insertMove(String danceName) {

        // on below line we are creating a variable for our sqlite database and calling writable method
        // as we are writing data in our database.
        SQLiteDatabase db = getWritableDatabase();

        // on below line we are creating a variable for content values.
        ContentValues values = new ContentValues();
        // on below line we are passing all values along with its key and value pair.
        //table 1
        values.put(DATABASE_COL_DANCE_NAME, danceName);

        System.out.println("Hi"); //test

        db.insert(DATABASE_TABLE_1, null, values);

        db.close();
    }

    public void insertIndividualMove(String danceMoveName, String carInstruction, long individualDuration, int order) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues valuesIndividual = new ContentValues();

        getMoveId(danceMoveName);
        valuesIndividual.put(DATABASE_COL_INSTRUCTION, carInstruction);
        valuesIndividual.put(DATABASE_COL_DURATION, individualDuration);
        valuesIndividual.put(DATABASE_COL_ORDER, order);

        System.out.println("Hi"); //test

        db.insert(DATABASE_TABLE_2, null, valuesIndividual);

        db.close();
    }

    public void getMoveId(String name){
        //SELECT ID FROM TABLE1 WHERE NAME = ...
    }
}