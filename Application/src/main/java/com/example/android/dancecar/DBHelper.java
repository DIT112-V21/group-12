package com.example.android.dancecar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;

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
    private static final String DATABASE_COL_NEWID = "dance_id";

    private static final String DATABASE_TABLE_3 = "chorMoves_table";
    private static final String DATABASE_COL_CHORMOVES_ID = "chor_id";
    private static final String DATABASE_COL_CHORMOVESNAME = "chor_name";
    private static final String DATABASE_COL_SET_OF_MOVES = "set_of_moves";


    private IndividualMove individualMove;
    private DancingActivity danceActivity;
    private Choreography choreography;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        SQLiteStatement moveData1 = db.compileStatement("CREATE TABLE "+DATABASE_TABLE_1 +" ("
                + DATABASE_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                + DATABASE_COL_DANCE_NAME +" VARCHAR );");

        SQLiteStatement individualMoveData2 = db.compileStatement("CREATE TABLE "+DATABASE_TABLE_2 +" ("
                + DATABASE_COL_INDIVIDUAL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                + DATABASE_COL_NEWID + " INTEGER, "
                + DATABASE_COL_INSTRUCTION +" VARCHAR, "
                + DATABASE_COL_ORDER +" INTEGER NOT NULL, "
                + DATABASE_COL_DURATION +" INTEGER NOT NULL, " +
                "FOREIGN KEY ("+ DATABASE_COL_NEWID +") REFERENCES "+ DATABASE_TABLE_1 + " ("+DATABASE_COL_ID+"));");

        SQLiteStatement chorMovesData = db.compileStatement("CREATE TABLE "+DATABASE_TABLE_3 +" ("
                + DATABASE_COL_CHORMOVES_ID + " INTEGER PRIMARY KEY NOT NULL, "
                + DATABASE_COL_ID + " INTEGER, "
                + DATABASE_COL_SET_OF_MOVES + " VARCHAR NOT NULL, "
                + DATABASE_COL_CHORMOVESNAME +" VARCHAR NOT NULL, " +
                "FOREIGN KEY ("+ DATABASE_COL_ID +") REFERENCES "+ DATABASE_TABLE_1 + " ("+DATABASE_COL_ID+"));");


        moveData1.execute();
        individualMoveData2.execute();
        chorMovesData.execute();
    }

    @Override
    // This method is called to check if the table exists already.
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_1);
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_2);
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_3);
        onCreate(db);
    }

    // This method is use to add new moves to our sqlite database, related to table 1.
    public void insertMove(String danceName) {

        // on below line we are creating a variable for our sqlite database and calling writable method
        // as we are writing data in our database.
        SQLiteDatabase db = getWritableDatabase();

        // on below line we are creating a variable for content values.
        ContentValues values = new ContentValues();

        // on below line we are passing all values along with its key and value pair.
        values.put(DATABASE_COL_DANCE_NAME, danceName);

        db.insert(DATABASE_TABLE_1, null, values);

        db.close();
    }
    // This method is used to add new individual move to our sqlite database, related to table 2.
    public void insertIndividualMove(String danceMoveName, String carInstruction, long individualDuration, int order) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues valuesIndividual = new ContentValues();

        int iD = getMoveId(danceMoveName);

        valuesIndividual.put(DATABASE_COL_INSTRUCTION, carInstruction);
        valuesIndividual.put(DATABASE_COL_DURATION, individualDuration);
        valuesIndividual.put(DATABASE_COL_ORDER, order);
        valuesIndividual.put(DATABASE_COL_NEWID, iD);

        System.out.println("Hi"); //test

        db.insert(DATABASE_TABLE_2, null, valuesIndividual);

        db.close();
    }
    // This method is used to add new choreography to our sqlite database, related to table 3.
    public void insertChorMove(ArrayList<DanceMove> fullChor, String chor_name) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues chorValues = new ContentValues();

        chorValues.put(DATABASE_COL_SET_OF_MOVES, String.valueOf(fullChor));
        chorValues.put(DATABASE_COL_CHORMOVESNAME, chor_name);

        db.insert(DATABASE_TABLE_3, null, chorValues);

        db.close();
    }

    public int getMoveId(String name){

        SQLiteDatabase db = getReadableDatabase();

        Cursor allIds = db.rawQuery("SELECT " + DATABASE_COL_ID + " FROM " + DATABASE_TABLE_1 + " WHERE " + DATABASE_COL_DANCE_NAME +"=?", new String[]{name});

        int iD = 0;

        while (allIds.moveToNext()){
            iD = allIds.getInt(0);

        }
        return iD;
    }
    }


