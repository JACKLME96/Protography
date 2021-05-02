package com.example.protography.ui.Fragments;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.protography.MainActivity;
import com.example.protography.R;
import com.example.protography.ui.ViewModels.AddViewModel;

public class AddActivity extends AppCompatActivity {

    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        sharedPref = getPreferences(Context.MODE_PRIVATE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Add a spot");
        setSupportActionBar(toolbar);

        loadFragment(new ImageSelectFragment(), "ImageSelectFragment");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_bar_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_forward:
                if(getSupportFragmentManager().findFragmentByTag("ImageSelectFragment") != null
                        && getSupportFragmentManager().findFragmentByTag("ImageSelectFragment").isVisible()) {
                    loadFragment(new PlaceSelectFragment(), "PlaceSelectFragment");
                }
                else
                    loadFragment(new AddImageDetailsFragment(), "AddImageDetailsFragment");
                break;
        }
        return true;
    }

    private void loadFragment(Fragment fragment, String tag) {
        // load fragment
        //(if tag != )
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction
                .replace(R.id.frame_container, fragment, tag)
                .addToBackStack(tag)
                .commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sharedPref.edit().clear().apply();
    }
}