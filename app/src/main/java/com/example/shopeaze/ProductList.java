package com.example.shopeaze;

import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

/*

NOTE: this method provides methods to add, remove, and retrieve products from the db, while
maintaining a local copy of the products for efficient access and display w/i the app.

 */

public class ProductList {
    private DatabaseReference databaseReference;
    public List<Product> products;
    private TextView textViewStoreName;

    public ProductList(FirebaseUser currentUser, TextView textViewStoreName) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("products");
        this.textViewStoreName = textViewStoreName;
        products = new ArrayList<>();
        loadProductsFromFirebase();


        if (currentUser != null) {
            String storeOwnerId = currentUser.getUid();
            DatabaseReference storeOwnerRef = FirebaseDatabase.getInstance()
                    .getReference().child("Users")
                    .child("StoreOwner")
                    .child(storeOwnerId);

            // Fetch the store name from Firebase
            fetchStoreName(storeOwnerRef);
        }

    }

    private void fetchStoreName(DatabaseReference storeOwnerRef) {
        storeOwnerRef.child("StoreName").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String storeName = dataSnapshot.getValue(String.class);
                if (storeName != null) {
                    textViewStoreName.setText(storeName);
                } else {
                    System.err.println("Store name not found in the database.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.err.println("Error fetching store name: " + databaseError.getMessage());
            }
        });
    }

    public void addProduct(Product product) {
        String productID = databaseReference.push().getKey();
        product.setProductID(productID);

        databaseReference.child(productID).setValue(product);
    }

    public void removeProduct(Product product) {
        databaseReference.child(product.getProductID()).removeValue();
    }

    public List<Product> getAllProducts() { return products; }

    private void loadProductsFromFirebase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                products.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Product product = snapshot.getValue(Product.class);
                    products.add(product);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.err.println("Error loading products from Firebase: " + error.getMessage());
            }
        });
    }

    // Get the product by its ID (search method)
    public Product getProductByID(String productID) throws AppExceptions.ProductNotFoundException {
        for (Product product : products) {
            if (product.getProductID().equals(productID)) {
                return product;
            }
        }

        // if the product is not found
        throw new AppExceptions.ProductNotFoundException("Product with ID " + productID + " not found.");
    }
}
