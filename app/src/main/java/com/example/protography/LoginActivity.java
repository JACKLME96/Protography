package com.example.protography;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView register, forgotPassword;
    private EditText editTextEmail, editTextPassword;
    private Button signIn;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        register = (TextView) findViewById(R.id.register);
        register.setOnClickListener(this);

        signIn = (Button) findViewById(R.id.button);
        signIn.setOnClickListener(this);

        editTextEmail = (EditText) findViewById(R.id.emailAddress);
        editTextPassword = (EditText) findViewById(R.id.password);

        mAuth = FirebaseAuth.getInstance();
        forgotPassword =(TextView)findViewById(R.id.forgotPassword);
        forgotPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register:
                startActivity(new Intent( this, UserRegistration.class));
                break;
            case R.id.button:
                userLogin();
                break;
            case R.id.forgotPassword:
                startActivity(new Intent(this, ForgotPassword.class));
                break;

        }
    }

    private void userLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(email.isEmpty()){
            editTextEmail.setError("Campo email vuoto");
            editTextEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Email non valida");
            editTextEmail.requestFocus();
            return;
        }
        if(password.isEmpty()){
            editTextPassword.setError("Campo password vuoto.");
            editTextPassword.requestFocus();
            return;
        }
        if(password.length() < 6){
            editTextPassword.setError("Lunghezza minima psw non soddisfatta");
            editTextPassword.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }
                else
                    Toast.makeText(LoginActivity.this, "login fallito, controlla i dati inseriti.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}