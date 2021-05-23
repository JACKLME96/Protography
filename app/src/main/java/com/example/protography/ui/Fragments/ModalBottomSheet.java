package com.example.protography.ui.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Layout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.protography.MainActivity;
import com.example.protography.R;
import com.example.protography.databinding.ActivityImageBinding;
import com.example.protography.databinding.FragmentModalBottomSheetBinding;
import com.example.protography.ui.ImageActivity;
import com.example.protography.ui.Models.Image;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

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

    private String position;

    private SharedPreferences sharedPreferences;
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
        bottomSheetBehavior.setPeekHeight(900);

        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull @NotNull View bottomSheet, int newState) {
                if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED)
                {
                    imagePrev.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    ViewGroup.LayoutParams params= imagePrev.getLayoutParams();
                    params.height = 600;
                    imagePrev.setLayoutParams(params);
                }
                else
                {
                    imagePrev.setScaleType(ImageView.ScaleType.FIT_START);
                    ViewGroup.LayoutParams params= imagePrev.getLayoutParams();
                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    imagePrev.setLayoutParams(params);
                }
            }

            @Override
            public void onSlide(@NonNull @NotNull View bottomSheet, float slideOffset) {
                //TODO Aggiungere un resizing dinamico dell'immagine per rendere più fluido
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

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        position = sharedPreferences.getString("POS", "DEFAULT");

        Query query = FirebaseDatabase.getInstance().getReference("Images")
                .orderByChild("coords").equalTo(position);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Image image = snapshot.getValue(Image.class);
                    Picasso.get().load(image.getImageUrl()).into(imagePrev);
                    title.setText(image.getImageTitle());
                    user.setText(((MainActivity) getActivity()).getNameUser());
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
        sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
    }
}