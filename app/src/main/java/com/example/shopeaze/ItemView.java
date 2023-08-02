package com.example.shopeaze;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.shopeaze.databinding.ItemDisplayBinding;
import com.google.android.material.button.MaterialButton;


public class ItemView extends Fragment {

    private ItemDisplayBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = ItemDisplayBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        // add to cart button that switches to cart_page
        ImageButton addToCartButton = rootView.findViewById(R.id.add_to_cart_button);
        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                NavHostFragment.findNavController(ItemView.this)
                        .navigate(R.id.action_item_display_to_cart_page);
            }
        });
//
        // details button that switches to item_display_description
        MaterialButton detailsButton = rootView.findViewById(R.id.details);
        detailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                NavHostFragment.findNavController(ItemView.this)
                        .navigate(R.id.action_item_display_to_item_display_description);
            }
        });

        // spinner that displays the size options
        Spinner spinner = rootView.findViewById(R.id.spinner_sizes);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(), R.array.sizes_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // update name of product
//        List<Product> productsList = ProductList.products;
//        String productName = productsList.get(0).getProductName();
//        binding.productName.setText(productName);

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}