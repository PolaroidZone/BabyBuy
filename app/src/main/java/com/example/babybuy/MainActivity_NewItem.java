package com.example.babybuy;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.babybuy.model.Items;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class MainActivity_NewItem extends AppCompatActivity implements View.OnClickListener {
    ImageButton back, maps, camera, gallery;
    Button submit;

    EditText itemNameView, itemCostView, locationPin;
    TextInputEditText textFieldDescription;

    FirebaseAuth firebaseAuth;
    DatabaseReference database;
    ProgressDialog progress;

    private static final int GALLERY_CODE = 1;
    private static final int CAMERA_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new_item);

        Objects.requireNonNull(getSupportActionBar()).hide();



        firebaseAuth = FirebaseAuth.getInstance();
        progress = new ProgressDialog(MainActivity_NewItem.this);
        progress.setTitle("create Item");
        progress.setMessage("Creating item");
        progress.setCanceledOnTouchOutside(false);

        database = FirebaseDatabase.getInstance().getReference();

        //UI components
        itemNameView = findViewById(R.id.editTextItemName);
        itemCostView = findViewById(R.id.editTextItemAmount);
        textFieldDescription = findViewById(R.id.descriptionTextField);
        locationPin = findViewById(R.id.item_map_pin_new);

        back = findViewById(R.id.imageButtonBackNewItem);
        back.setOnClickListener(this);
        maps = findViewById(R.id.imageButtonGoogleMaps);
        maps.setOnClickListener(this);
        submit = findViewById(R.id.buttonSubmitNewItem);
        submit.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        String id = firebaseAuth.getUid();
        String name = itemNameView.getText().toString();
        String cost = itemCostView.getText().toString();
        String description = textFieldDescription.getText().toString();
        String geoTag = locationPin.getText().toString();

        int buttonId = view.getId();

        if (buttonId == R.id.imageButtonBackNewItem){
            Intent intent = new Intent(MainActivity_NewItem.this, MainActivity_Home.class);
            startActivity(intent);
            finish();
        }else if (buttonId == R.id.imageButtonGoogleMaps){
            if(locationPin.getText().toString().length() != 0){
                Toast.makeText(this, "open maps", Toast.LENGTH_SHORT).show();
                String locationString = locationPin.getText().toString();
                Uri gmmIntentUri = Uri.parse("geo:0,0?q="+locationString);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }else {
                Toast.makeText(MainActivity_NewItem.this, "there isn't any location stated", Toast.LENGTH_SHORT).show();
            }
        }else if (buttonId == R.id.buttonSubmitNewItem){
            if (itemNameView.getText().toString().length() == 0 ||
                    itemCostView.getText().toString().length() == 0 ||
                        textFieldDescription.getText().toString().length() == 0){
                Toast.makeText(this, "One or more of the fields is empty", Toast.LENGTH_SHORT).show();
            }else {
                createItem( id, name, cost, description, geoTag);
            }

        }

    }


    public void createItem(String id, String name, String cost, String description, String geoTag) {
        //method to crete a new item on the firebase database
        String uidItem = firebaseAuth.getUid();
        String itemId = uidItem+name;
        final Items item = new Items( id, itemId ,name, cost, description, geoTag);
        progress.show();

        database.child("Items")
                .child(id)
                .child(id+"pending")
                .child(id+name)
                .setValue(item)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                progress.dismiss();
                Intent intent = new Intent(MainActivity_NewItem.this, MainActivity_Home.class);
                startActivity(intent);
            }
        });


    }
}