package com.example.njjeske.cs668androiddiabetesapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;

import java.sql.SQLOutput;

public class DatabaseHelper extends SQLiteOpenHelper {


    private String password = "password";

    private static final String TABLE_NAME_LOGIN = "LOGIN_DB";
    private static final String colName = "NAME";
    private static final String colPassword = "PASSWORD";

    private static final String DATABASE_NAME = "ACTIVITY_DB";
    private static final String TABLE_NAME_ACT = "ACTIVITY_TABLE";
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


        db.execSQL(" create table " + TABLE_NAME_LOGIN + " ("
                + colName + " text primary key, "
                + colPassword + " text);");

        db.execSQL(" create table " + TABLE_NAME_ACT + " ("
                + colActivityId + " integer primary key autoincrement, "
                + colActivityType + " text, "
                + colDate + " text, "
                + colTime + " text, "
                + colDescription + " text);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_LOGIN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_ACT);
        onCreate(db);
    }

    public void insertData(String activityType, String date, String time, String description) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(colActivityType, activityType);
        contentValues.put(colDate, date);
        contentValues.put(colTime, time);
        contentValues.put(colDescription, description);
        db.insert(TABLE_NAME_ACT, null, contentValues);

        db.close();
    }

//    public void update(int ID, IDatabaseObject object) {
//
//        SQLiteDatabase db = getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        Activity act = (Activity) object;
//
//
//        contentValues.put(colActivityType, activityType);
//        contentValues.put(colDate, date);
//        contentValues.put(colTime, time);
//        contentValues.put(colDescription, description);
//        db.update(TABLE_NAME_ACT, contentValues, colActivityId + " = ? ", new String[]{Integer.toString(id)});
//
//        db.close();
//
//
//    }


    public Cursor getAllData() {

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor result = db.rawQuery("SELECT * from " + TABLE_NAME_ACT, null);
        return result;
    }

    public Cursor getRowByID(int id) {

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor result = db.rawQuery("select * from " + TABLE_NAME_ACT + " where " + colActivityId + " = " + id, null);
        return result;
    }

    public boolean deleteRowByID(int id) {

        SQLiteDatabase db = this.getWritableDatabase();

        int result = db.delete(TABLE_NAME_ACT, colActivityId + "=" + id, null);
        if (result == 0)
            return false;
        return true;


    }

    public void addUser(User user) {
        // Will need password in db somehow
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(colName, user.getName());
        values.put(colPassword, user.getPassword());
        db.insert(TABLE_NAME_LOGIN, null, values);
        db.close();
    }

    public boolean isUserRegistered(String userName) {
        boolean registered = false;
        Cursor result;
        User userFound = new User();
        // Will need password in db somehow
        SQLiteDatabase db = getWritableDatabase();
        result = db.rawQuery("Select * from " + TABLE_NAME_LOGIN + " WHERE " + colName + " = '" + userName + "'", null);
        if (result.getCount() == 0) {
            System.out.println("User not found!");
            registered = false;
        } else {
            registered = true;
            while (result.moveToNext()) {
                userFound.setName(result.getString(0));
                userFound.setPassword(result.getString(1));
            }
        }
        result.close();
        return registered;
    }







}
