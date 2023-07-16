package com.example.babybuy.ui.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.babybuy.adapter.ItemAdapter;
import com.example.babybuy.databinding.FragmentPurchasedListBinding;
import com.example.babybuy.model.Items;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class purchased_list extends Fragment {


    FragmentPurchasedListBinding binding;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseDatabase database;
    ArrayList<Items> list = new ArrayList<>();

    public purchased_list() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentPurchasedListBinding.inflate(inflater, container, false);
        database = FirebaseDatabase.getInstance();
        String uid = firebaseAuth.getUid();

        ItemAdapter itemAdapter = new ItemAdapter(list, getContext());
        binding.itemListRecyclerPurchased.setAdapter(itemAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.itemListRecyclerPurchased.setLayoutManager(layoutManager);

        database.getReference().child("Items").child(uid).child(uid+"purchased").addValueEventListener(new ValueEventListener() {
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