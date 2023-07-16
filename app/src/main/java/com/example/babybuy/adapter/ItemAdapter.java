package com.example.babybuy.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.babybuy.MainActivity_Home;
import com.example.babybuy.MainActivity_ViewItem;
import com.example.babybuy.R;
import com.example.babybuy.model.Items;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder>{

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    ArrayList<Items> list;
    Context context;

    public ItemAdapter(ArrayList<Items> list, Context context) {
        this.list = list;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.show_item, parent, false);
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Items items = list.get(position);
        Picasso.get().load(items.getItemImage()).placeholder(R.drawable.shopping_bag_2).into(holder.itemImage);
        holder.itemName.setText(items.getItemName());
        holder.itemCost.setText(items.getItemCost());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "view Item", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, MainActivity_ViewItem.class);
                intent.putExtra("itemId", items.getItemId());
                intent.putExtra("itemName", items.getItemName());
                intent.putExtra("itemCost", items.getItemCost());
                intent.putExtra("itemDescription", items.getItemDescription());
                intent.putExtra("ItemLocation", items.getMapsLocation());
                intent.putExtra("itemImage", items.getItemImage());
                context.startActivity(intent);
            }
        });


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                String id = firebaseAuth.getUid();
                database.getReference().child("Items")
                        .child(id)
                        .child(id+"purchased")
                        .child(items.getItemId())
                        .setValue(items)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                                String viewItemId = items.getItemId();
                                database.getReference().child("Items").child(id).child(id+"pending").child(viewItemId).removeValue();
                            }
                        });
                return true;
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }
        });

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView itemImage;
        TextView itemName, itemCost;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemImage = itemView.findViewById(R.id.list_item_imageHolder_l);
            itemName = itemView.findViewById(R.id.list_item_name_i);
            itemCost = itemView.findViewById(R.id.list_item_cost_i);
        }
    }
}
