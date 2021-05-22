package com.example.protography;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);

        mAuth = FirebaseAuth.getInstance();

        registerUser = (Button) findViewById(R.id.register);
        registerUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        editTextFullName = (EditText) findViewById(R.id.NomeCompleto);
        editTextEmail = (EditText) findViewById(R.id.registerAddress);
        editTextPassword = (EditText) findViewById(R.id.registerPassword);

    }

    private void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String nomeCompleto = editTextFullName.getText().toString().trim();

        if(nomeCompleto.isEmpty()){
            editTextFullName.setError("Nome non valido. il campo non deve essere vuoto.");
            editTextFullName.requestFocus();
            return;
        }

        if(password.isEmpty()){
            editTextPassword.setError("Password non valida. il campo non deve essere vuoto.");
            editTextPassword.requestFocus();
            return;
        }
        if(email.isEmpty()){
            editTextEmail.setError("Email non valida. il campo non deve essere vuoto.");
            editTextEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Mail non valida");
            editTextEmail.requestFocus();
            return;
        }

        if(password.length() < 6){
            editTextPassword.setError("Lunghezza minima della password è di 6 caratteri ");
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

                                        Toast.makeText(UserRegistration.this, "Utente registrato con successo! Ora puoi loggarti", Toast.LENGTH_LONG).show();

                                    } else {
                                        Toast.makeText(UserRegistration.this, "Registrazione fallita, Utente già esistente", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }else {
                            Toast.makeText(UserRegistration.this, "Registrazione fallita, Utente già esistente", Toast.LENGTH_LONG).show();

                        }
                    }
                });

    }

}