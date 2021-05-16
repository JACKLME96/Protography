package com.example.protography.ui.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.example.protography.R;
import com.example.protography.databinding.FragmentProfileBinding;
import com.example.protography.ui.Adapters.ProfileAdapter;
import com.example.protography.ui.ViewModels.ProfileViewModel;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class ProfileFragment extends Fragment {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private FragmentProfileBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

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