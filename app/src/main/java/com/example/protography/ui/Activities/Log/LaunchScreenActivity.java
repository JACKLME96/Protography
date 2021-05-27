package com.example.protography.ui.Activities.Log;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.protography.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;


public class LaunchScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chooseActivityToLaunch();
    }

    private void chooseActivityToLaunch() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String mail = sharedPref.getString("EMAIL", null);
        String password = sharedPref.getString("PSW", null);

        if (mail != null){
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            mAuth.signInWithEmailAndPassword(mail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        startActivity(new Intent(LaunchScreenActivity.this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(LaunchScreenActivity.this, "Something went wrong, login again", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LaunchScreenActivity.this, LoginActivity.class));
                        finish();
                    }
                }
            });
        }
        else {
            startActivity(new Intent(LaunchScreenActivity.this, LoginActivity.class));
            finish();
        }
    }
}