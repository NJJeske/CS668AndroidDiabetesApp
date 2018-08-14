package com.example.njjeske.cs668androiddiabetesapp;

import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Search by date, time, keywords, BGL value, type
 */
public class Search {
    ArrayList<DB_Object> db;
    String fromDate, toDate, fromTime, toTime, keywords, startValue, endValue;
    boolean check_bgl, check_exercise, check_diet, check_medication;

    public Search(ArrayList<DB_Object> db) {
        this.db = db;
        //everything else is default empty/false
    }

    public Search(ArrayList<DB_Object> db, SharedPreferences sp) {
        if (db != null) {
            this.db = db;
        } else {
            Log.v("SEARCH", "ArrayList passed is null, creating empty ArrayList");
            db = new ArrayList<>(); // returns nothing
        }
        if (sp != null) {
            this.fromDate = sp.getString("from_date", "");
            this.toDate = sp.getString("to_date", "");
            if (!sp.getString("from_time", "").equals("")) {
                this.fromTime = sp.getString("from_time", "");
            } else {
                this.fromTime = "0:0";
            }
            if (!sp.getString("to_time", "").equals("")) {
                this.toTime = sp.getString("to_time", "");
            } else {
                this.toTime = "24:0";
            }
            this.keywords = sp.getString("keywords", "");
            if (!sp.getString("start_val", "").equals("")) {
                this.startValue = sp.getString("start_val", "");
            } else {
                this.startValue = "0";
            }
            if (!sp.getString("end_val", "").equals("")) {
                this.endValue = sp.getString("end_val", "");
            } else {
                this.endValue = "800";
            }
            this.check_bgl = sp.getBoolean("bgl_check", false);
            this.check_exercise = sp.getBoolean("exercise_check", false);
            this.check_diet = sp.getBoolean("diet_check", false);
            this.check_medication = sp.getBoolean("medication_check", false);
        } else {
            Log.v("SEARCH", "SharedPreferences passed is null");
        }
    }

    public ArrayList<DB_Object> getDb() {
        return db;
    }

    public void setDb(ArrayList<DB_Object> db) {
        this.db = db;
    }

    /**
     * SEARCH: Filters by activity type
     *
     * @return filtered list by type
     */
    public ArrayList<DB_Object> filterByType() {
        return filterByType(db);
    }

    /**
     * SEARCH: Filters by activity type
     *
     * @param objects
     * @return filtered list by type
     */
    public ArrayList<DB_Object> filterByType(ArrayList<DB_Object> objects) {
//        Log.v("RESULTS", "FILTERING BY TYPE");
//        Log.v("RESULTS", "TYPES - Starting List Size: " + objects.size());
        //new array list that will hold the filtered data
        ArrayList<DB_Object> filteredDataByType = new ArrayList<>();
        for (int i = 0; i < objects.size(); i++) {
            String type = objects.get(i).getActivityType();
            if (type.equals("Blood Glucose") && check_bgl) {
//                Log.v("RESULTS", type + " added to results list");
                filteredDataByType.add(objects.get(i));
            } else if (type.equals("Exercise") && check_exercise) {
//                Log.v("RESULTS", type + " added to results list");
                filteredDataByType.add(objects.get(i));
            } else if (type.equals("Diet") && check_diet) {
//                Log.v("RESULTS", type + " added to results list");
                filteredDataByType.add(objects.get(i));
            } else if (type.equals("Medication") && check_medication) {
//                Log.v("RESULTS", type + " added to results list");
                filteredDataByType.add(objects.get(i));
            }
        }
//        Log.v("RESULTS", "TYPES - Ending List Size: " + filteredDataByType.size());
        return filteredDataByType;
    }

    /**
     * SEARCH: Filters by date
     *
     * @return filtered list by dates
     */
    public ArrayList<DB_Object> filterByDate() {
        return filterByDate(db);
    }

