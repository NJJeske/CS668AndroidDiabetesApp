package com.example.njjeske.cs668androiddiabetesapp;

/**
 * Database Object to represent the various activities to add from the app
 */
public class DB_Object {
    String activityType, date, time, description;
    int id;

    public DB_Object() {
    }

    public DB_Object(int id, String activityType, String date, String time, String description) {
        this.id = id;
        this.activityType = activityType;
        this.date = date;
        this.time = time;
        this.description = description;
    }

    public DB_Object(String activityType, String date, String time, String description) {
        this.activityType = activityType;
        this.date = date;
        this.time = time;
        this.description = description;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
