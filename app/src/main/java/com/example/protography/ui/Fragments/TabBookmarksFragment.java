package com.example.protography.ui.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.protography.R;
import com.example.protography.databinding.FragmentUploadsTabBinding;


public class TabBookmarksFragment extends Fragment {

    FragmentUploadsTabBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentUploadsTabBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        return view;
    }
}