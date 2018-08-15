package com.example.njjeske.cs668androiddiabetesapp;

public class User {

    private String name, password, email;

    public User() {

    }

    public User(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public void setName(String temp) {
        this.name = temp;
    }

    public void setPassword(String temp) {
        this.password = temp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
