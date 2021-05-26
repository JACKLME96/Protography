package com.example.protography.ui.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.example.protography.R;
import com.example.protography.databinding.FragmentProfileBinding;
import com.example.protography.ui.Adapters.ProfileAdapter;
import com.example.protography.ui.LoginActivity;
import com.example.protography.ui.Models.Image;
import com.example.protography.ui.ViewModels.MapsViewModel;
import com.example.protography.ui.ViewModels.ProfileViewModel;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private FragmentProfileBinding binding;
    private String nameUser;
    private Button logout;
    SharedPreferences sharedPref;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        nameUser = sharedPref.getString("FULLNAME", null);
        binding.textViewUsername.setText(nameUser);
        logout = binding.logout;

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                sharedPref.edit().remove("EMAIL").apply();
                sharedPref.edit().remove("PSW").apply();
                sharedPref.edit().remove("FULLNAME").apply();
                sharedPref.edit().remove("REMEMBER").apply();
                getActivity().finish();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        mTabLayout = binding.tabLayout;
        // Si aggiungono i tab con il loro titolo che viene mostrato
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.tab_upload));
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.tab_bookmarks));
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        mViewPager = binding.viewPager;

        ProfileAdapter adapter = new ProfileAdapter(getParentFragmentManager(), mTabLayout.getTabCount());
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return root;
    }


}