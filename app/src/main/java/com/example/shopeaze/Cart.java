package com.example.shopeaze;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.example.shopeaze.databinding.CartPageBinding;

public class Cart extends Activity {

    private CartPageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_page);

        // home button tht switches to stores_page.xml
        ImageButton HomeButton = findViewById(R.id.homeButton);
        HomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // placeholder until we get the class for the display of stores we can switch to
                // Intent intent = new Intent(Cart.this, StoresPage.class);
                // startActivity(intent);
            }
        });

        // orders button that switches to orders_shopper.xml
        ImageButton OrdersButton = findViewById(R.id.ordersButton);
        HomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // placeholder until we get the class for the shopper orders we can switch to
                // Intent intent = new Intent(Cart.this, OrdersShopper.class);
                // startActivity(intent);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

}

