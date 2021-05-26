package com.example.protography.ui;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.widget.TextView;

import com.example.protography.R;
import com.example.protography.databinding.ActivityImageBinding;
import com.example.protography.ui.Models.Image;
import com.squareup.picasso.Picasso;

public class ImageActivity extends AppCompatActivity {

    private static final String TAG = "ImageActivity";
    ActivityImageBinding binding;
    Image image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityImageBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();
        setContentView(root);

        image = (Image) getIntent().getSerializableExtra("Immagine");

        // Carica dati immagine
        Picasso.get().load(image.getImageUrl()).into(binding.imageView);
        binding.user.setText("User");
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
        binding.description.setShowMoreColor(getResources().getColor(R.color.DarkThemeGray));
        binding.description.setShowLessTextColor(getResources().getColor(R.color.DarkThemeGray));

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


        binding.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ImageActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }


}