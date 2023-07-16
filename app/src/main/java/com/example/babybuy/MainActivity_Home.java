package com.example.babybuy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.babybuy.adapter.FragmentAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class MainActivity_Home extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth firebaseAuth;
    ViewPager viewPager;
    TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home);

        Objects.requireNonNull(getSupportActionBar()).setElevation(0);
        //getting firebase authentication.
        firebaseAuth = FirebaseAuth.getInstance();

        FloatingActionButton floatingActionButton = findViewById(R.id.floating_add_button_home);
        floatingActionButton.setOnClickListener(this);
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager()));
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        if(firebaseAuth.getCurrentUser() == null){
            Intent intent = new Intent(MainActivity_Home.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflating the item menu
        getMenuInflater().inflate(R.menu.shopping_list_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //creating the menu item actions
        switch (item.getItemId()){
            case R.id.more_options:
                Intent intentAc = new Intent(MainActivity_Home.this, MainActivity_Settings.class);
                startActivity(intentAc);
                return true;
            case R.id.logout_option:
                setLogout();
                return true;
            case R.id.about_app:
                Intent intentAbout = new Intent(MainActivity_Home.this, MainActivity_About.class);
                startActivity(intentAbout);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        int buttonId = view.getId();

        if (buttonId == R.id.floating_add_button_home){
            Intent intent = new Intent(MainActivity_Home.this, MainActivity_NewItem.class);
            startActivity(intent);
        }
    }

    protected void setLogout(){
        firebaseAuth.signOut();
        Toast.makeText(this, "Logging out", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity_Home.this, MainActivity.class);
        startActivity(intent);
    }
    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}