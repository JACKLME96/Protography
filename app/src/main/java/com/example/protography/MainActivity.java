package com.example.protography;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.protography.databinding.ActivityMainBinding;
import com.example.protography.ui.AddActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private  static final String TAG = "MainActivity";
    private static final String ADD_ACTIVITY = "isAddActivity";
    private SharedPreferences sharedPreferences;
    BottomNavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();
        setContentView(root);

        sharedPreferences = getSharedPreferences("MYSHAREDPREF", Context.MODE_PRIVATE);

        navView = binding.navView;
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        NavigationUI.setupWithNavController(navView, navController);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //altrimenti tornando dall'attività di aggiunta sarebbe selezionato un item non corrispondente al fragment aperto.
        boolean isAddActivity = sharedPreferences.getBoolean(ADD_ACTIVITY, false);
        if (isAddActivity) {
            navView.setSelectedItemId(R.id.navigation_search);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove(ADD_ACTIVITY);
            editor.commit();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (navView.getSelectedItemId() == R.id.navigation_add) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(ADD_ACTIVITY, true);
            editor.commit();
        }
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