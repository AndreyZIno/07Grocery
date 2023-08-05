package com.example.shopeaze;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ProductDetailsFragment extends Fragment {
    private static final String ARG_PRODUCT = "product_id";
    private Product product;

    public static ProductDetailsFragment newInstance(Product product) {
        ProductDetailsFragment fragment = new ProductDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PRODUCT, product);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_product_details, container, false);

        Bundle arguments = getArguments();
        if (arguments != null) {
            product = (Product) arguments.getSerializable(ARG_PRODUCT);
            if (product != null) {
                initializeViews(rootView);
            } else {
                Toast.makeText(requireContext(), "Product data is null.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(requireContext(), "No arguments passed to the fragment.", Toast.LENGTH_SHORT).show();
        }

        ImageButton imageButtonGoBack = rootView.findViewById(R.id.buttonGoBack);
        imageButtonGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle "go back" button click by navigating back to the previous fragment
                requireActivity().onBackPressed();
            }
        });

        Button buttonRemoveProduct = rootView.findViewById(R.id.buttonRemoveProduct);
        buttonRemoveProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRemoveProductDialog();
            }
        });

        return rootView;
    }

    private void initializeViews(View rootView) {
        ImageView imageViewProduct = rootView.findViewById(R.id.imageViewProduct);
        TextView textViewProductName = rootView.findViewById(R.id.textViewProductName);
        TextView textViewProductBrand = rootView.findViewById(R.id.textViewProductBrand);
        TextView textViewProductPrice = rootView.findViewById(R.id.textViewProductPrice);
        TextView textViewProductDescription = rootView.findViewById(R.id.textViewProductDescription);
        Button buttonChangePrice = rootView.findViewById(R.id.buttonChangePrice);
        Button buttonChangeDescription = rootView.findViewById(R.id.buttonChangeDescription);
        Button buttonRemoveProduct = rootView.findViewById(R.id.buttonRemoveProduct);

        imageViewProduct.setImageResource(R.drawable.sample);
        textViewProductName.setText(product.getName());
        textViewProductBrand.setText(product.getBrand());
        textViewProductPrice.setText("$ " + String.valueOf(product.getPrice()));
        textViewProductDescription.setText(product.getDescription());

        // Add click listeners and implement actions for the buttons if needed.
    }

    private void showRemoveProductDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Confirm Removal")
                .setMessage("Are you sure you want to remove this product?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removeProductFromFirebase();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void removeProductFromFirebase() {
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child("StoreOwner")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("Products");

        Query productQuery = productRef.orderByChild("name").equalTo(product.getName());

        productQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                    productSnapshot.getRef().removeValue();
                }
                NavController navController = NavHostFragment.findNavController(ProductDetailsFragment.this);
                navController.navigate(R.id.action_product_details_to_product_list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showToast("Failed to remove product.");
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}

