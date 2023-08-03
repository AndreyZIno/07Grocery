package com.example.shopeaze;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.example.shopeaze.databinding.CartPageBinding;

public class Cart extends Fragment {

    private CartPageBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = CartPageBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        // checkout that switches to order_success
        Button checkoutButton = rootView.findViewById(R.id.checkout);
        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(Cart.this)
                        .navigate(R.id.action_cart_page_to_order_success);
            }
        });

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}