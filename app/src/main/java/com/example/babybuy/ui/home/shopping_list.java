package com.example.babybuy.ui.home;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.babybuy.adapter.ItemAdapter;
import com.example.babybuy.databinding.FragmentShoppingListBinding;
import com.example.babybuy.model.Items;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class shopping_list extends Fragment {

    FragmentShoppingListBinding binding;
    ArrayList<Items> list = new ArrayList<>();
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    public shopping_list() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentShoppingListBinding.inflate(inflater, container, false);
        firebaseDatabase = FirebaseDatabase.getInstance();
        String uid = firebaseAuth.getUid();

        ItemAdapter itemAdapter = new ItemAdapter(list,getContext());
        binding.itemListRecycler.setAdapter(itemAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.itemListRecycler.setLayoutManager(layoutManager);

        assert uid != null;
        firebaseDatabase.getReference().child("Items").child(uid).child(uid+"pending").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for ( DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Items items = dataSnapshot.getValue(Items.class);
                    assert items != null;
                    items.setItemId(dataSnapshot.getKey());
                    if (!items.getItemId().equals(FirebaseAuth.getInstance().getUid())){
                        list.add(items);
                    }
                }
                itemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return binding.getRoot();
    }
}