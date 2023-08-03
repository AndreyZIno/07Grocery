package com.example.shopeaze;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.StoreViewHolder> {

    private List<Store> stores;
    private OnItemClickListener itemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Store store);
    }

    public StoreAdapter(List<Store> stores, OnItemClickListener itemClickListener) {
        this.stores = stores;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public StoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_store, parent, false);
        return new StoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoreViewHolder holder, int position) {
        Store store = stores.get(position);
        holder.textViewStoreName.setText(store.getStoreName());
        String logoUrl = store.getLogoUrl();
        if (logoUrl != null && !logoUrl.isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(logoUrl)
                    .into(holder.imageViewStoreLogo);
        }
    }

    @Override
    public int getItemCount() {
        return stores.size();
    }


    public static class StoreViewHolder extends RecyclerView.ViewHolder {
        TextView textViewStoreName;
        ImageView imageViewStoreLogo;

        public StoreViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewStoreName = itemView.findViewById(R.id.textViewStoreName);
            imageViewStoreLogo = itemView.findViewById(R.id.imageViewStoreLogo);

            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // handle click on store item
                    // start new activity or fragment to display products offered by clicked store
                }
            }); */
        }

    }

    // FOR TEMPORARY TEST PURPOSES


}
