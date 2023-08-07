package com.example.shopeaze;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;

public class MyCartAdapter extends RecyclerView.Adapter<MyCartAdapter.MyViewHolder>{

    Context context;
    ArrayList<Product> products;

    public MyCartAdapter(Context context, ArrayList<Product> products) {
        this.context = context;
        this.products = products;
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
        Product product = products.get(position);
        holder.cartProductName.setText(products.get(position).getName());
        holder.cartProductPrice.setText("$ " + String.valueOf(product.getPrice()));
        holder.cartProductBrand.setText(products.get(position).getBrand());
        holder.cartProductQuantity.setText(products.get(position).getQuantity());
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView cartProductName, cartProductPrice, cartProductBrand, cartProductQuantity;
        public MyViewHolder(@androidx.annotation.NonNull android.view.View itemView) {
            super(itemView);

            cartProductName = itemView.findViewById(R.id.ProductName);
            cartProductPrice = itemView.findViewById(R.id.ProductPrice);
            cartProductBrand = itemView.findViewById(R.id.ProductBrand);
            cartProductQuantity = itemView.findViewById(R.id.productQuantity);
        }
    }





}
