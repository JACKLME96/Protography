package com.example.protography.ui.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.protography.R;
import com.example.protography.databinding.FragmentModalBottomSheetBinding;
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
    private FragmentModalBottomSheetBinding binding;
    private BottomSheetBehavior bottomSheetBehavior;
    private ImageView imagePrev;
    private TextView title;
    private TextView user;
    private TextView desc;
    private TextView equipment;
    private TextView camera;
    private TextView tips;
    private TextView bestTimeToGo;
    private String position;

    private SharedPreferences sharedPreferences;
    private String TAG = "MBS";

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog bottomSheet = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        //inflating layout
        View view = View.inflate(getContext(), R.layout.fragment_modal_bottom_sheet, null);
        //setting layout with bottom sheet
        bottomSheet.setContentView(view);
        bottomSheetBehavior = BottomSheetBehavior.from((View) (view.getParent()));

        bottomSheetBehavior.setPeekHeight(BottomSheetBehavior.PEEK_HEIGHT_AUTO);
        
        return bottomSheet;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                                Bundle savedInstanceState) {
        binding = FragmentModalBottomSheetBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        imagePrev = binding.imageView;
        title = binding.title;
        user = binding.user;
        desc = binding.description;
        equipment = binding.equipment;
        camera = binding.cameraSettings;
        tips = binding.tips;
        bestTimeToGo = binding.bestTimeToGo;

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        position = sharedPreferences.getString("POS", "DEFAULT");
        Log.d(TAG, position);

        Query query = FirebaseDatabase.getInstance().getReference("Images")
                .orderByChild("coords").equalTo(position);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Image image = snapshot.getValue(Image.class);
                    Picasso.get().load(image.getImageUrl()).into(imagePrev);
                    title.setText(image.getImageTitle());
                    user.setText("User");
                    desc.setText(image.getImageDescription());
                    equipment.setText(image.getImageEquipment());
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