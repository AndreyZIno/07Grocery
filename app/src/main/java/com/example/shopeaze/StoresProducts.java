package com.example.shopeaze;

        import android.os.Bundle;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageButton;


        import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;
        import androidx.fragment.app.Fragment;
        import androidx.navigation.fragment.NavHostFragment;

        import com.example.shopeaze.databinding.StoreProductsPageBinding;


public class StoresProducts extends Fragment {
    private StoreProductsPageBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = StoreProductsPageBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        // home button that switches to stores_page.xml
        ImageButton homeButton = rootView.findViewById(R.id.homeButton);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(StoresProducts.this)
                        .navigate(R.id.action_store_products_page_to_stores_page);
            }
        });

        // cart button that switches to cart_page.xml
        ImageButton cartButton = rootView.findViewById(R.id.cartButton);
        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(StoresProducts.this)
                        .navigate(R.id.action_store_products_page_to_cart_page);
            }
        });

        // orders button that switches to orders_shopper.xml
        ImageButton ordersButton = rootView.findViewById(R.id.ordersButton);
        ordersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(StoresProducts.this)
                        .navigate(R.id.action_store_products_page_to_orders_shopper);
            }
        });

        // item button that switches to item_display.xml
        ImageButton item1Button = rootView.findViewById(R.id.item1);
        item1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(StoresProducts.this)
                        .navigate(R.id.action_store_products_page_to_item_display);
            }
        });

        return rootView;
    }
}

