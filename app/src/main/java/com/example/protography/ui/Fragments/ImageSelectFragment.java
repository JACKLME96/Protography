package com.example.protography.ui.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.example.protography.MainActivity;
import com.example.protography.ui.ViewModels.AddViewModel;
import com.squareup.picasso.Picasso;


import com.example.protography.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.theartofdev.edmodo.cropper.CropImage;

public class ImageSelectFragment extends Fragment implements View.OnClickListener {

    ImageView selectedImage;
    Button reselectBtn;
    private Uri imageUri;
    SharedPreferences sharedPref;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("Select an image");
        CropImage.activity().start(getContext(), this);
        sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_image_select, container, false);
        selectedImage = v.findViewById(R.id.image);
        reselectBtn = v.findViewById(R.id.reselectBtn);
        reselectBtn.setVisibility(View.GONE);
        reselectBtn.setOnClickListener(this);

        OnBackPressedCallback callback = new OnBackPressedCallback(true ) {
            @Override
            public void handleOnBackPressed() {
                getActivity().finish();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            selectedImage.setImageURI(imageUri);
            reselectBtn.setVisibility(View.VISIBLE);
        }
        else {
            Toast.makeText(getContext(), "You Must select an image!", Toast.LENGTH_LONG).show();
            getActivity().finish();
        }

        if(imageUri != null) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("IMAGEURI", imageUri.toString());
            editor.apply();
        }
    }

    @Override
    public void onClick(View v) {
        CropImage.activity().start(getContext(), this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        reselectBtn.setVisibility(View.VISIBLE);
        selectedImage.setImageURI(Uri.parse(sharedPref.getString("IMAGEURI", "DEFAULT")));
    }
}