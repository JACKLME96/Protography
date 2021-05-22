package com.example.protography.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
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

    private FirebaseAuth mAuth;
=======
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private static final String TAG = "LoginActivity";
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        register = (TextView) findViewById(R.id.register);
        register.setOnClickListener(this);

        signIn =  findViewById(R.id.button);
        signIn.setOnClickListener(this);

        editTextEmail =  findViewById(R.id.emailAddress);
        editTextPassword = findViewById(R.id.password);
        remember = findViewById(R.id.remember);

        mAuth = FirebaseAuth.getInstance();
        forgotPassword =findViewById(R.id.forgotPassword);
        forgotPassword.setOnClickListener(this);

        sharedPref = this.getPreferences(Context.MODE_PRIVATE);

        if(!sharedPref.getString("EMAIL", "").isEmpty() && !sharedPref.getString("PSW", "").isEmpty())
            userLogin();
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
        String email = editTextEmail.getText().toString().trim().isEmpty() ? sharedPref.getString("EMAIL", "DEFAULT") : editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim().isEmpty() ?  sharedPref.getString("PSW", "DEFAULT") : editTextPassword.getText().toString().trim();;

        if(email.isEmpty()){
            editTextEmail.setError("Campo email vuoto");
            editTextEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Email non valida. Utente non registrato.");
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

        mAuth.signInWithEmailAndPassword(email ,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                //Login avvenuto con successo
                if(task.isSuccessful()){
<<<<<<< HEAD
                    if(remember.isChecked()){
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("EMAIL", email);
                        editor.putString("PSW", password);
                        editor.apply();
                    }
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
=======
                    startMainActivity();
>>>>>>> develop
                }
                //Login Fallito, credenziali errate
                else
                    Toast.makeText(LoginActivity.this, "Login fallito, controlla i dati inseriti.", Toast.LENGTH_SHORT).show();
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

                String nameUser = "Nessun User";
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