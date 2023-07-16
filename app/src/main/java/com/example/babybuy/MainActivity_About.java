package com.example.babybuy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class MainActivity_About extends AppCompatActivity implements View.OnClickListener{

    ImageButton backButton;
    TextView versionTextview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_about);

        Objects.requireNonNull(getSupportActionBar()).hide();

        backButton = findViewById(R.id.imageButtonBackAbout);
        backButton.setOnClickListener(this);
        versionTextview = findViewById(R.id.textViewVersion);
        versionTextview.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        int buttonId = view.getId();

        if (buttonId == R.id.imageButtonBackAbout){
            Intent intent = new Intent(MainActivity_About.this, MainActivity_Home.class);
            startActivity(intent);
            finish();
        }else if (buttonId == R.id.textViewVersion){
            Toast.makeText(this, "BabyBuy 1.0", Toast.LENGTH_SHORT).show();
        }
    }
}