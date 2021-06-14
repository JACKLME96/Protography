package com.example.protography.ui.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.protography.R;
import com.example.protography.databinding.FragmentFindBinding;
import com.example.protography.databinding.FragmentUploadsTabBinding;
import com.example.protography.ui.Adapters.AdapterDiscover;
import com.example.protography.ui.Adapters.RecyclerViewAdapterFind;
import com.example.protography.ui.Models.Image;
import com.google.android.material.chip.Chip;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FindFragment extends Fragment {

    private static final String TAG = "FindFragment";
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseRoot = database.getReference();
    private FragmentFindBinding binding;
    private Chip chip1, chip2, chip3;
    private Query query;
    RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFindBinding.inflate(inflater,container,false);

        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String nameUser;
        nameUser = prefs.getString("FULLNAME", null);

        List<Image> imageList = new ArrayList<>();
        List<String>selectedChip = new ArrayList<>();
        query = FirebaseDatabase.getInstance().getReference("Images");
        chip1 = binding.chip1;
        chip2 = binding.chip2;
        chip3 = binding.chip3;
        recyclerView = binding.recyclerViewFind;
        swipeRefreshLayout = binding.swipeFind;

        AdapterDiscover adapterDiscover = new AdapterDiscover (imageList, getContext(),true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        recyclerView.setLayoutManager(layoutManager);

        SnapHelper snapHelper = new PagerSnapHelper();
        recyclerView.setLayoutManager(layoutManager);
        snapHelper.attachToRecyclerView(recyclerView);

        recyclerView.setAdapter(adapterDiscover);


        CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    selectedChip.add(buttonView.getText().toString());
                    load(query, imageList, adapterDiscover, selectedChip, nameUser);
                }
                else {
                    selectedChip.remove(buttonView.getText().toString());
                    load(query, imageList, adapterDiscover, selectedChip, nameUser);
                }
            }
        };

        chip1.setOnCheckedChangeListener(checkedChangeListener);
        chip2.setOnCheckedChangeListener(checkedChangeListener);
        chip3.setOnCheckedChangeListener(checkedChangeListener);

        load(query, imageList, adapterDiscover, selectedChip, nameUser);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                load(query, imageList, adapterDiscover, selectedChip, nameUser);
                recyclerView.setAdapter(adapterDiscover);
            }
        });
    }

    void load(Query query, List<Image> imageList, AdapterDiscover adapterDiscover, List<String> selectedChip, String nameUser) {
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                imageList.clear();
                if(snapshot.exists() && snapshot.getChildrenCount() > 0) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Image image = dataSnapshot.getValue(Image.class);
                        switch (selectedChip.size()) {
                            case 0 :
                                if(!image.getImageNameUser().equals(nameUser))
                                    imageList.add(image);
                                break;

                            case 1 :
                                if(!image.getImageNameUser().equals(nameUser))
                                    if(image.getImageCategory().equals(selectedChip.get(0)))
                                        imageList.add(image);
                                break;

                            case 2 :
                                if(!image.getImageNameUser().equals(nameUser))
                                    if(image.getImageCategory().equals(selectedChip.get(0)) || image.getImageCategory().equals(selectedChip.get(1)))
                                        imageList.add(image);
                                break;

                            case 3 :
                                if(!image.getImageNameUser().equals(nameUser))
                                    if(image.getImageCategory().equals(selectedChip.get(0)) || image.getImageCategory().equals(selectedChip.get(1)) || image.getImageCategory().equals(selectedChip.get(2)))
                                        imageList.add(image);
                                break;
                        }
                    }
                    Collections.shuffle(imageList);
                    adapterDiscover.notifyDataSetChanged();
                    recyclerView.setAdapter(adapterDiscover);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "Errore caricamento immagini: " + error.getMessage());
            }
        });
    }
}