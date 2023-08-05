package com.example.shopeaze;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class OwnerOrderFragment extends Fragment {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    // Declare a ListView to display the orders and a Button for refreshing the orders.
    private ListView ordersListView;
    private Button refreshButton;

    // Declare an ArrayAdapter to handle the list of orders.
    private ArrayAdapter<String> ordersAdapter;

    // Declare a List to hold the orders data.
    private List<String> ordersList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_owner_orders_view, container, false);

        ordersListView = view.findViewById(R.id.ordersListView);
        
        Button refreshButton = view.findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(v -> loadOrders());

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        loadOrders();

        return view;
    }

    private void loadOrders() {
    }
}
