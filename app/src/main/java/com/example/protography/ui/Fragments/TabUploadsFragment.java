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

import java.util.ArrayList;
import java.util.List;


public class TabUploadsFragment extends Fragment {

    private static final String TAG = "TabUploadsFragment";

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
        // Aggiunta di immagine di prova per vedere la visualizzazione dei titoli
        for (int i = 0; i < 30; i++) {
            Image image = new Image();
            image.setImageTitle("Titolo " + (i + 1));
            imageList.add(image);
        }

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_uploads);
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(imageList, new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Image image) {
                Log.d(TAG, image.getImageTitle());
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(recyclerViewAdapter);
    }
}