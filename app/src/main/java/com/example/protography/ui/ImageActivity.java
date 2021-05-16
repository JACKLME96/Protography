package com.example.protography.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.protography.databinding.ActivityImageBinding;
import com.example.protography.ui.Models.Image;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

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
        binding.description.setText(image.getImageDescription());
        binding.equipment.setText(image.getImageEquipment());
        binding.cameraSettings.setText(image.getImageSettings());
        if (image.getImageTime() == null || image.getImageTime().equals(""))
            binding.bestTimeToGo.setText("----------");
        else
            binding.bestTimeToGo.setText(image.getImageTime());
        if (image.getImageTips() == null || image.getImageTips().equals(""))
            binding.tips.setText("----------");
        else
            binding.tips.setText(image.getImageTips());
    }


}