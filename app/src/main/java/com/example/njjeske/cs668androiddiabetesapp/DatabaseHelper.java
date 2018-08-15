package com.example.njjeske.cs668androiddiabetesapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {


    private String password = "password";

    private static final String TABLE_NAME_LOGIN = "LOGIN_DB";
    private static final String colName = "NAME";
    private static final String colPassword = "PASSWORD";

    private static final String TABLE_REGIMEN = "REGIMEN_DB";
    private static final String colRegID ="_id";
    private static final String colRegName = "REG_NAME";
    private static final String colRegDescription = "REG_DESCRIPTION";
    private static final String colRegTime = "REG_TIME";

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

        db.execSQL(" create table " + TABLE_REGIMEN + " ("
                + colRegID + " integer primary key autoincrement, "
                + colRegName + " text, "
                + colRegDescription + " text, "
                + colRegTime + " text);");

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
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REGIMEN);
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

    public void insertRegimen(String activityType, String time, String description) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(colRegName, activityType);
        contentValues.put(colRegDescription, description);
        contentValues.put(colRegTime, time);
        db.insert(TABLE_REGIMEN, null, contentValues);

        db.close();
    }

    public void updateActivity(int id, DB_Object object) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        DB_Object act = object;

        contentValues.put(colActivityType, act.getActivityType());
        contentValues.put(colDate, act.getDate());
        contentValues.put(colTime, act.getTime());
        contentValues.put(colDescription, act.getDescription());

        String where = "_id = ?";
        String[] whereArgs = new String[] {String.valueOf(id)};

        try {
            db.update(TABLE_NAME_ACT, contentValues, where, whereArgs);
        }
        catch (Exception e){
            String error =  e.getMessage().toString();
            System.out.println(error);
        }

        db.close();


    }

    public void updateRegimen(int id, DB_Object object) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        DB_Object act = object;

        contentValues.put(colRegName, act.getActivityType());
        contentValues.put(colRegTime, act.getTime());
        contentValues.put(colRegDescription, act.getDescription());

        String where = "_id = ?";
        String[] whereArgs = new String[]{String.valueOf(id)};

        try {
            db.update(TABLE_REGIMEN, contentValues, where, whereArgs);
        } catch (Exception e) {
            String error = e.getMessage().toString();
            System.out.println(error);
        }

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
            case "Regimen":
                output = db.rawQuery("SELECT * from " + TABLE_REGIMEN + " order by " + colRegTime + " desc", null);
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

    public boolean deleteRowByID(int id, String table) {

        SQLiteDatabase db = this.getWritableDatabase();
        int output;
        if (table.equals("Regimen")) {
            output = db.delete(TABLE_REGIMEN, colRegID + "=" + id, null);
        } else if (table.equals("Activity")) {
            output = db.delete(TABLE_NAME_ACT, colActivityId + "=" + id, null);
        } else {
            return false;
        }

        return output != 0;


    }

    public void addUser(User user) {
        // Will need password in db somehow
        SQLiteDatabase db = this.getWritableDatabase();
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
        SQLiteDatabase db = this.getWritableDatabase();
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

    public User getUserByUserName(String userName) {
        Cursor output;
        User userFound = new User();
        SQLiteDatabase db = getWritableDatabase();
        output = db.rawQuery("Select * from " + TABLE_NAME_LOGIN + " WHERE " + colName + " = '" + userName + "'", null);
        if (output.getCount() == 0) {
            System.out.println("No Records");
        } else {
            while (output.moveToNext()) {
                userFound.setName(output.getString(0));
                userFound.setPassword(output.getString(1));
            }
        }
        output.close();
        return userFound;
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
                item.setId(output.getInt(0));
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

    /**
     * Filter by Keywords
     *
     * @param keywords
     * @return
     */
    public ArrayList<DB_Object> getByKeyword(String keywords) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<DB_Object> listItems = new ArrayList<DB_Object>();
        if (keywords.equals("")) {
            listItems = getAllActivity();
        } else {
            Cursor output = db.rawQuery("SELECT * from " + TABLE_NAME_ACT
                    + " WHERE DESCRIPTION LIKE '%" + keywords + "%'"
                    + " order by " + colDate + " desc, " + colTime + " desc", new String[]{});
            if (output.moveToFirst()) {
                do {
                    DB_Object item = new DB_Object();
                    item.setId(output.getInt(0));
                    item.setActivityType(output.getString(1));
                    item.setDate(output.getString(2));
                    item.setTime(output.getString(3));
                    item.setDescription(output.getString(4));

                    listItems.add(item);
                } while (output.moveToNext());
            } else {
                System.out.println("DATABASE HELPER: cursor empty");
            }

            output.close();
        }
        return listItems;
    }

    /**
     * Returns all activities from the database for ArrayAdapter use in ListView
     */
    public ArrayList<DB_Object> getAllRegimen() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<DB_Object> listItems = new ArrayList<DB_Object>();
        Cursor output = db.rawQuery("SELECT * from " + TABLE_REGIMEN + " order by " + colRegTime + " desc", new String[]{});

        if (output.moveToFirst()) {
            do {
                DB_Object item = new DB_Object();
                item.setId(output.getInt(0));
                item.setActivityType(output.getString(1));
                item.setDescription(output.getString(2));
                item.setTime(output.getString(3));

                listItems.add(item);
            } while (output.moveToNext());
        } else {
            System.out.println("DATABASE HELPER: cursor empty");
        }

        output.close();

        return listItems;
    }

    public Cursor getRegimenByDate(String date, String userName) {
        Cursor result = null;
        SQLiteDatabase db = getWritableDatabase();
        result = db.rawQuery("Select * from " + TABLE_REGIMEN + " WHERE USERNAME='" + userName + "' AND DATE like '"+date+"%'", null);
       // System.out.println("Select * from " + TABLE_REGIMEN + " WHERE USERNAME='" + userName + "' AND DATE like '"+date+"%'");
        return result;
    }
}
