package com.example.protography.ui.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.protography.R;
import com.example.protography.ui.Adapters.RecyclerViewAdapter;
import com.example.protography.ui.Models.Image;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class TabUploadsFragment extends Fragment {

    private static final String TAG = "TabUploadsFragment";
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference root = database.getReference().child("Images");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_uploads_tab, container, false);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<Image> imageList = new ArrayList<>();


        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_uploads);
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(imageList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(recyclerViewAdapter);

        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Image image = dataSnapshot.getValue(Image.class);
                    imageList.add(image);
                }

                recyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}