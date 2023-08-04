package com.example.shopeaze;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;


public class ProductAdapter extends FirebaseRecyclerAdapter<Product, ProductAdapter.ProductViewHolder> {
    private OnItemClickListener itemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Product product);
    }

    public ProductAdapter(@NonNull FirebaseRecyclerOptions<Product> options, OnItemClickListener itemClickListener) {
        super(options);
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Product model) {
        holder.bind(model, itemClickListener);
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView textViewProductName, textViewProductBrand, textViewProductPrice;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewProductName = itemView.findViewById(R.id.textViewProductName);
            textViewProductBrand = itemView.findViewById(R.id.textViewProductBrand);
            textViewProductPrice = itemView.findViewById(R.id.textViewProductPrice);
        }

        public void bind(final Product product, final OnItemClickListener listener) {
            textViewProductName.setText(product.getName());
            textViewProductBrand.setText(product.getBrand());
            textViewProductPrice.setText(String.valueOf(product.getPrice()));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(product);
                }
            });
        }
    }
}

