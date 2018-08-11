package com.example.njjeske.cs668androiddiabetesapp;

public class Activity {

    private String activityType;
    private String date;
    private String time;
    private String description;

    public String getActivityType() {
        return activityType;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getDescription() {
        return description;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
