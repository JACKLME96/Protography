package com.example.protography;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Button;
import android.widget.Toast;

import com.example.protography.databinding.ActivityForgotPasswordBinding;
import com.example.protography.databinding.ActivityImageBinding;
import com.example.protography.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

public class ForgotPassword extends AppCompatActivity {

    private EditText emailEditText;
    private ActivityForgotPasswordBinding binding;
    private Button resetPasswordButton;
    

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();
        setContentView(root);


        emailEditText = binding.email;
        resetPasswordButton = binding.resetButton;


        auth = FirebaseAuth.getInstance();

        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }

            private void resetPassword() {

                String email = emailEditText.getText().toString().trim();

                if(email.isEmpty()){
                    emailEditText.setError("Email richiesta.");
                    emailEditText.requestFocus();
                    return;
                }

                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    emailEditText.setError("Email non valida. Riprovate");
                    emailEditText.requestFocus();
                    return;
                }


                auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(ForgotPassword.this, "Controllare la mail per il reset della password.", Toast.LENGTH_LONG).show();
                            finish();
                        }
                        else
                            Toast.makeText(ForgotPassword.this, "Errore durante il recupero della password. Ricontrollare i dati.", Toast.LENGTH_LONG).show();
                    }
                });

            }
        });

    }

}