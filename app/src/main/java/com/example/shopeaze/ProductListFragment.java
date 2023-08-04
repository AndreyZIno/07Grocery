package com.example.shopeaze;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProductListFragment extends Fragment implements ProductAdapter.OnItemClickListener {
    private List<Product> products;
    private ProductAdapter adapter;

    private DatabaseReference storeOwnerRef;
    public TextView textViewStoreName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_product_list, container, false);

        // Find the TextView and set the store name
        textViewStoreName = rootView.findViewById(R.id.textViewStoreName);

        // Initialize Firebase components
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            String storeOwnerId = currentUser.getUid();
            storeOwnerRef = FirebaseDatabase.getInstance().getReference().child("Users").child("StoreOwner").child(storeOwnerId);

            fetchStoreName();

            RecyclerView recyclerViewProducts = rootView.findViewById(R.id.recyclerViewProducts);
            recyclerViewProducts.setLayoutManager(new LinearLayoutManager(getActivity()));

            // Initialize the adapter and set it to the RecyclerView
            products = new ArrayList<>();
            adapter = new ProductAdapter(products, this);
            recyclerViewProducts.setAdapter(adapter);

            fetchProducts();
        }

        FloatingActionButton fabAddProduct = rootView.findViewById(R.id.fabAddProduct);
        fabAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddProductDialog();
            }
        });

        return rootView;
    }

    private void fetchProducts() {
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Products");
        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Product> products = new ArrayList<>();
                for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                    Product product = productSnapshot.getValue(Product.class);
                    if (product != null) {
                        products.add(product);
                    }
                }
                // Update the adapter with the fetched products
                adapter.setProducts(products);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the database error here
                String errorMessage = "Error fetching products: " + databaseError.getMessage();
                showToast(errorMessage);
            }
        });
    }

    private void fetchStoreName() {
        storeOwnerRef.child("StoreName").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String storeName = dataSnapshot.getValue(String.class);
                if (storeName != null) {
                    textViewStoreName.setText(storeName);
                } else {
                    // The store name is not available or null in the database
                    showToast("Store name not found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the database error here
                String errorMessage = "Error fetching store name: " + databaseError.getMessage();
                showToast(errorMessage);
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(Product product) {
        openProductDetailsFragment(product.getProductID());
    }

    private void openProductDetailsFragment(String productID) {
        ProductDetailsFragment fragment = ProductDetailsFragment.newInstance(productID);
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void showAddProductDialog() {
        // Create and show a dialog to gather product information from the user
        AddProductDialog dialog = new AddProductDialog();
        dialog.show(requireActivity().getSupportFragmentManager(), "AddProductDialog");
    }

}