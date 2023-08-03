
package com.example.shopeaze;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.shopeaze.databinding.InventoryOwnerBinding;

class InventoryOwner extends Fragment {

    private InventoryOwnerBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = InventoryOwnerBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        // orders button that switches to orders_owner.xml
        ImageButton ordersButton = rootView.findViewById(R.id.ordersButton);
        ordersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(InventoryOwner.this)
                        .navigate(R.id.action_inventory_owner_to_orders_owner);
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