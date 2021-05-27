package com.example.protography.ui.Adapters;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.example.protography.ui.Fragments.AddImage.AddImageDetailsFragment;
import com.example.protography.ui.Fragments.AddImage.ImageSelectFragment;
import com.example.protography.ui.Fragments.AddImage.PlaceSelectFragment;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;
import com.stepstone.stepper.viewmodel.StepViewModel;

public class AddPhotoAdapter extends AbstractFragmentStepAdapter {

    public AddPhotoAdapter(FragmentManager fm, Context context) {
        super(fm, context);
    }

    @Override
    public Step createStep(int position) {
        Bundle b = new Bundle();
        switch (position) {
            case 0:
                final ImageSelectFragment step1 = new ImageSelectFragment();
                b.putInt("CURRENT_STEP_POSITION_KEY", position);
                step1.setArguments(b);
                return step1;
            case 1:
                final PlaceSelectFragment step2 = new PlaceSelectFragment();
                b.putInt("CURRENT_STEP_POSITION_KEY", position);
                step2.setArguments(b);
                return step2;
            case 2:
                final AddImageDetailsFragment step3 = new AddImageDetailsFragment();
                b.putInt("CURRENT_STEP_POSITION_KEY", position);
                step3.setArguments(b);
                return step3;
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @NonNull
    @Override
    public StepViewModel getViewModel(@IntRange(from = 0) int position) {
        //Override this method to set Step title for the Tabs, not necessary for other stepper types
        switch (position) {
            case 0:
            return new StepViewModel.Builder(context)
                    .setTitle("Image") //can be a CharSequence instead
                    .setSubtitle("Choose your image")
                    .create();
            case 1:
                return new StepViewModel.Builder(context)
                        .setTitle("Position") //can be a CharSequence instead
                        .setSubtitle("Set your coordinates")
                        .create();
            case 2:
                return new StepViewModel.Builder(context)
                        .setTitle("Details") //can be a CharSequence instead
                        .setSubtitle("Add some details")
                        .create();
            default:
                throw new IllegalArgumentException();
        }
    }
}
