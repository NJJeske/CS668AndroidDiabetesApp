package com.example.njjeske.cs668androiddiabetesapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    //User Table (Main)
    private static final String dbName = "demoDB";
    private static final String userTable = "USER";
    private static final String colUserID = "USER_ID";
    private static final String colUserName = "USER_NAME";
    private static final String colGlucose = "GLUCOSE_ID";
    private static final String colDiet = "DIET_ID";
    private static final String colExercise = "EXERCISE_ID";
    private static final String colMedication = "MEDICATION_ID";

    //Glucose Table
    private static final String glucoseTable = "GLUCOSE";
    private static final String colGlucoseID = "GLUCOSE_ID";
    private static final String colGlucoseValue = "GLUCOSE_VALUE";
    private static final String colGlucoseTime = "GLUCOSE_TIME";

    //Diet Table
    private static final String dietTable = "DIET";
    private static final String colDietID = "DIET_ID";
    private static final String colDietType = "DIET_TYPE";
    private static final String colDietName = "DIET_NAME";
    private static final String colDietAmount = "DIET_AMOUNT";
    private static final String colDietTime = "DIET_TIME";

    //Medication Table
    private static final String medicationTable = "MEDICATION";
    private static final String colMedicationID = "MEDICATION_ID";
    private static final String colMedicationName = "MEDICATION_NAME";
    private static final String colMedicationAmount = "MEDICATION_AMOUNT";
    private static final String colMedicationTime = "MEDICATION_TIME";

    //Exercise Table
    private static final String exerciseTable = "EXERCISE";
    private static final String colExerciseID = "EXERCISE_ID";
    private static final String colExerciseActivity = "EXERCISE_ACTIVITY";
    private static final String colExerciseDuration = "EXERCISE_DURATION";
    private static final String colExerciseTime = "EXERCISE_TIME";

    private static final String viewEmps = "ViewEmps";

    public DatabaseHelper(Context context) {
        super(context, dbName, null, 33);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //User
//        db.execSQL("CREATE TABLE " + userTable + " (" + colUserID + " INTEGER PRIMARY KEY, " + colUserName + " TEXT, " +
//                colGlucose + " Integer NOT NULL ,FOREIGN KEY ("+ colGlucose + ") REFERENCES " + glucoseTable + "(" + colGlucoseID + ")" +
//                colExercise + " Integer NOT NULL ,FOREIGN KEY ("+ colExercise + ") REFERENCES " + exerciseTable + "(" + colExerciseID + ")" +
//                colMedication + " Integer NOT NULL ,FOREIGN KEY ("+ colMedication + ") REFERENCES " + medicationTable + "(" + colMedicationID + ")" +
//                colDiet + " INTEGER NOT NULL ,FOREIGN KEY (" + colDiet + ") REFERENCES " + dietTable + " (" + colDietID + "));");

        //Diet
        db.execSQL("CREATE TABLE " + dietTable + " (" + colDietID + " INTEGER PRIMARY KEY , " +
                colDietType + " TEXT, " + colDietName + " TEXT, " + colDietAmount + " REAL, " + colDietTime + " TEXT );");

        //Glucose
        db.execSQL("CREATE TABLE " + glucoseTable + " (" + colGlucoseID + " INTEGER PRIMARY KEY , " +
                colGlucoseValue + " INTEGER, " + colGlucoseTime + " TEXT );");

        //Medication
        db.execSQL("CREATE TABLE " + medicationTable + " (" + colMedicationID + " INTEGER PRIMARY KEY , " +
                colMedicationName + " TEXT, " + colMedicationAmount + " INTEGER, " + colMedicationTime + " TEXT );");

        //Exercise
        db.execSQL("CREATE TABLE " + exerciseTable + " (" + colExerciseID + " INTEGER PRIMARY KEY , " +
                colExerciseActivity + " TEXT, " + colExerciseDuration + " INTEGER, " + colExerciseTime + " TEXT );");


//        db.execSQL("CREATE TRIGGER fk_empdept_deptid " + " BEFORE INSERT " + " ON " + userTable + " FOR EACH ROW BEGIN" +
//                " SELECT CASE WHEN ((SELECT " + colDietID + " FROM " + dietTable + " WHERE " +
//                colDietID + " = new." + colDiet + " ) IS NULL)" + " THEN RAISE (ABORT,'Foreign Key Violation') END;" + "  END;");
//
//        db.execSQL("CREATE VIEW " + viewEmps + " AS SELECT " + userTable + "." + colUserID + " AS _id," +
//                " " + userTable + "." + colUserName + "," + " " + userTable + "." + colGlucose + "," +
//                " " + dietTable + "." + colDietType + "" + " FROM " + userTable + " JOIN " + dietTable +
//                " ON " + userTable + "." + colDiet + " =" + dietTable + "." + colDietID);
        //Inserts pre-defined departments
//        InsertDepts(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


        db.execSQL("DROP TABLE IF EXISTS " + userTable);
        db.execSQL("DROP TABLE IF EXISTS " + dietTable);
        db.execSQL("DROP TABLE IF EXISTS " + glucoseTable);
        db.execSQL("DROP TABLE IF EXISTS " + medicationTable);
        db.execSQL("DROP TABLE IF EXISTS " + exerciseTable);

//        db.execSQL("DROP TRIGGER IF EXISTS dept_id_trigger");
//        db.execSQL("DROP TRIGGER IF EXISTS dept_id_trigger22");
//        db.execSQL("DROP TRIGGER IF EXISTS fk_empdept_deptid");
//        db.execSQL("DROP VIEW IF EXISTS " + viewEmps);
        onCreate(db);
    }
}