    /**
     * SEARCH: Filters by date
     *
     * @param objects
     * @return filtered list by dates
     */
    public ArrayList<DB_Object> filterByDate(ArrayList<DB_Object> objects) {
        Log.v("RESULTS", "FILTERING BY DATE");
        Log.v("RESULTS", "DATE - Starting List Size: " + objects.size());
        ArrayList<DB_Object> filteredData = new ArrayList<>();

        for (int i = 0; i < objects.size(); i++) {
            if (isAfterDate(fromDate, objects.get(i).getDate()) &&
                    isBeforeDate(toDate, objects.get(i).getDate()))
                filteredData.add(objects.get(i));

        }
        Log.v("RESULTS", "DATE - Ending List Size: " + filteredData.size());
        return filteredData;
    }

    /**
     * SEARCH: Filters list by time
     *
     * @return filtered list by time
     */
    public ArrayList<DB_Object> filterByTime() {
        return filterByTime(db);
    }

    /**
     * SEARCH: Filters list by time
     *
     * @param objects
     * @return filtered list by time
     */
    public ArrayList<DB_Object> filterByTime(ArrayList<DB_Object> objects) {
        Log.v("RESULTS", "FILTERING BY TIME");
        Log.v("RESULTS", "TIME - Starting List Size: " + objects.size());
        ArrayList<DB_Object> filteredTime = new ArrayList<>();
        if (fromTime.equals("")) {
            fromTime = "00:00";
        }
        if (toTime.equals("")) {
            toTime = "24:00";
        }

        for (int i = 0; i < objects.size(); i++) {
            if (isAfterTime(fromTime, objects.get(i).getTime()) &&
                    !isAfterTime(toTime, objects.get(i).getTime()))
                filteredTime.add(objects.get(i));
        }
        Log.v("RESULTS", "TIME - Ending List Size: " + filteredTime.size());
        return filteredTime;
    }

    /**
     * SEARCH: Filter database results by BGL Values
     *
     * @return filtered list by the Bgl Values
     */
    public ArrayList<DB_Object> filterByBglValue() {
        return filterByBglValue(db);
    }

    /**
     * SEARCH: Filter database results by BGL Values
     *
     * @param objects
     * @return filtered list by the Bgl Values
     */
    public ArrayList<DB_Object> filterByBglValue(ArrayList<DB_Object> objects) {
        Log.v("RESULTS", "FILTERING BY BGL VALUE");
        Log.v("RESULTS", "BGL VAL - Starting List Size: " + objects.size());
        ArrayList<DB_Object> filteredBglValue = new ArrayList<>();
        for (int i = 0; i < objects.size(); i++) {
            if (objects.get(i).getActivityType().equals("Blood Glucose")) {
                if (isBetweenBglValue(startValue, objects.get(i).getDescription(), endValue)) {
                    filteredBglValue.add(objects.get(i));
                }
            } else {
                filteredBglValue.add(objects.get(i));
            }
        }
        Log.v("RESULTS", "BGL VAL - Ending List Size: " + filteredBglValue.size());
        return filteredBglValue;
    }

    /**
     * SEARCH HELPER: Filter database results by keywords
     *
     * @return filtered list of keywords
     */
    public ArrayList<DB_Object> filterByKeyWords() {
        return filterByKeyWords(db);
    }

    /**
     * SEARCH HELPER: Filter database results by keywords
     *
     * @param objects
     * @return filtered list of keywords
     */
    public ArrayList<DB_Object> filterByKeyWords(ArrayList<DB_Object> objects) {
        Log.v("RESULTS", "FILTERING BY KEYWORDS");
        Log.v("RESULTS", "KEYWORDS - Starting List Size: " + objects.size());
        ArrayList<DB_Object> filteredKeyWords = new ArrayList<>();
        for (int i = 0; i < objects.size(); i++) {
            if (!objects.get(i).getActivityType().equals("Blood Glucose")) {
                if (containsWords(keywords, objects.get(i).getDescription())) {
                    filteredKeyWords.add(objects.get(i));
                }
            } else filteredKeyWords.add(objects.get(i));
        }
        Log.v("RESULTS", "KEYWORDS - Ending List Size: " + filteredKeyWords.size());
        return filteredKeyWords;
    }

