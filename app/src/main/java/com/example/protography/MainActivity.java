package com.example.protography;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;


import com.example.protography.ui.AddActivity;

import com.example.protography.ui.Fragments.MapsFragment;
import com.example.protography.ui.Fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

    Fragment fragment;
    boolean IsAddActivity = false;
    BottomNavigationView navView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navView.setOnNavigationItemReselectedListener(mOnNavigationItemReselectedListener);

        loadFragment(new MapsFragment());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(IsAddActivity) {
            navView.setSelectedItemId(R.id.navigation_search);
            //altrimenti tornando dall'attività di aggiunta sarebbe selezionato un item non corrispondente al fragment aperto.
            IsAddActivity = false;
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_add:
                    Intent intent = new Intent(MainActivity.this, AddActivity.class);
                    startActivity(intent);
                    IsAddActivity = true;
                    return true;
                case R.id.navigation_profile:
                    fragment = new ProfileFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_search:
                    fragment = new MapsFragment();
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }
    };

    private BottomNavigationView.OnNavigationItemReselectedListener mOnNavigationItemReselectedListener
            = new BottomNavigationView.OnNavigationItemReselectedListener() {
        @Override
        public void onNavigationItemReselected(@NonNull MenuItem item) {
            //on reselect do nothing
        }
    };

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this)
                .setTitle("Warning")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        MainActivity.super.onBackPressed();
                    }
                }).create().show();
    }
}