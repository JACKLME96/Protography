package com.example.protography.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;


import com.example.protography.R;
import com.example.protography.ui.Adapters.AddPhotoAdapter;
import com.example.protography.ui.Fragments.AddImageDetailsFragment;
import com.example.protography.ui.Fragments.ImageSelectFragment;
import com.example.protography.ui.Fragments.PlaceSelectFragment;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

public class AddActivity extends AppCompatActivity implements StepperLayout.StepperListener {

    SharedPreferences sharedPref;
    private StepperLayout mStepperLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        mStepperLayout = (StepperLayout) findViewById(R.id.stepperLayout);
        mStepperLayout.setAdapter(new AddPhotoAdapter(getSupportFragmentManager(), this));
        sharedPref = getPreferences(Context.MODE_PRIVATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sharedPref.edit().remove("COORDS").apply();
        sharedPref.edit().remove("IMAGEURI").apply();
    }

    @Override
    public void onCompleted(View completeButton) {

    }

    @Override
    public void onError(VerificationError verificationError) {

    }

    @Override
    public void onStepSelected(int newStepPosition) {

    }

    @Override
    public void onReturn() {

    }
}