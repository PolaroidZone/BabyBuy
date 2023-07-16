package com.example.babybuy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaParser;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class MainActivity_Login extends AppCompatActivity implements View.OnClickListener{

    Button loginButton, signupButton;
    EditText emailField, passwordField;

    private FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);

        Objects.requireNonNull(getSupportActionBar()).hide();

        //Initializing firebase authentication
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        progressDialog = new ProgressDialog(MainActivity_Login.this);
        progressDialog.setTitle("Login");
        progressDialog.setMessage("Validating...");

        loginButton = findViewById(R.id.buttonLoginLogin);
        loginButton.setOnClickListener(this);
        signupButton = findViewById(R.id.buttonSignupLogin);
        signupButton.setOnClickListener(this);

        emailField = findViewById(R.id.editTextTextEmailLogin);
        passwordField = findViewById(R.id.editTextTextPasswordLogin);

        if(firebaseAuth.getCurrentUser() != null){
            Intent intent = new Intent(MainActivity_Login.this, MainActivity_Home.class);
            startActivity(intent);
        }

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.buttonLoginLogin){
            //checking if all InputTexts are filled
            if (emailField.getText().toString().length() == 0 ||
                    passwordField.getText().toString().length() == 0){
                Toast.makeText(this, "One or more of the text fields are empty", Toast.LENGTH_SHORT).show();
            }else {
                getAuthentication();
            }
        }else if (id == R.id.buttonSignupLogin){
            Intent intent = new Intent(MainActivity_Login.this, MainActivity_signup.class);
            startActivity(intent);
        }
    }

    private void getAuthentication() {
        progressDialog.show();
        //fire base user authentication
        //uses email and password verification.
        firebaseAuth.signInWithEmailAndPassword(emailField.getText().toString(), passwordField.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if(task.isSuccessful()){
                    //intent to open the home activity
                    Toast.makeText(MainActivity_Login.this, "Longin success", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity_Login.this, MainActivity_Home.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(MainActivity_Login.this, task.getException().toString()+"/n"+"Check if you have internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Click back again to exit", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

}