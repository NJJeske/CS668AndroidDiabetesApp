package com.example.njjeske.cs668androiddiabetesapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;

import java.sql.SQLOutput;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {


    private String password = "password";

    private static final String TABLE_NAME_LOGIN = "LOGIN_DB";
    private static final String colName = "NAME";
    private static final String colPassword = "PASSWORD";

    private static final String DATABASE_NAME = "DIABETES_DB";
    private static final String TABLE_NAME_ACT = "ACTIVITY_TABLE";
    private static final String colActivityId = "_id";
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

    public void updateActivity(int id, Activity object) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        Activity act = object;

        contentValues.put(colActivityType, act.getActivityType());
        contentValues.put(colDate, act.getDate());
        contentValues.put(colTime, act.getTime());
        contentValues.put(colDescription, act.getDescription());
        db.update(TABLE_NAME_ACT, contentValues, colActivityId + " = ? ", new String[]{Integer.toString(id)});

        db.close();


    }

    public Cursor getAllData() {

        SQLiteDatabase db = this.getWritableDatabase();

//        Cursor output = db.rawQuery("SELECT * from " + TABLE_NAME_ACT, null);
        // updated to return all results automatically in chronological desc order
        Cursor output = db.rawQuery("SELECT * from " + TABLE_NAME_ACT + " order by " + colDate + " desc, " + colTime + " desc", null);
        return output;
    }

    public Cursor getDataByActivity(String activity) {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor output = null;

        switch (activity) {
            case "Blood Glucose":
                output = db.rawQuery("SELECT * from " + TABLE_NAME_ACT + " WHERE ACTIVITY_TYPE='" + activity + "'", null);
                break;
            case "Diet":
                output = db.rawQuery("SELECT * from " + TABLE_NAME_ACT + " WHERE ACTIVITY_TYPE='" + activity + "'", null);
                break;
            case "Exercise":
                output = db.rawQuery("SELECT * from " + TABLE_NAME_ACT + " WHERE ACTIVITY_TYPE='" + activity + "'", null);
                break;
            case "Medicine":
                output = db.rawQuery("SELECT * from " + TABLE_NAME_ACT + " WHERE ACTIVITY_TYPE='" + activity + "'", null);
                break;
        }
        return output;
    }

    public Cursor getRowByID(int id) {

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor output = db.rawQuery("select * from " + TABLE_NAME_ACT + " where " + colActivityId + " = " + id, null);
        return output;
    }

    public boolean deleteRowByID(int id) {

        SQLiteDatabase db = this.getWritableDatabase();

        int output = db.delete(TABLE_NAME_ACT, colActivityId + "=" + id, null);
        return output != 0;


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
        Cursor output;
        User userFound = new User();
        // Will need password in db somehow
        SQLiteDatabase db = getWritableDatabase();
        output = db.rawQuery("Select * from " + TABLE_NAME_LOGIN + " WHERE " + colName + " = '" + userName + "'", null);
        if (output.getCount() == 0) {
            System.out.println("User not found!");
            registered = false;
        } else {
            registered = true;
            while (output.moveToNext()) {
                userFound.setName(output.getString(0));
                userFound.setPassword(output.getString(1));
            }
        }
        output.close();
        return registered;
    }

    /**
     * Reset the database (clear all data)
     */
    public void reset() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME_ACT, null, null);
        System.out.println("DELETED ALL ROWS... TABLE EMPTY");
    }

    /**
     * Returns all activities from the database for ArrayAdapter use in ListView
     */
    public ArrayList<DB_Object> getAllActivity() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<DB_Object> listItems = new ArrayList<DB_Object>();
        Cursor output = db.rawQuery("SELECT * from " + TABLE_NAME_ACT + " order by " + colDate + " desc, " + colTime + " desc", new String[] {});

        if (output.moveToFirst()) {
            do {
                DB_Object item = new DB_Object();
                item.setActivityType(output.getString(1));
                item.setDate(output.getString(2));
                item.setTime(output.getString(3));
                item.setDescription(output.getString(4));

                listItems.add(item);
            } while (output.moveToNext());
        }else{
            System.out.println("DATABASE HELPER: cursor empty");
        }

        output.close();

        return listItems;
    }
}
