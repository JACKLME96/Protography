package com.example.protography.ui.Fragments.User;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.protography.databinding.FragmentUploadsTabBinding;
import com.example.protography.ui.Adapters.RecyclerViewAdapterFind;
import com.example.protography.ui.Models.Image;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class TabUploadsFragment extends Fragment {

    private static final String TAG = "TabUploadsFragment";
    private FragmentUploadsTabBinding binding;
    private SharedPreferences sharedPref;
    private String nameUser;
    private RecyclerViewAdapterFind recyclerViewAdapter;
    private List<Image> imageList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        nameUser = sharedPref.getString("FULLNAME", null);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentUploadsTabBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imageList = new ArrayList<>();


        RecyclerView recyclerView = binding.recyclerViewUploads;
        recyclerViewAdapter = new RecyclerViewAdapterFind(imageList, getContext(),false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(recyclerViewAdapter);

        // Ricerco le immagini che l'utente attuale ha aggiunto
        Query query = FirebaseDatabase.getInstance().getReference("Images").orderByChild("imageNameUser").equalTo(nameUser);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                imageList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Image image = dataSnapshot.getValue(Image.class);
                    imageList.add(image);
                }

                if (imageList.size() == 0)
                    binding.noImages.setVisibility(View.VISIBLE);
                else
                    binding.noImages.setVisibility(View.GONE);

                recyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "Errore caricamento immagini: " + error.getMessage());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d(TAG, "RESUME");
        boolean newUsername = sharedPref.getBoolean("NAMECHANGED", false);
        if (newUsername) {

            Log.d(TAG, "RESUME, new username");

            nameUser = sharedPref.getString("FULLNAME", null);
            sharedPref.edit().remove("NAMECHANGED").apply();


            Query query = FirebaseDatabase.getInstance().getReference("Images").orderByChild("imageNameUser").equalTo(nameUser);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    imageList.clear();

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Image image = dataSnapshot.getValue(Image.class);
                        imageList.add(image);
                    }

                    if (imageList.size() == 0)
                        binding.noImages.setVisibility(View.VISIBLE);
                    else
                        binding.noImages.setVisibility(View.GONE);

                    recyclerViewAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d(TAG, "Errore caricamento immagini: " + error.getMessage());
                }
            });
        }
    }
}