    /**
     * SEARCH HELPER: Compares between dates - afterDate
     *
     * @param fromDate
     * @param dataBaseDate
     * @return filtered list of dates
     */
    public boolean isAfterDate(String fromDate, String dataBaseDate) {
        if (fromDate.equals("")) {
            return true;
        }
        String[] dateOne = fromDate.split("/");
        String[] dateTwo = dataBaseDate.split("/");
        if (Integer.parseInt(dateOne[2]) > Integer.parseInt(dateTwo[2])) {
            return false;
        } else if (Integer.parseInt(dateOne[2]) < Integer.parseInt(dateTwo[2])) {
            return true;
        } else if (Integer.parseInt(dateOne[0]) > Integer.parseInt(dateTwo[0])) {
            return false;
        } else if (Integer.parseInt(dateOne[0]) < Integer.parseInt(dateTwo[0])) {
            return true;
        } else return Integer.parseInt(dateOne[1]) <= Integer.parseInt(dateTwo[1]);

    }

    /**
     * SEARCH HELPER: Compares between dates - beforeDate
     *
     * @param toDate
     * @param dataBaseDate
     * @return filtered list of dates
     */
    public boolean isBeforeDate(String toDate, String dataBaseDate) {
        if (toDate.equals("")) {
            return true;
        }
        String[] dateOne = toDate.split("/");
        String[] dateTwo = dataBaseDate.split("/");
        if (Integer.parseInt(dateOne[2]) < Integer.parseInt(dateTwo[2])) { // year1 < year2
            return false;
        } else if (Integer.parseInt(dateOne[2]) >= Integer.parseInt(dateTwo[2])) { // year1 >= year2
            return true;
        } else if (Integer.parseInt(dateOne[0]) < Integer.parseInt(dateTwo[0])) { // month1 < month2
            return false;
        } else if (Integer.parseInt(dateOne[0]) >= Integer.parseInt(dateTwo[0])) { // month1 >= month2
            return true;
        } else return Integer.parseInt(dateOne[1]) >= Integer.parseInt(dateTwo[1]); // day1 >= day2
    }

    /**
     * SEARCH HELPER: Compares between times
     *
     * @param fromTime
     * @param databaseTime
     * @return filtered list of times
     */
    public boolean isAfterTime(String fromTime, String databaseTime) {
        // fromTime < databaseTime
        if (fromTime.equals("")) {
            return true;
        }
        String[] timeOne = fromTime.split(":");
        String[] time = databaseTime.split(":");
        int hour = Integer.parseInt(time[0]);
        int minutes = Integer.parseInt(time[1]);

        if (Integer.parseInt(timeOne[0]) < hour) {
            return true;
        } else if (Integer.parseInt(timeOne[0]) > hour) {
            return false;
        } else return (Integer.parseInt(timeOne[1]) <= minutes);
    }

    /**
     * SEARCH HELPER: Compares between BGL values
     *
     * @param bglValueFrom
     * @param bglValueData
     * @param bglValueTo
     * @return whether if actually between values
     */
    public boolean isBetweenBglValue(String bglValueFrom, String bglValueData, String bglValueTo) {
        // 40-600
        // valueFrom: minimum
        // valueTo: maximum
        // valueFrom < bglValueData < valueTo
        Double valueFrom, valueTo;
        if (bglValueFrom.equals("") && bglValueTo.equals("")) {
            return true; // default return if both entries empty
        }

        if (!bglValueFrom.equals("")) {
            valueFrom = Double.parseDouble(bglValueFrom);
        } else {
            valueFrom = 40.0; //default min
        }
        if (!bglValueTo.equals("")) {
            valueTo = Double.parseDouble(bglValueTo);
        } else {
            valueTo = 600.0;
        }
        Double bglValueData2 = Double.parseDouble(bglValueData);

        return (bglValueData2 >= valueFrom && bglValueData2 <= valueTo);
    }

    /**
     * SEARCH HELPER: Compares using keywords
     *
     * @param keywordString
     * @param fromData
     * @return whether data contains keywords
     */
    public boolean containsWords(String keywordString, String fromData) {
        ArrayList<String> stringOne = new ArrayList<>(Arrays.asList(keywordString.split(" ")));
        if (keywordString.equals("") || keywordString.equals(null))
            return true;
        if (stringOne.contains("AND")) {
            return fromData.contains(stringOne.get(0)) && fromData.contains(stringOne.get(2));
        } else if (stringOne.contains("OR")) {
            return fromData.contains(stringOne.get(0)) || fromData.contains(stringOne.get(2));
        } else return fromData.contains(keywordString);
    }
}
