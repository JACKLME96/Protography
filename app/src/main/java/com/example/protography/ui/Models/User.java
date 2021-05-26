package com.example.protography.ui.Models;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String fullName, email;
    private List<String> fotoPiaciute;  // Lista di imageUrl

    public User(){
    }

    public User(String fullName, String email){
        this.fullName = fullName;
        this.email = email;
        fotoPiaciute = new ArrayList<>();
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public List<String> getFotoPiaciute() {
        return fotoPiaciute;
    }

    public void setFotoPiaciute(List<String> fotoPiaciute) {
        this.fotoPiaciute = fotoPiaciute;
    }

    public void addLike(String likedImageUrl) {
        fotoPiaciute.add(likedImageUrl);
    }

    public void removeLike(String likedImageUrl) {
        for (String urlFoto : fotoPiaciute)
            if (urlFoto.equals(likedImageUrl))
                fotoPiaciute.remove(likedImageUrl);
    }
}
