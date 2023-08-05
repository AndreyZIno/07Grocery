package com.example.shopeaze;

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
import androidx.fragment.app.Fragment;

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
}

