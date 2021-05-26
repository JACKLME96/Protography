package com.example.protography.ui.Models;

import java.util.List;

public class User {
    private String fullName, email;
    // Lista di imageUrl
    //private List<String> fotoPiaciute;

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
