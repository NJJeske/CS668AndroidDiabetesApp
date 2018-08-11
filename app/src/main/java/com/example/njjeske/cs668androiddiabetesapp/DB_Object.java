package com.example.njjeske.cs668androiddiabetesapp;

public class DB_Object {
    String type, date, time, description;
    int id;

    public DB_Object() {
    }

    public DB_Object(int id, String type, String date, String time, String description) {
        this.id = id;
        this.type = type;
        this.date = date;
        this.time = time;
        this.description = description;
    }

    public DB_Object(String type, String date, String time, String description) {
        this.type = type;
        this.date = date;
        this.time = time;
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
