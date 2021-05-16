package com.example.protography.ui.Models;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class Image implements Serializable {
    private String ImageTitle;
    private String ImageUrl;
    private String ImageDescription;
    private String ImageEquipment;
    private String ImageSettings;
    private String ImageTime;
    private String ImageTips;
    private String ImageCoords;

    public Image(){ }

    public Image(String title, String url, String description, String settings, String time, String tips, String equipment, String coords){
        ImageTitle = title;
        ImageUrl = url;
        ImageDescription = description;
        ImageEquipment = equipment;
        ImageSettings = settings;
        ImageTime = time;
        ImageTips = tips;
        ImageCoords = coords;
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

    public String getImageTime(){
        return ImageTime;
    }

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
        return ImageCoords;
    }

    public void setCoords(String coords){
        ImageCoords = coords;
    }
}