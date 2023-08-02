package com.example.shopeaze;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import com.example.shopeaze.databinding.ItemDisplayBinding;

public class ItemView extends Activity{

    private ItemDisplayBinding binding;

//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//
//        binding = ItemDisplayBinding.inflate(inflater, container, false);
//        return binding.getRoot();
//
//    }

//    Spinner spnUnit;
//    ArrayAdapter<String> ad;
//    ArrayList<String> alAngle = new ArrayList<String>();



//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View rootView = inflater.inflate(R.layout.item_display, container, false);
//        Spinner spinnerSizes = rootView.findViewById(R.id.spinner_sizes);
//
//        // Populate the spinner with data
//        String[] sizes = {"XS", "S", "M", "L", "XL"};
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, sizes);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerSizes.setAdapter(adapter);
//
//        // Add any other logic related to the spinner here
//
//        return rootView;
//    }


//    private Fragment spinner_sizes;
//    Spinner spinner = (Spinner) spinner_sizes.getView();
//    // Create an ArrayAdapter using the string array and a default spinner layout
//    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
//            R.array.sizes_array, android.R.layout.simple_spinner_item);
//    // Specify the layout to use when the list of choices appears
//    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//    // Apply the adapter to the spinner
//    spinner.setAdapter(adapter);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_display);

        // add to cart button tht switches to cart_page
        ImageButton addToCartButton = findViewById(R.id.add_to_cart_button);
        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ItemView.this, Cart.class);
                startActivity(intent);
            }
        });

        // spinner that displays the size options
        Spinner  spinner = (Spinner) findViewById(R.id.spinner_sizes);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.sizes_array,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //update name of product


    }


//    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
//        // create super.OnCreate()
//        super.onCreate(savedInstanceState);
//
//        Button addToCartButton = view.findViewById(R.id.add_to_cart_button);
//
//        // when the addToCartButton is clicked, switch to the cart_page
//    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

}