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
import com.example.protography.ui.ViewModels.ProfileViewModel;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class ProfileFragment extends Fragment {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_profile, container, false);


        mTabLayout = root.findViewById(R.id.tab_layout);
        // Si aggiungono i tab con il loro titolo che viene mostrato
        mTabLayout.addTab(mTabLayout.newTab().setText("Uploads"));
        mTabLayout.addTab(mTabLayout.newTab().setText("Bookmarks"));
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        mViewPager = root.findViewById(R.id.view_pager);

        MainAdapter adapter = new MainAdapter(getParentFragmentManager(), mTabLayout.getTabCount());
        mViewPager.setAdapter(adapter);
        mViewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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


// Tipo estensione
    private class MainAdapter extends FragmentStatePagerAdapter {

        private int numeroPagine;

        public MainAdapter(@NonNull FragmentManager fm, int numeroPagine) {
            super(fm);
            this.numeroPagine = numeroPagine;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    TabUploadsFragment tabUploads = new TabUploadsFragment();
                    return tabUploads;
                case 1:
                    TabBookmarksFragment tabBookmark = new TabBookmarksFragment();
                    return tabBookmark;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return numeroPagine;
        }


    }
}