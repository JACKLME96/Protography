package com.example.protography.ui.Fragments.Search;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.protography.MainActivity;
import com.example.protography.R;
import com.example.protography.databinding.ActivityImageBinding;

import com.example.protography.ui.Models.Image;
import com.example.protography.ui.Models.User;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ModalBottomSheet extends BottomSheetDialogFragment {
    private ActivityImageBinding binding;
    private BottomSheetBehavior bottomSheetBehavior;
    private ImageView imagePrev;
    private TextView title;
    private TextView user;
    private TextView desc;
    private TextView equipment;
    private TextView camera;
    private TextView tips;
    private TextView bestTimeToGo;
    private TextView coords;
    private Image image;
    LinearLayout swipeUp;

    private List<String> imagesLiked;
    private User currentUser;
    private String userKey;


    private String latLng;

    private SharedPreferences sharedPreferences;
    private SharedPreferences sharedPreferencesDefault;
    private String TAG = "ModalBottomSheet";

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog bottomSheet = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        //inflating layout
        View view = View.inflate(getContext(), R.layout.activity_image, null);
        //setting layout with bottom sheet
        bottomSheet.setContentView(view);
        bottomSheetBehavior = BottomSheetBehavior.from((View) view.getParent());

        bottomSheetBehavior.setState(bottomSheetBehavior.STATE_COLLAPSED);
        bottomSheetBehavior.setHideable(true);
        bottomSheetBehavior.setPeekHeight(1000);

        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull @NotNull View bottomSheet, int newState) {
                if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED)
                {
                    imagePrev.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    ViewGroup.LayoutParams params= imagePrev.getLayoutParams();
                    params.height = 600;
                    imagePrev.setLayoutParams(params);
                    swipeUp.setVisibility(View.VISIBLE);

                    if(image.getImageTitle().length() > 15)
                        title.setText(image.getImageTitle().substring(0,15).concat("..."));
                }
                else if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
                {
                    imagePrev.setScaleType(ImageView.ScaleType.FIT_START);
                    ViewGroup.LayoutParams params= imagePrev.getLayoutParams();
                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    imagePrev.setLayoutParams(params);
                    swipeUp.setVisibility(View.GONE);
                    title.setText(image.getImageTitle());
                }
            }

            @Override
            public void onSlide(@NonNull @NotNull View bottomSheet, float slideOffset) {
                //TODO Aggiungere un resizing dinamico dell'immagine/testo per rendere più fluido
            }
        });

        return bottomSheet;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                                Bundle savedInstanceState) {
        binding = ActivityImageBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        imagePrev = binding.imageView;
        imagePrev.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ViewGroup.LayoutParams params= imagePrev.getLayoutParams();
        params.height = 600;
        imagePrev.setLayoutParams(params);

        title = binding.title;
        user = binding.user;
        desc = binding.description;
        equipment = binding.equipment;
        camera = binding.cameraSettings;
        tips = binding.tips;
        bestTimeToGo = binding.bestTimeToGo;
        coords = binding.coords;
        swipeUp = binding.swipeUp;


        sharedPreferencesDefault = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Set<String> imageL = sharedPreferencesDefault.getStringSet("IMAGES_LIKED", null);
        imagesLiked = new ArrayList<>();
        imagesLiked.addAll(imageL);
        userKey = sharedPreferencesDefault.getString("USER_KEY", null);
        String mail = sharedPreferencesDefault.getString("EMAIL", null);
        String fullName = sharedPreferencesDefault.getString("FULLNAME", null);
        currentUser = new User(fullName, mail);
        currentUser.setFotoPiaciute(imagesLiked);



        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        latLng = sharedPreferences.getString("LATLNG", "");
        Query query = FirebaseDatabase.getInstance().getReference("Images")
        .orderByChild("coords").equalTo(latLng);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    image = snapshot.getValue(Image.class);
                    Picasso.get().load(image.getImageUrl()).into(imagePrev);

                    if(image.getImageTitle().length() > 15)
                        title.setText(image.getImageTitle().substring(0,15) + "...");
                    else
                        title.setText(image.getImageTitle());

                    swipeUp.setVisibility(View.VISIBLE);
                    user.setText(image.getImageNameUser());
                    desc.setText(image.getImageDescription());
                    binding.description.setShowingLine(4);
                    binding.description.setShowMoreColor(getResources().getColor(R.color.DarkThemeGray));
                    binding.description.setShowLessTextColor(getResources().getColor(R.color.DarkThemeGray));
                    equipment.setText(image.getImageEquipment());
                    camera.setText(image.getImageSettings());
                    if (image.getImageTime() == null || image.getImageTime().isEmpty())
                        bestTimeToGo.setText("----------");
                    else
                        bestTimeToGo.setText(image.getImageTime());

                    if (image.getImageTips() == null || image.getImageTips().isEmpty())
                        tips.setText("----------");
                    else
                        tips.setText(image.getImageTips());

                    if (image.getImageCategory().equals("Nature"))
                        binding.chipNature.setChecked(true);
                    else if (image.getImageCategory().equals("Urban"))
                        binding.chipUrban.setChecked(true);
                    else
                        binding.chipPortrait.setChecked(true);

                    // Imposta il like in base a se è un'immagine preferita o meno
                    Log.d(TAG, "220-Immagini: " + imagesLiked);
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
                                Toast.makeText(getContext(), getString(R.string.disliked), Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(getContext(), getString(R.string.liked), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    coords.setOnClickListener(v -> {
                        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + image.getCoords());
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");

                        // Controlla che ci sia un applicazione per la navigazione
                        if (mapIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                            startActivity(mapIntent);
                        } else {
                            Toast.makeText(getContext(), R.string.errorMaps, Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }
}