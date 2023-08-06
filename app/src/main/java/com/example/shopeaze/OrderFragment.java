package com.example.shopeaze;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class OrderFragment extends Fragment {
    // Declare Firebase Auth and Firestore instances.
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    // Declare a ListView to display the orders and a Button for refreshing the orders.
    private ListView ordersListView;
    private Button refreshButton;

    // Declare a List to hold the orders data.
    private List<String> ordersList;

    private RecyclerView ordersRecyclerView;
    private OrdersAdapter ordersAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, container, false);


        ordersRecyclerView = view.findViewById(R.id.recyclerViewOrders);

        Button refreshButton = view.findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(v -> loadOrders());

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        loadOrders();

        ImageButton ordersButton = view.findViewById(R.id.button_orders);
        ordersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = NavHostFragment.findNavController(OrderFragment.this);
                NavDestination currentDestination = navController.getCurrentDestination();
                if (currentDestination != null && currentDestination.getId() == R.id.OrderFragment) {
                    // User is already on OrderFragment, do nothing
                    return;
                }
            }
        });

        ImageButton storesButton = view.findViewById(R.id.button_stores);
        storesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = NavHostFragment.findNavController(OrderFragment.this);
                navController.navigate(R.id.action_OrderFragment_to_StoreList);
            }
        });

        ImageButton cartButton = view.findViewById(R.id.button_cart);
        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = NavHostFragment.findNavController(OrderFragment.this);
                navController.navigate(R.id.action_OrderFragment_to_Cart);
            }
        });

        return view;
    }


    // Function to load orders from Firestore.
    private void loadOrders() {
        // Get the current user's ID.
        String userId = mAuth.getCurrentUser().getUid();

        // Query Firestore for orders where the userId matches the current user's ID.
        db.collection("orders")
                .whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(task -> {
                    // If the query is successful...
                    if (task.isSuccessful()) {
                        // Initialize the ordersList.
                        ordersList = new ArrayList<>();

                        // Loop through each document in the results.
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Convert the document into an Order object.
                            Order order = document.toObject(Order.class);

                            // Add the order's ID and status to the ordersList.
                            ordersList.add("Order ID: " + order.getOrderId() + "\nStatus: " + order.getStatus());
                        }

                        // Create an ArrayAdapter with the ordersList and set it as the adapter for ordersListView.
                        ordersAdapter = new OrdersAdapter(ordersList);
                        ordersRecyclerView.setAdapter(ordersAdapter);
                    } else {
                        // If the query is not successful, display a toast with an error message.
                        Toast.makeText(getContext(), "Error getting orders", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final NavController navController = NavHostFragment.findNavController(this);
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                ImageButton storesButton = view.findViewById(R.id.button_stores);
                ImageButton cartButton = view.findViewById(R.id.button_cart);
                ImageButton ordersButton = view.findViewById(R.id.button_orders);
                TextView storesText = view.findViewById(R.id.textViewStoresText);
                TextView cartText = view.findViewById(R.id.textViewCartText);
                TextView ordersText = view.findViewById(R.id.textViewOrdersText);
                ImageView storeIcon = view.findViewById(R.id.storeIcon);
                ImageView cartIcon = view.findViewById(R.id.cartIcon);
                ImageView ordersIcon = view.findViewById(R.id.ordersIcon);
                if (destination.getId() == R.id.StoreList) {
                    storesButton.setImageResource(R.drawable.focused_nav_button);
                    cartButton.setImageResource(R.drawable.nav_gradient);
                    ordersButton.setImageResource(R.drawable.nav_gradient);
                    storesText.setTextColor(ContextCompat.getColor(getContext(), R.color.navy_blue));
                    cartText.setTextColor(ContextCompat.getColor(getContext(), R.color.light_gray));
                    ordersText.setTextColor(ContextCompat.getColor(getContext(), R.color.light_gray));
                    storeIcon.setImageResource(R.drawable.black_store);
                    cartIcon.setImageResource(R.drawable.white_cart);
                    ordersIcon.setImageResource(R.drawable.white_orders);
                } else if (destination.getId() == R.id.Cart) {
                    storesButton.setImageResource(R.drawable.nav_gradient);
                    cartButton.setImageResource(R.drawable.focused_nav_button);
                    ordersButton.setImageResource(R.drawable.nav_gradient);
                    storesText.setTextColor(ContextCompat.getColor(getContext(), R.color.light_gray));
                    cartText.setTextColor(ContextCompat.getColor(getContext(), R.color.navy_blue));
                    ordersText.setTextColor(ContextCompat.getColor(getContext(), R.color.light_gray));
                    storeIcon.setImageResource(R.drawable.white_store);
                    cartIcon.setImageResource(R.drawable.black_cart);
                    ordersIcon.setImageResource(R.drawable.white_orders);
                } else if (destination.getId() == R.id.OrderFragment) {
                    storesButton.setImageResource(R.drawable.nav_gradient);
                    cartButton.setImageResource(R.drawable.nav_gradient);
                    ordersButton.setImageResource(R.drawable.focused_nav_button);
                    storesText.setTextColor(ContextCompat.getColor(getContext(), R.color.light_gray));
                    cartText.setTextColor(ContextCompat.getColor(getContext(), R.color.light_gray));
                    ordersText.setTextColor(ContextCompat.getColor(getContext(), R.color.navy_blue));
                    storeIcon.setImageResource(R.drawable.white_store);
                    cartIcon.setImageResource(R.drawable.white_cart);
                    ordersIcon.setImageResource(R.drawable.black_orders);
                }
            }
        });
    }
}
