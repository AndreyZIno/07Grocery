package com.example.shopeaze;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.List;

public class ProductStatusFragment extends Fragment {
    // Declare Firebase Auth and Firestore instances.
    private FirebaseAuth mAuth;
    private FirebaseDatabase db;

    // Declare a ListView to display the orders and a Button for refreshing the orders.
    private ListView productStatusListView;
    private Button refreshButton;

    // Declare an ArrayAdapter to handle the list of orders.
    private ArrayAdapter<String> productStatusAdapter;

    // Declare a List to hold the orders data.
    private List<String> productStatusList;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_order_view, container, false);

        productStatusList = view.findViewById(R.id.productOrderedListView);

        // Initialize the refresh Button and set a click listener.
        Button refreshButton = view.findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(v -> loadProducts());

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance("https://grocery-d4fbb-default-rtdb.firebaseio.com//");


        loadProducts();

        return view;
    }

    private void loadProducts() {
        //Search the orders collection for orders that have products that match the shop owners store name.
        //If the order has products that match the store name, add the order to the list of orders.
        //If the order does not have products that match the store name, do not add the order to the list of orders.
        //Display the list of orders in the ListView.

        //Get current user's ID
        FirebaseUser user = mAuth.getCurrentUser();
        //Get that user's store name
        String storeName = db.getReference("Users").child("ShopOwners").child(user.getUid()).child("StoreName").toString();
        Query query = db.getReference("orders").child("Products").orderByChild("StoreName").equalTo(storeName);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot productSnapshot : snapshot.getChildren()){
                    String productName = productSnapshot.child("ProductName").getValue(String.class);
                    String productPrice = productSnapshot.child("ProductPrice").getValue(String.class);
                    String productQuantity = productSnapshot.child("ProductQuantity").getValue(String.class);
                    String productStatus = productSnapshot.child("ProductStatus").getValue(String.class);
                    String productOrder = productName + " " + productPrice + " " + productStatus + " " + productQuantity;
                    productStatusList.add(productOrder);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast toast = Toast.makeText(getContext(), "Error loading products", Toast.LENGTH_SHORT);
            }
        });



    }


}
