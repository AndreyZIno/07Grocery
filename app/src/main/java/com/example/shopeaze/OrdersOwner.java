
package com.example.shopeaze;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.shopeaze.databinding.OrdersOwnerBinding;

public class OrdersOwner extends Fragment {

    private OrdersOwnerBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = OrdersOwnerBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        // inventory button that switches to inventory_owner.xml
        ImageButton inventoryButton = rootView.findViewById(R.id.inventoryButton);
        inventoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(OrdersOwner.this)
                        .navigate(R.id.action_orders_shopper_to_stores_page);
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