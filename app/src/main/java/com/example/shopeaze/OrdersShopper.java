
package com.example.shopeaze;

        import android.os.Bundle;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageButton;

        import androidx.fragment.app.Fragment;
        import androidx.navigation.fragment.NavHostFragment;

        import com.example.shopeaze.databinding.OrdersShopperBinding;

public class OrdersShopper extends Fragment {

    private OrdersShopperBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = OrdersShopperBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        // home button that switches to stores_page.xml
        ImageButton homeButton = rootView.findViewById(R.id.homeButton);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(OrdersShopper.this)
                        .navigate(R.id.action_orders_shopper_to_stores_page);
            }
        });

        // cart button that switches to cart_page.xml
        ImageButton cartButton = rootView.findViewById(R.id.cartButton);
        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(OrdersShopper.this)
                        .navigate(R.id.action_orders_shopper_to_cart_page);
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