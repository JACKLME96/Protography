package com.example.protography.ui.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.protography.R;
import com.example.protography.databinding.FragmentBookmarksTabBinding;
import com.example.protography.databinding.FragmentUploadsTabBinding;
import com.example.protography.ui.Adapters.RecyclerViewAdapter;
import com.example.protography.ui.Models.Image;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class TabBookmarksFragment extends Fragment {

    private static final String TAG = "TabBookmarksFragment";
    private FragmentBookmarksTabBinding binding;
    private SharedPreferences sharedPref;
    private String nameUser;

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
        binding = FragmentBookmarksTabBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<Image> imageList = new ArrayList<>();


        RecyclerView recyclerView = binding.recyclerViewBookmarks;
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(imageList, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(recyclerViewAdapter);

        // Ricerco le immagini che l'utente ha messo come preferite
        // TODO
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
                    binding.noImages.setVisibility(View.VISIBLE);

                recyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}