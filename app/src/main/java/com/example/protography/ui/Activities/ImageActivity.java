package com.example.protography.ui.Activities;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.protography.R;
import com.example.protography.databinding.ActivityImageBinding;
import com.example.protography.ui.Models.Image;
import com.example.protography.ui.Models.User;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ImageActivity extends AppCompatActivity {

    private static final String TAG = "ImageActivity";
    ActivityImageBinding binding;
    Image image;
    private SharedPreferences sharedPreferencesDefault;
    private List<String> imagesLiked;
    private String userKey;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityImageBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();
        setContentView(root);

        image = (Image) getIntent().getParcelableExtra("Immagine");

        // Carica dati immagine
        Picasso.get().load(image.getImageUrl()).into(binding.imageView);
        binding.user.setText(image.getImageNameUser());
        binding.title.setText(image.getImageTitle());

        if (image.getImageCategory().equals("Nature"))
            binding.chipNature.setChecked(true);
        else if (image.getImageCategory().equals("Urban"))
            binding.chipUrban.setChecked(true);
        else
            binding.chipPortrait.setChecked(true);

        // Apertura gmaps
        binding.coords.setOnClickListener(v -> {
            Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + image.getCoords());
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");

            // Controlla che ci sia un applicazione per la navigazione
            if (mapIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(mapIntent);
            } else {
                Toast.makeText(ImageActivity.this, R.string.errorMaps, Toast.LENGTH_SHORT).show();
            }
        });

        binding.description.setText(image.getImageDescription());
        binding.description.setShowingLine(4);
        binding.description.setShowMoreColor(getResources().getColor(R.color.yellow));
        binding.description.setShowLessTextColor(getResources().getColor(R.color.yellow));

        binding.equipment.setText(image.getImageEquipment());

        binding.cameraSettings.setText(image.getImageSettings());

        if (image.getImageTime() == null || image.getImageTime().isEmpty())
            binding.bestTimeToGo.setText("----------");
        else
            binding.bestTimeToGo.setText(image.getImageTime());

        if (image.getImageTips() == null || image.getImageTips().isEmpty())
            binding.tips.setText("----------");
        else
            binding.tips.setText(image.getImageTips());


        sharedPreferencesDefault = PreferenceManager.getDefaultSharedPreferences(this);
        Set<String> imageL = sharedPreferencesDefault.getStringSet("IMAGES_LIKED", null);
        imagesLiked = new ArrayList<>();
        imagesLiked.addAll(imageL);
        userKey = sharedPreferencesDefault.getString("USER_KEY", null);
        String mail = sharedPreferencesDefault.getString("EMAIL", null);
        String fullName = sharedPreferencesDefault.getString("FULLNAME", null);
        currentUser = new User(fullName, mail);
        currentUser.setFotoPiaciute(imagesLiked);

        if (imagesLiked.contains(image.getImageUrl()))
            binding.like.setImageResource(R.drawable.ic_baseline_bookmark_24);
        else
            binding.like.setImageResource(R.drawable.ic_baseline_not_bookmark_24);

        binding.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Immagini: " + imagesLiked);
                if (imagesLiked.contains(image.getImageUrl())) {

                    // Se è piaciuta la rimuovo
                    imagesLiked.remove(image.getImageUrl());
                    currentUser.setFotoPiaciute(imagesLiked);
                    FirebaseDatabase.getInstance().getReference("Users").child(userKey).setValue(currentUser);
                    HashSet<String> fotoPiaciute = new HashSet<>();
                    fotoPiaciute.addAll(imagesLiked);
                    sharedPreferencesDefault.edit().remove("IMAGES_LIKED").commit();
                    sharedPreferencesDefault.edit().putStringSet("IMAGES_LIKED", fotoPiaciute).commit();

                    binding.like.setImageResource(R.drawable.ic_baseline_not_bookmark_24);
                    Toast.makeText(ImageActivity.this, getString(R.string.disliked), Toast.LENGTH_SHORT).show();
                } else {

                    // Se non è piaciuta la aggiungo
                    imagesLiked.add(image.getImageUrl());
                    currentUser.setFotoPiaciute(imagesLiked);
                    FirebaseDatabase.getInstance().getReference("Users").child(userKey).setValue(currentUser);
                    HashSet<String> fotoPiaciute = new HashSet<>();
                    fotoPiaciute.addAll(imagesLiked);
                    sharedPreferencesDefault.edit().remove("IMAGES_LIKED").commit();
                    sharedPreferencesDefault.edit().putStringSet("IMAGES_LIKED", fotoPiaciute).commit();

                    binding.like.setImageResource(R.drawable.ic_baseline_bookmark_24);
                    Toast.makeText(ImageActivity.this, getString(R.string.liked), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


}