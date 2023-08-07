package com.example.shopeaze;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MyCartAdapter extends RecyclerView.Adapter<MyCartAdapter.MyViewHolder>{

    Context context;
    ArrayList<CartItem> cartItems;

    public MyCartAdapter(Context context, ArrayList<CartItem> cartItems) {
        this.context = context;
        this.cartItems = cartItems;
    }

    @NonNull
    @Override
    public MyCartAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.cart_item, parent, false));

        View v = LayoutInflater.from(context).inflate(R.layout.cart_item, parent, false);
        return new MyCartAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyCartAdapter.MyViewHolder holder, int position) {
        CartItem cartItem = cartItems.get(position);
        Log.d("MyCartAdapter", "Name: " + cartItem.getcartProductName());
        Log.d("MyCartAdapter", "Price: " + cartItem.getcartProductPrice());
        Log.d("MyCartAdapter", "Brand: " + cartItem.getcartProductBrand());
        Log.d("MyCartAdapter", "Quantity: " + cartItem.getCartQuantity());
        Log.d("MyCartAdapter", "ID: " + cartItem.getcartProductID());
        Log.d("MyCartAdapter", "Status: " + cartItem.getStatus());

        holder.cartProductName.setText(cartItem.getcartProductName());
        holder.cartProductPrice.setText("$ " + String.valueOf(cartItem.getcartProductPrice()));
        holder.cartProductBrand.setText(cartItem.getcartProductBrand());
        holder.cartProductQuantity.setText(String.valueOf(cartItem.getCartQuantity()));

        holder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    CartItem cartItem = cartItems.get(position);
                    removeCartItem(cartItem);
                    cartItems.remove(position);
                    notifyItemRemoved(position);
                }
            }
        });

    }

    private void removeCartItem(CartItem cartItem) {
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");
        DatabaseReference shopperRef = usersRef.child("Shoppers").child(userID);
        DatabaseReference ordersRef = shopperRef.child("Orders");
        ordersRef.child(cartItem.getcartProductID()).removeValue();

        DatabaseReference cartRef = shopperRef.child("Cart");
        cartRef.child(cartItem.getcartProductID()).removeValue();
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView cartProductName, cartProductPrice, cartProductBrand, cartProductQuantity;
        Button removeButton;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            cartProductName = itemView.findViewById(R.id.cartProductName);
            cartProductPrice = itemView.findViewById(R.id.cartProductPrice);
            cartProductBrand = itemView.findViewById(R.id.cartProductBrand);
            cartProductQuantity = itemView.findViewById(R.id.productQuantity);
            removeButton = itemView.findViewById(R.id.removeButton);
        }
    }





}