package com.example.protography.ui.Fragments.User;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.protography.MainActivity;
import com.example.protography.R;
import com.example.protography.databinding.FragmentBookmarksTabBinding;
import com.example.protography.databinding.FragmentUploadsTabBinding;
import com.example.protography.ui.Adapters.RecyclerViewAdapter;
import com.example.protography.ui.Adapters.RecyclerViewAdapterFind;
import com.example.protography.ui.Models.Image;
import com.example.protography.ui.Models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class TabBookmarksFragment extends Fragment {

    private static final String TAG = "TabBookmarksFragment";
    private FragmentBookmarksTabBinding binding;
    private SharedPreferences sharedPref;
    private String nameUser;
    private List<Image> allImages;
    private List<String> imagesLiked;
    private List<Image> imagesToShow;
    private RecyclerViewAdapterFind recyclerViewAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            allImages = savedInstanceState.getParcelableArrayList("ALL_IMAGES");
        }
        else {
            allImages = ((MainActivity) getActivity()).getAllImages();
        }
        imagesToShow = new ArrayList<>();
        updateImagesToShow();


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


        RecyclerView recyclerView = binding.recyclerViewBookmarks;
        recyclerViewAdapter = new RecyclerViewAdapterFind(imagesToShow, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(recyclerViewAdapter);


        if (imagesToShow.size() == 0)
            binding.noImages.setVisibility(View.VISIBLE);
        else
            binding.noImages.setVisibility(View.VISIBLE);

        recyclerViewAdapter.notifyDataSetChanged();

    }

    @Override
    public void onResume() {
        super.onResume();

        updateImagesToShow();
        recyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSaveInstanceState(@NonNull @NotNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList("ALL_IMAGES", (ArrayList<Image>) allImages);
    }

    public void updateImagesToShow() {

        sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        nameUser = sharedPref.getString("FULLNAME", null);
        Set<String> imageL = sharedPref.getStringSet("IMAGES_LIKED", null);
        imagesLiked = new ArrayList<>();
        imagesLiked.addAll(imageL);

        imagesToShow.clear();

        if (allImages == null)
            Log.d(TAG, "ERRORE LISTA NULLA");
        else {
            for (Image i : allImages) {
                for (String url : imagesLiked) {
                    if (i.getImageUrl().equals(url))
                        imagesToShow.add(i);
                }
            }
        }
    }
}