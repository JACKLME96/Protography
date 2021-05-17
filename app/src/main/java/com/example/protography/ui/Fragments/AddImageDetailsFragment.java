package com.example.protography.ui.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.protography.R;
import com.example.protography.databinding.FragmentAddImageDetailsBinding;
import com.example.protography.ui.Models.Image;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;
import org.jetbrains.annotations.NotNull;

public class AddImageDetailsFragment extends Fragment implements BlockingStep {
    private ImageView image;
    private TextInputEditText title;
    private EditText description;
    private EditText equipment;
    private EditText iso;
    private EditText shutterSpeed;
    private EditText aperture;
    private LinearLayout settingsView;
    private RelativeLayout arrowLayout;
    private Button arrowBtn;
    private CardView equipmentCardView;
    private EditText time;
    private EditText tips;
    private Uri imageuri;
    private String coords;
    private String settings ="";
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private FragmentAddImageDetailsBinding binding;

    SharedPreferences sharedPref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentAddImageDetailsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        image = binding.image;
        title = binding.title;
        description = binding.description;
        equipment = binding.equipment;
        iso = binding.iso;
        aperture = binding.aperture;
        shutterSpeed = binding.shutterSpeed;
        time = binding.time;
        tips = binding.tips;
        equipmentCardView = binding.equipmentCard;
        arrowBtn = binding.arrowBtn;
        settingsView = binding.expandableView;

        //gestione sezione settings
        arrowLayout = binding.arrowLayout;
        arrowLayout.setOnClickListener(v -> {
            if (settingsView.getVisibility()==View.GONE){
                TransitionManager.beginDelayedTransition(equipmentCardView, new AutoTransition());
                settingsView.setVisibility(View.VISIBLE);
                arrowBtn.setBackgroundResource(R.drawable.ic_baseline_arrow_drop_up_24);
            } else {
                TransitionManager.beginDelayedTransition(equipmentCardView, new AutoTransition());
                settingsView.setVisibility(View.GONE);
                arrowBtn.setBackgroundResource(R.drawable.ic_baseline_arrow_drop_down_24);
                iso.getText().clear();
                shutterSpeed.getText().clear();
                aperture.getText().clear();
            }
        });

        imageuri = Uri.parse(sharedPref.getString("IMAGEURI", "DEFAULT"));
        mStorageRef = FirebaseStorage.getInstance().getReference("Images");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Images");

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        image.setImageURI(imageuri);
    }

    private void uploadFile() {
        if (imageuri != null) {
            coords = sharedPref.getString("COORDS", "DEFAULT");

            AlertDialog dialog =  new AlertDialog.Builder(getContext())
                    .setView(R.layout.loading_dialog)
                    .setCancelable(false)
                    .create();
            dialog.show();

            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + ".jpg");
            fileReference.putFile(imageuri)
                    .addOnSuccessListener(taskSnapshot -> {

                        Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!urlTask.isSuccessful());
                        Uri downloadUrl = urlTask.getResult();
                        final String image_url = String.valueOf(downloadUrl);

                        Toast.makeText(getActivity(), "Upload successful", Toast.LENGTH_LONG).show();

                        Image upload = new Image(title.getText().toString().trim(),image_url, description.getText().toString().trim(), settings,
                                time.getText().toString().trim(), tips.getText().toString().trim(), equipment.getText().toString().trim(), coords);
                        String uploadId = mDatabaseRef.push().getKey();
                        mDatabaseRef.child(uploadId).setValue(upload);

                        dialog.dismiss();
                        getActivity().finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        getActivity().finish();
                    });
        }
        else {
            Toast.makeText(getContext(), "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean FieldChecks(){
        if (title.getText().toString().trim().isEmpty()) {
            title.setError("Field is required");
            title.requestFocus();
            return false;
        }

        if (description.getText().toString().trim().isEmpty()) {
            description.setError("Field is required");
            description.requestFocus();
            return false;
        }

        if (equipment.getText().toString().trim().isEmpty()) {
            equipment.setError("Field is required");
            equipment.requestFocus();
            return false;
        }

        //i settings se visibili devono essere tutti valorizzati
        if (settingsView.getVisibility()==View.VISIBLE) {
            if (iso.getText().toString().trim().isEmpty()) {
                if (shutterSpeed.getText().toString().trim().isEmpty()) {
                    if (aperture.getText().toString().trim().isEmpty()) {
                        iso.setError("Fill all fields");
                        iso.requestFocus();
                        return false;
                    } else {
                        aperture.setError("Fill all fields");
                        aperture.requestFocus();
                        return false;
                    }
                } else {
                    iso.setError("Fill all fields");
                    iso.requestFocus();
                    return false;
                }
            } else {
                if (!shutterSpeed.getText().toString().trim().isEmpty()) {
                    if (!aperture.getText().toString().trim().isEmpty()) {
                        settings = iso.getText().toString().trim() + "," + "f/" + shutterSpeed.getText().toString().trim() + "," + aperture.getText().toString().trim();
                        return true;
                    } else {
                        aperture.setError("Fill all fields");
                        aperture.requestFocus();
                        return false;
                    }
                } else {
                    shutterSpeed.setError("Fill all fields");
                    shutterSpeed.requestFocus();
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {

    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {
        if(FieldChecks())
            uploadFile();
    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {
        callback.goToPrevStep();
    }


    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull @NotNull VerificationError error) {

    }
}