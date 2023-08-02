
package com.example.shopeaze;

        import android.os.Bundle;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageButton;
        import androidx.fragment.app.Fragment;
        import androidx.navigation.fragment.NavHostFragment;


        import com.example.shopeaze.databinding.OrderSuccessBinding;

public class OrdersSuccess extends Fragment {

    private OrderSuccessBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = OrderSuccessBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        // home button that switches to order_success
        ImageButton homeButton = rootView.findViewById(R.id.backhomeButton);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(OrdersSuccess.this)
                        .navigate(R.id.action_order_success_to_stores_page);
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