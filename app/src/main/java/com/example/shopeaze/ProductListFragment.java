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

import com.firebase.ui.database.FirebaseRecyclerOptions;
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
    private ProductAdapter adapter;

    private DatabaseReference storeOwnerRef;
    public TextView textViewStoreName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_product_list, container, false);

        textViewStoreName = rootView.findViewById(R.id.textViewStoreName);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            String storeOwnerId = currentUser.getUid();
            storeOwnerRef = FirebaseDatabase.getInstance().getReference().child("Users").child("StoreOwner").child(storeOwnerId);

            fetchStoreName();

            RecyclerView recyclerViewProducts = rootView.findViewById(R.id.recyclerViewProducts);
            recyclerViewProducts.setLayoutManager(new LinearLayoutManager(getActivity()));

            adapter = new ProductAdapter(new FirebaseRecyclerOptions.Builder<Product>()
                    .setQuery(FirebaseDatabase.getInstance().getReference().child("Products"), Product.class)
                    .build(), this);
            recyclerViewProducts.setAdapter(adapter);
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

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    private void fetchStoreName() {
        storeOwnerRef.child("StoreName").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String storeName = dataSnapshot.getValue(String.class);
                if (storeName != null) {
                    textViewStoreName.setText(storeName);
                } else {
                    showToast("Store name not found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
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
        AddProductDialog dialog = new AddProductDialog();
        dialog.show(requireActivity().getSupportFragmentManager(), "AddProductDialog");
    }

}
