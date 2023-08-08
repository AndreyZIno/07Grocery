package com.example.shopeaze;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OwnerOrdersAdapter extends RecyclerView.Adapter<OwnerOrdersAdapter.ViewHolder> {
    private List<Order> mOrders;

    public OwnerOrdersAdapter(List<Order> orders) {
        mOrders = orders;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.owner_order_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = mOrders.get(position);

        // Set up the views in your owner_order_item layout here using the data from the current order
    }

    @Override
    public int getItemCount() {
        return mOrders.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Declare views from your owner_order_item layout here

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize views from your owner_order_item layout here
        }
    }
}
