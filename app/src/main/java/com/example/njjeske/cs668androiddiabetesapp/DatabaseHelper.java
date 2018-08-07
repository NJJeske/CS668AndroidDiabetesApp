package com.example.njjeske.cs668androiddiabetesapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ACTIVITY_DB";
    private static final String TABLE_NAME = "ACTIVITY_TABLE";
    private static final String colActivityId = "ACTIVITY_ID";
    private static final String colActivityType = "ACTIVITY_TYPE";
    private static final String colTime = "TIME";
    private static final String colAmount = "AMOUNT";
    private static final String colName = "NAME";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + TABLE_NAME + "("
                + colActivityId + "INTEGER PRIMARY KEY AUTOINCREMENT,"
                + colActivityType + "TEXT NOT NULL,"
                + colTime + "TEXT NOT NULL,"
                + colAmount + "INTEGER NOT NULL,"
                + colName + "TEXT NOT NULL);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String activityType, String time, String amount, String name) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(colActivityType, activityType);
        contentValues.put(colTime, time);
        contentValues.put(colAmount, amount);
        contentValues.put(colName, name);
        long result = DB.insert(TABLE_NAME, null, contentValues);

        return result != -1;
    }


    public Cursor getAllData() {

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor result = db.rawQuery("SELECT * from " + TABLE_NAME + " order by " + colTime, null);
        return result;
    }

    public Cursor getRowByID(int id) {

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor result = db.rawQuery("select * from " + TABLE_NAME + " where " + colActivityId + " = " + id, null);
        return result;
    }

    public boolean deleteRowByID(int id) {

        SQLiteDatabase db = this.getWritableDatabase();

        int result = db.delete(TABLE_NAME, colActivityId + "=" + id, null);
        if (result == 0)
            return false;
        return true;
    }


}
