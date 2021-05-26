package com.example.protography.ui.Models;

import java.io.Serializable;

public class Image implements Serializable {
    private String ImageTitle;
    private String ImageUrl;
    private String ImageDescription;
    private String ImageEquipment;
    private String ImageSettings;
    private String ImageTime;
    private String ImageTips;
    private String CoordsIndex;
    private double Latitude;
    private double Longitude;
    private String ImageCategory;
    private String ImageNameUser;

    public Image(){ }

    public Image(String title, String url, String description, String settings, String time, String tips, String equipment, String coords, Double latitude, Double longitude, String category, String nameUser){
        ImageTitle = title;
        ImageUrl = url;
        ImageDescription = description;
        ImageEquipment = equipment;
        ImageSettings = settings;
        ImageTime = time;
        ImageTips = tips;
        CoordsIndex = coords;
        Latitude = latitude;
        Longitude = longitude;
        ImageCategory = category;
        ImageNameUser = nameUser;
    }

    public String getImageTitle(){
        return ImageTitle;
    }

    public void setImageTitle(String title){
        ImageTitle = title;
    }

    public String getImageUrl(){
        return ImageUrl;
    }

    public void setImageUrl(String url){
        ImageUrl = url;
    }

    public String getImageDescription(){
        return ImageDescription;
    }

    public void setImageDescription(String description){
        ImageDescription = description;
    }

    public String getImageEquipment(){
        return ImageEquipment;
    }

    public void setImageEquipment(String equipment){
        ImageEquipment = equipment;
    }

    public String getImageTime(){return ImageTime;}

    public void setImageTime(String time){
        ImageTime = time;
    }

    public String getImageTips(){
        return ImageTips;
    }

    public void setImageTips(String tips){
        ImageTips = tips;
    }

    public String getImageSettings(){
        return ImageSettings;
    }

    public void setImageSettings(String settings){
        ImageSettings = settings;
    }

    public String getCoords(){
        return CoordsIndex;
    }

    public void setCoords(String coords){
        CoordsIndex = coords;
    }

    public Double getLatitude(){
        return Latitude;
    }

    public void setLatitude(Double latitude){
        Latitude = latitude;
    }

    public Double getLongitude(){
        return Longitude;
    }

    public void setLongitude(Double longitude){
        Longitude = longitude;
    }

    public String getImageCategory(){
        return ImageCategory;
    }

    public void setImageCategory(String category){
        ImageCategory = category;
    }

    public String getImageNameUser() { return ImageNameUser; }

    public void setImageNameUser(String nameUser) { ImageNameUser = nameUser; }
}