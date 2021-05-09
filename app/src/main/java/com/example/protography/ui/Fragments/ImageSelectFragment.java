package com.example.protography.ui.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.ImageView;

import android.widget.Toast;

import com.example.protography.R;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;
import com.theartofdev.edmodo.cropper.CropImage;

import org.jetbrains.annotations.NotNull;


public class ImageSelectFragment extends Fragment implements View.OnClickListener, Step {

    ImageView selectedImage;
    Button reselectBtn;
    private Uri imageUri;
    SharedPreferences sharedPref;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public VerificationError verifyStep() {
        if(imageUri == null)
           return new VerificationError("You must select an image to proceed");
        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull @NotNull VerificationError error) {
        Toast.makeText(this.getContext(), error.getErrorMessage(), Toast.LENGTH_SHORT).show();
    }
}