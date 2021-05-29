package com.example.protography.ui.Activities.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.protography.R;
import com.example.protography.databinding.ActivityUserRegistrationBinding;
import com.example.protography.ui.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

public class UserRegistration extends AppCompatActivity {


    private TextView registerUser;
    private EditText editTextFullName, editTextEmail, editTextPassword;
    private ActivityUserRegistrationBinding binding;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserRegistrationBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();
        setContentView(root);

        mAuth = FirebaseAuth.getInstance();

        registerUser = binding.register;
        registerUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        editTextFullName = binding.NomeCompleto;
        editTextEmail = binding.registerAddress;
        editTextPassword = binding.registerPassword;

    }

    private void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String nomeCompleto = editTextFullName.getText().toString().trim();

        // Messaggi d'errore
        String emptyName = getString(R.string.empty_name);
        String longName = getString(R.string.long_name);
        String emptyPassword = getString(R.string.password_empty);
        String emptyMail = getString(R.string.empty_email);
        String invalidEmail = getString(R.string.invalid_email);
        String passwordLenght = getString(R.string.password_at_least_six);


        if(nomeCompleto.isEmpty()){
            editTextFullName.setError(emptyName);
            editTextFullName.requestFocus();
            return;
        }

        if(nomeCompleto.length() > 15){
            editTextFullName.setError(longName);
            editTextFullName.requestFocus();
            return;
        }

        if(password.isEmpty()){
            editTextPassword.setError(emptyPassword);
            editTextPassword.requestFocus();
            return;
        }
        if(email.isEmpty()){
            editTextEmail.setError(emptyMail);
            editTextEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError(invalidEmail);
            editTextEmail.requestFocus();
            return;
        }

        if(password.length() < 6){
            editTextPassword.setError(passwordLenght);
            editTextPassword.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user = new User(nomeCompleto,email);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        Toast.makeText(UserRegistration.this, getString(R.string.success_registration), Toast.LENGTH_LONG).show();
                                        finish();

                                    } else {
                                        Toast.makeText(UserRegistration.this, getString(R.string.user_already_existing), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }else {
                            Toast.makeText(UserRegistration.this, getString(R.string.user_already_existing), Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }
}