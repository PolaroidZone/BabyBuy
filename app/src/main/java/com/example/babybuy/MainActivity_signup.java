package com.example.babybuy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.babybuy.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class MainActivity_signup extends AppCompatActivity implements View.OnClickListener{

    Button signupButton, loginButton;
    EditText emailInput, usernameInput, passwordInput;

    protected FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_signup);

        //removing/ diding the default action bar
        Objects.requireNonNull(getSupportActionBar()).hide();

        //Initializing firebase authentication
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        progressDialog = new ProgressDialog(MainActivity_signup.this);
        progressDialog.setTitle("Create an account");
        progressDialog.setMessage("Creating your account...");

        signupButton = findViewById(R.id.buttonSignupSignup);
        signupButton.setOnClickListener(this);
        loginButton = findViewById(R.id.buttonLoginSignup);
        loginButton.setOnClickListener(this);
        emailInput = findViewById(R.id.editTextTextEmailSignup);
        usernameInput = findViewById(R.id.editTextUsernameSignup);
        passwordInput = findViewById(R.id.editTextTextPasswordSignup);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.buttonSignupSignup){
            //checking if all the InputTexts have been filled
            if (emailInput.getText().toString().length() == 0 ||
                    usernameInput.getText().toString().length() == 0 ||
                        passwordInput.getText().toString().length() == 0){
                Toast.makeText(this, "One or more of the text fields is/are empty", Toast.LENGTH_SHORT).show();
            }else {
                getCreateAccount();
            }
        }else if (id == R.id.buttonLoginSignup){
            Intent intent = new Intent(MainActivity_signup.this, MainActivity_Login.class);
            startActivity(intent);
            finish();
        }
    }

    private void getCreateAccount() {
        //zThis id the fire base authentication code.
        //It creates a user using email and password.
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(emailInput.getText().toString(), passwordInput.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    //when action is complete an onComplete method if triggered
                    //this method is responsible for creating a users a account
                    //it populates the the database with user details.
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()){
                    User user = new User(usernameInput.getText().toString(), emailInput.getText().toString(), passwordInput.getText().toString());
                    String userId = Objects.requireNonNull(Objects.requireNonNull(task.getResult()).getUser()).getUid();
                    firebaseDatabase.getReference().child("User").child(userId).setValue(user);

                    Toast.makeText(MainActivity_signup.this, "Account created successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity_signup.this, MainActivity_Home.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(MainActivity_signup.this, Objects.requireNonNull(task.getException())+"/n"+"check you internet connection", Toast.LENGTH_SHORT).show();
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