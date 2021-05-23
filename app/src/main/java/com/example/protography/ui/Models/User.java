package com.example.protography.ui.Models;

public class User {
    private String fullName, email;

    public User(){
    }

    public User(String fullName, String email){
        this.fullName = fullName;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }
}
