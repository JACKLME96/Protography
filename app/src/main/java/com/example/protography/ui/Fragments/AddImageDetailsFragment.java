package com.example.protography.ui.Fragments;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.protography.R;
import com.example.protography.ui.Models.Image;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import org.jetbrains.annotations.NotNull;

public class AddImageDetailsFragment extends Fragment implements BlockingStep {
    private ImageView image;
    private TextInputEditText title;
    private EditText descriprion;
    private EditText equipment;
    private EditText settings;
    private EditText time;
    private EditText tips;
    private Uri imageuri;
    private String coords;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;

    SharedPreferences sharedPref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_add_image_details, container, false);

        image = v.findViewById(R.id.image);
        title = v.findViewById(R.id.title);
        descriprion = v.findViewById(R.id.description);
        equipment = v.findViewById(R.id.equipment);
        settings = v.findViewById(R.id.settings);
        time = v.findViewById(R.id.time);
        tips = v.findViewById(R.id.tips);
        imageuri = Uri.parse(sharedPref.getString("IMAGEURI", "DEFAULT"));
        coords = sharedPref.getString("COORDS", "DEFAULT");

        mStorageRef = FirebaseStorage.getInstance().getReference("Images");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Images");

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        image.setImageURI(imageuri);
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        if (imageuri != null) {
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(imageuri));
            fileReference.putFile(imageuri)
                    .addOnSuccessListener(taskSnapshot -> {

                        Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!urlTask.isSuccessful());
                        Uri downloadUrl = urlTask.getResult();
                        final String image_url = String.valueOf(downloadUrl);

                        Toast.makeText(getActivity(), "Upload successful", Toast.LENGTH_LONG).show();

                        Image upload = new Image(title.getText().toString().trim(),image_url, descriprion.getText().toString().trim(), settings.getText().toString().trim(),
                                time.getText().toString().trim(),tips.getText().toString().trim(),equipment.getText().toString().trim(),coords);
                        String uploadId = mDatabaseRef.push().getKey();
                        mDatabaseRef.child(uploadId).setValue(upload);

                        getActivity().finish();
                    })
                    .addOnFailureListener(e -> Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show());
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

        if (descriprion.getText().toString().trim().isEmpty()) {
            descriprion.setError("Field is required");
            descriprion.requestFocus();
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