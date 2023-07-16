package com.example.babybuy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.babybuy.model.ItemImage;
import com.example.babybuy.model.Items;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

public class MainActivity_ViewItem extends AppCompatActivity implements View.OnClickListener{
    private static final int GALLERY_CODE = 1;
    private static final int CAMERA_CODE = 2;

    ItemImage itemImage = new ItemImage();

    ImageButton camera, gmaps, gallery;
    Button updateButton;
    EditText itemName, itemCost, locationPin;
    TextInputEditText descriptionField;
    ImageView imageView;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    ProgressDialog progress;
    FirebaseStorage storage;

    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view_item);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        camera = findViewById(R.id.view_item_camera_btn);
        camera.setOnClickListener(this);
        gmaps = findViewById(R.id.view_maps_btn);
        gmaps.setOnClickListener(this);
        updateButton = findViewById(R.id.view_item_update_btn);
        updateButton.setOnClickListener(this);
        gallery = findViewById(R.id.view_item_gallery_btn);
        gallery.setOnClickListener(this);

        itemName = findViewById(R.id.view_item_name);
        itemCost = findViewById(R.id.view_itme_cost);
        locationPin = findViewById(R.id.item_map_pin);
        descriptionField = findViewById(R.id.view_item_description);
        imageView =  findViewById(R.id.roundedImageView);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        progress = new ProgressDialog(MainActivity_ViewItem.this);
        progress.setTitle("Update item");
        progress.setMessage("Updating your item...");
        progress.setCanceledOnTouchOutside(false);


        String viewItemName = getIntent().getStringExtra("itemName");
        String viewItemCost = getIntent().getStringExtra("itemCost");
        String viewItemDescription = getIntent().getStringExtra("itemDescription");
        String viewMapsLocation = getIntent().getStringExtra("ItemLocation");
        String itemImage = getIntent().getStringExtra("itemImage");

        itemName.setText(viewItemName);
        itemCost.setText(viewItemCost);
        descriptionField.setText(viewItemDescription);
        locationPin.setText(viewMapsLocation);

        Picasso.get().load(itemImage).placeholder(R.drawable.shopping_bag_2).into(imageView);

        if(firebaseAuth.getCurrentUser() == null){
            Intent intent = new Intent(MainActivity_ViewItem.this, MainActivity.class);
            startActivity(intent);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.view_item_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.view_menu_share_item:
                Toast.makeText(MainActivity_ViewItem.this, "share", Toast.LENGTH_SHORT).show();
                setShareItem();
                return true;
            case R.id.view_menu_delete_item:
                Toast.makeText(MainActivity_ViewItem.this, "Deleting", Toast.LENGTH_SHORT).show();
                setDeleteItem();
                return true;
            case R.id.view_menu_logout:
                Toast.makeText(MainActivity_ViewItem.this, "Logout", Toast.LENGTH_SHORT).show();
                firebaseAuth.signOut();
                Intent intent = new Intent(MainActivity_ViewItem.this, MainActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setDeleteItem() {
        //Method to delete a referenced item from firebase database
        String uidItem = firebaseAuth.getUid();
        String viewItemId = getIntent().getStringExtra("itemId");
        firebaseDatabase.getReference().child("Items").child(uidItem).child(uidItem+"purchased").child(viewItemId).removeValue();
        firebaseDatabase.getReference().child("Items").child(uidItem).child(uidItem+"pending").child(viewItemId).removeValue();
        itemName.setText("");
        itemCost.setText("");
        descriptionField.setText("");
        Intent intent = new Intent(MainActivity_ViewItem.this, MainActivity_Home.class);
        startActivity(intent);
    }

    private void setShareItem() {
        // Create the text message with a string.
        String viewItemName = getIntent().getStringExtra("itemName");
        String viewItemCost = getIntent().getStringExtra("itemCost");
        String viewItemDescription = getIntent().getStringExtra("itemDescription");

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Hello there... I was wondering if you could get me this item. " +
                "It is for my baby and it would mean a lot if you would. This is the item I am requesting... (" +
                viewItemName + "). It costs (" + viewItemCost + ") (" + viewItemDescription + ")" +
                "I have pined the nearest location for you location");
        sendIntent.setType("text/plain");

        // Try to invoke the intent.
        try {
            startActivity(sendIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(MainActivity_ViewItem.this, "No app is avilable to send a message", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        String viewItemId = getIntent().getStringExtra("itemId");

        String geoTag = locationPin.getText().toString();
        String id = firebaseAuth.getUid();
        String name = itemName.getText().toString();
        String cost = itemCost.getText().toString();
        String description = descriptionField.getText().toString();
        String imageUrl = itemImage.getImageUri();

        int buttonId = view.getId();

        if (buttonId == R.id.view_item_camera_btn){
            Toast.makeText(this, "open camera", Toast.LENGTH_SHORT).show();
            setUseCamera();

        }else if (buttonId == R.id.view_maps_btn){
            if(locationPin.getText().toString().length() != 0){
                Toast.makeText(this, "open maps", Toast.LENGTH_SHORT).show();
                String locationString = locationPin.getText().toString();
                Uri gmmIntentUri = Uri.parse("geo:0,0?q="+locationString);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }else {
                Toast.makeText(MainActivity_ViewItem.this, "there isn't any location stated", Toast.LENGTH_SHORT).show();
            }
        }else if (buttonId == R.id.view_item_update_btn){
            if (itemCost.getText().toString().length() == 0 ||
                    itemName.getText().toString().length() == 0||
                    descriptionField.getText().toString().length() == 0 ) {
                Toast.makeText(this, "One or more of the fields is empty", Toast.LENGTH_SHORT).show();
            }else{
                setUpdateItem(id,viewItemId, name, cost, description, geoTag, imageUrl);
            }
        }else if(buttonId == R.id.view_item_gallery_btn){
            setGalleryImage();
        }

    }

    private void setGalleryImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_CODE);
    }

    private void setUseCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, CAMERA_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK) {
            if (data.getData() != null) {
                ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("Uploading image");
                progressDialog.setMessage("Your image is uploading...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                String viewItemName = getIntent().getStringExtra("itemName");
                String viewItemId = getIntent().getStringExtra("itemId");
                String uid = firebaseAuth.getUid();

                imageUri = data.getData(); //we have the actual path
                imageView.setImageURI(imageUri); //show image

                StorageReference reference = storage.getReference().child("images").child(uid).child(viewItemName+viewItemId).child(viewItemId);

                reference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                progressDialog.dismiss();
                                String ur = uri.toString();
                                itemImage.setImageUri(ur);
                            }
                        });
                    }
                });
            }
        } else if (requestCode == CAMERA_CODE && resultCode == RESULT_OK) {
            onCapturedImageResults(data);
        }
    }

    private void onCapturedImageResults(Intent data){

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading image");
        progressDialog.setMessage("Your image is uploading...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG,50, bytes);
        byte bb[] = bytes.toByteArray();
        String file = Base64.encodeToString(bb, Base64.DEFAULT);
        imageView.setImageBitmap(thumbnail);

        String viewItemName = getIntent().getStringExtra("itemName");
        String viewItemId = getIntent().getStringExtra("itemId");
        String uid = firebaseAuth.getUid();

        StorageReference reference = storage.getReference().child("images").child(uid).child(viewItemName+viewItemId).child(viewItemId);

        reference.putBytes(bb).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        progressDialog.dismiss();
                        String ur = uri.toString();
                        itemImage.setImageUri(ur);
                    }
                });
            }
        });
    }

    private void setUpdateItem(String id, String viewItemId ,String name, String cost, String description, String geoTag, String ImageUrl) {
        //method to update a selected item from the firebase database

        progress.show();
        Intent intent = getIntent();
        String itemImage = getIntent().getStringExtra("itemImage");

        if (itemImage == null){
            final Items item = new Items(id, viewItemId ,name, cost, description, geoTag, ImageUrl);
            firebaseDatabase.getReference().child("Items")
                    .child(id)
                    .child(id+"pending")
                    .child(viewItemId)
                    .setValue(item)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            progress.dismiss();
                            String uidItem = firebaseAuth.getUid();
                            String viewItemId = getIntent().getStringExtra("itemId");
                            firebaseDatabase.getReference().child("Items").child(uidItem).child(uidItem+"purchased").child(viewItemId).removeValue();
                            Intent intent = new Intent(MainActivity_ViewItem.this, MainActivity_Home.class);
                            startActivity(intent);
                            finish();
                        }
                    });
        }else {
            final Items item = new Items(id, viewItemId ,name, cost, description, geoTag, itemImage);
            firebaseDatabase.getReference().child("Items")
                    .child(id)
                    .child(id+"pending")
                    .child(viewItemId)
                    .setValue(item)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            progress.dismiss();
                            String uidItem = firebaseAuth.getUid();
                            String viewItemId = getIntent().getStringExtra("itemId");
                            firebaseDatabase.getReference().child("Items").child(uidItem).child(uidItem+"purchased").child(viewItemId).removeValue();
                            Intent intent = new Intent(MainActivity_ViewItem.this, MainActivity_Home.class);
                            startActivity(intent);
                            finish();
                        }
                    });
        }
    }
}