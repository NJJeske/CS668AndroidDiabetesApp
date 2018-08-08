package com.example.njjeske.cs668androiddiabetesapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;

import java.sql.SQLOutput;

public class DatabaseHelper extends SQLiteOpenHelper {


    private String password = "password";

    private static final String DATABASE_NAME = "ACTIVITY_DB";
    private static final String TABLE_NAME = "ACTIVITY_TABLE";
    private static final String colActivityId = "ACTIVITY_ID";
    private static final String colActivityType = "ACTIVITY_TYPE";
    private static final String colTime = "TIME";
    private static final String colDate = "DATE";
    private static final String colDescription = "DESCRIPTION";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

//        db.execSQL("CREATE TABLE " + TABLE_NAME + "("
////                + colActivityId + "INTEGER PRIMARY KEY AUTOINCREMENT,"
////                + colActivityType + "TEXT NOT NULL,"
////                + colDate + "TEXT NOT NULL,"
////                + colTime + "TEXT NOT NULL,"
////                + colDescription + "TEXT NOT NULL);");

        db.execSQL(" create table " + TABLE_NAME + " ("
                + colActivityId + " integer primary key autoincrement, "
                + colActivityType + " text, "
                + colDate + " text, "
                + colTime + " text, "
                + colDescription + " text);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertData(String activityType, String date, String time, String description) {
        SQLiteDatabase DB = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(colActivityType, activityType);
        contentValues.put(colDate, date);
        contentValues.put(colTime, time);
        contentValues.put(colDescription, description);
        DB.insert(TABLE_NAME, null, contentValues);


        System.out.println("ADDED TO DB");
        DB.close();
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
//
//    public Cursor getRegimenByDate(String date, String userName) {
//        Cursor result = null;
//        SQLiteDatabase db = getWritableDatabase(password);
//        result = db.rawQuery("Select * from " + Constants.TABLE_REGIMEN + " WHERE USERNAME='" + userName + "' AND DATE like '"+date+"%'", null);
//        System.out.println("Select * from " + Constants.TABLE_REGIMEN + " WHERE USERNAME='" + userName + "' AND DATE like '"+date+"%'");
//        return result;
//    }


//    public void addUser(User user) {
//        SQLiteDatabase db = getWritableDatabase(password);
//        ContentValues values = new ContentValues();
//        values.put(Constants.LOGIN_USERNAME, user.getUserName());
//        values.put(Constants.LOGIN_PASSWORD, user.getPassword());
//        db.insert(Constants.TABLE_LOGIN, null, values);
//    }


}
