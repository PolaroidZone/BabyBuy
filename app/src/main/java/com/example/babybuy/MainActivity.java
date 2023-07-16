package com.example.babybuy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    FirebaseAuth firebaseAuth;

    Button signup, login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initializing firebase authentication
        firebaseAuth = FirebaseAuth.getInstance();

        signup = findViewById(R.id.signupButton);
        signup.setOnClickListener(this);
        login = findViewById(R.id.loginButton);
        login.setOnClickListener(this);


    }

    @Override
    protected void onStart() {
        super.onStart();
        if(firebaseAuth.getCurrentUser() != null){
            Intent intent = new Intent(MainActivity.this, MainActivity_Home.class);
            startActivity(intent);
            finish();
        }

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.loginButton){
            Intent intent = new Intent(MainActivity.this, MainActivity_Login.class);
            startActivity(intent);
            finish();
        }else if (id == R.id.signupButton){
            Intent intent = new Intent(MainActivity.this, MainActivity_signup.class);
            startActivity(intent);
            finish();
        }
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