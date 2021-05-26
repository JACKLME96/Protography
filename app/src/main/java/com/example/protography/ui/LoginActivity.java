package com.example.protography.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.protography.ForgotPassword;
import com.example.protography.MainActivity;
import com.example.protography.R;
import com.example.protography.UserRegistration;
import com.example.protography.databinding.ActivityLoginBinding;
import com.example.protography.databinding.ActivityMainBinding;
import com.example.protography.ui.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView register, forgotPassword;
    private EditText editTextEmail, editTextPassword;
    private Button signIn;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private static final String TAG = "LoginActivity";
    private ActivityLoginBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();
        setContentView(root);
        //setContentView(R.layout.activity_login);

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

        // Messaggi d'errore
        String fieldMailIsEmpty = getString(R.string.field_email_is_empty);
        String invalidEmail = getString(R.string.email_not_valid_unregistered_user);
        String emptyPassword = getString(R.string.field_password_empty);
        String minLenghtPassword = getString(R.string.password_at_least_six);

        if(email.isEmpty()){
            editTextEmail.setError(fieldMailIsEmpty);
            editTextEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError(invalidEmail);
            editTextEmail.requestFocus();
            return;
        }
        if(password.isEmpty()){
            editTextPassword.setError(emptyPassword);
            editTextPassword.requestFocus();
            return;
        }
        if(password.length() < 6){
            editTextPassword.setError(minLenghtPassword);
            editTextPassword.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                //Login avvenuto con successo
                if(task.isSuccessful()){
                    startMainActivity();
                }
                //Login Fallito, credenziali errate
                else
                    Toast.makeText(LoginActivity.this, R.string.login_failed_check_data, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void startMainActivity() {

        // Metto simbolo di caricamento
        binding.button.setVisibility(View.GONE);
        binding.loading.setVisibility(View.VISIBLE);

        // Cerco il nome dell'utente da passare nell'intent
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String nameUser = "";
                // Cerco i dati dall'utente loggato
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User u = dataSnapshot.getValue(User.class);
                    if (u.email.equals(mAuth.getCurrentUser().getEmail())) {
                        nameUser = u.fullName;
                        break;
                    }
                }

                // Una volta trovato l'utente faccio partire la mainActivity con i suoi dati
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("mailUser", mAuth.getCurrentUser().getEmail());
                intent.putExtra("nameUser", nameUser);

                startActivity(intent);
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}