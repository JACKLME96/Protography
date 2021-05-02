package com.example.protography.ui.ViewModels;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;

public class AddViewModel extends ViewModel {

    private MutableLiveData<Uri> imageSelectedUri;
    private MutableLiveData<LatLng> imageSelectedCords;


    public AddViewModel() {
        imageSelectedUri = new MutableLiveData<>();
        imageSelectedCords = new MutableLiveData<>();

    }

    public LiveData<Uri> getUri() {
        return imageSelectedUri;
    }

    public void SetUri( Uri input) {
        imageSelectedUri.setValue(input);
    }

    public LiveData<LatLng> getcCords() {
        return imageSelectedCords;
    }

    public void SetCoords( LatLng input) {
        imageSelectedCords.setValue(input);
    }



}