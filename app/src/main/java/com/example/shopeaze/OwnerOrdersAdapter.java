package com.example.shopeaze;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.List;

public class OwnerOrdersAdapter extends RecyclerView.Adapter<OwnerOrdersAdapter.ViewHolder> {
    private List<Order> mOrders;
    private DatabaseReference mDatabaseRef;

    public OwnerOrdersAdapter(List<Order> orders) {
        mOrders = orders;
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
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
        holder.orderIdTextView.setText(order.getOrderID());

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(holder.itemView.getContext(),
                R.array.order_statuses, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.orderStatusSpinner.setAdapter(adapter);

        String[] orderStatuses = holder.itemView.getResources().getStringArray(R.array.order_statuses);
        int currentStatusIndex = Arrays.asList(orderStatuses).indexOf(order.getStatus());
        holder.orderStatusSpinner.setSelection(currentStatusIndex);

        holder.orderStatusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedStatus = (String) parent.getItemAtPosition(position);
                if (!selectedStatus.equals(order.getStatus())) {
                    // Update the order status in the Firebase database
                    mDatabaseRef.child("Users").child("StoreOwner").child(order.getStoreID())
                            .child("Orders").child(order.getOrderID()).child("Status")
                            .setValue(selectedStatus);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mOrders.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Declare views from your owner_order_item layout here
        TextView orderIdTextView;
        Spinner orderStatusSpinner;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize views from your owner_order_item layout here
            orderIdTextView = itemView.findViewById(R.id.orderIdTextView);
            orderStatusSpinner = itemView.findViewById(R.id.orderStatusSpinner);
        }
    }


}
