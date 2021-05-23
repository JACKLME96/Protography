package com.example.protography.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Patterns;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.protography.ForgotPassword;
import com.example.protography.MainActivity;
import com.example.protography.R;
import com.example.protography.UserRegistration;
import com.example.protography.databinding.ActivityLoginBinding;
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
    private CheckBox remember;
    private SharedPreferences sharedPref;
    private ActivityLoginBinding binding;
    private static final String TAG = "LoginActivity";
    private DatabaseReference databaseReference;

    private String mail,password;


    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();
        setContentView(root);

        register =  binding.register;
        register.setOnClickListener(this);

        signIn =  binding.button;
        signIn.setOnClickListener(this);

        editTextEmail =  binding.emailAddress;
        editTextPassword = binding.password;
        remember = binding.remember;

        mAuth = FirebaseAuth.getInstance();
        forgotPassword =binding.forgotPassword;
        forgotPassword.setOnClickListener(this);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

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
         mail = editTextEmail.getText().toString().trim().isEmpty() ? sharedPref.getString("EMAIL", null) : editTextEmail.getText().toString().trim();
         password = editTextPassword.getText().toString().trim().isEmpty() ? sharedPref.getString("PSW", null) : editTextPassword.getText().toString().trim();

        if (mail.isEmpty()) {
            editTextEmail.setError("Campo email vuoto");
            editTextEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
            editTextEmail.setError("Email non valida. Utente non registrato.");
            editTextEmail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            editTextPassword.setError("Campo password vuoto.");
            editTextPassword.requestFocus();
            return;
        }
        if (password.length() < 6) {
            editTextPassword.setError("Lunghezza minima psw non soddisfatta");
            editTextPassword.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(mail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                //Login avvenuto con successo
                if (task.isSuccessful()) {
                    if (remember.isChecked()) {
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("EMAIL", mail);
                        editor.putString("PSW", password);
                        editor.apply();
                    }
                    startMainActivity();
                }
                //Login Fallito, credenziali errate
                else {
                    YoYo.with(Techniques.Shake)
                            .duration(300)
                            .repeat(2)
                            .playOn(editTextEmail);
                    YoYo.with(Techniques.Shake)
                            .duration(300)
                            .repeat(2)
                            .playOn(editTextPassword);
                    editTextPassword.getText().clear();
                    Toast.makeText(LoginActivity.this, "Login fallito, controlla i dati inseriti.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void startMainActivity() {

        AlertDialog dialog =  new AlertDialog.Builder(this)
                .setView(R.layout.loading_dialog)
                .setCancelable(false)
                .create();
        dialog.show();

        // Cerco il nome dell'utente da passare nell'intent
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String nameUser = "Nessun User";
                // Cerco i dati dall'utente loggato
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User u = dataSnapshot.getValue(User.class);
                    if (u.getEmail().equals(mAuth.getCurrentUser().getEmail())) {
                        nameUser = u.getFullName();
                        break;
                    }
                }

                // Una volta trovato l'utente faccio partire la mainActivity con i suoi dati
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("mailUser", mAuth.getCurrentUser().getEmail());
                intent.putExtra("nameUser", nameUser);

                startActivity(intent);
                dialog.dismiss();
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}