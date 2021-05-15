package com.example.protography.ui.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
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
    private EditText settings;
    private EditText time;
    private EditText tips;
    private Uri imageuri;
    private String coords;
    private ProgressBar progressBar;
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
        settings = binding.settings;
        time = binding.time;
        tips = binding.tips;
        progressBar = binding.progressBar;

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

            //set progress bar visible and disable user interaction
            progressBar.setVisibility(View.VISIBLE);
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + ".jpg");
            fileReference.putFile(imageuri)
                    .addOnSuccessListener(taskSnapshot -> {

                        Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!urlTask.isSuccessful());
                        Uri downloadUrl = urlTask.getResult();
                        final String image_url = String.valueOf(downloadUrl);

                        Toast.makeText(getActivity(), "Upload successful", Toast.LENGTH_LONG).show();

                        Image upload = new Image(title.getText().toString().trim(),image_url, description.getText().toString().trim(), settings.getText().toString().trim(),
                                time.getText().toString().trim(), tips.getText().toString().trim(), equipment.getText().toString().trim(), coords);
                        String uploadId = mDatabaseRef.push().getKey();
                        mDatabaseRef.child(uploadId).setValue(upload);

                        //set progress bar invisible and enable user interaction
                        progressBar.setVisibility(View.GONE);
                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        getActivity().finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        //set progress bar invisible and enable user interaction
                        progressBar.setVisibility(View.GONE);
                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

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

        if (settings.getText().toString().trim().isEmpty()) {
            settings.setError("Field is required");
            settings.requestFocus();
            return false;
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