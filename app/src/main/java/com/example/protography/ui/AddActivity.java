package com.example.protography.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.View;


import com.example.protography.R;
import com.example.protography.ui.Adapters.AddPhotoAdapter;

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
        sharedPref.edit().remove("COORDS").apply();
        sharedPref.edit().remove("IMAGEURI").apply();
    }

    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this)
                .setTitle("Warning")
                .setMessage("Do you want to exit? You'll lose your data!")
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, (arg0, arg1) -> AddActivity.super.onBackPressed()).create().show();
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