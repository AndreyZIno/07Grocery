package com.example.shopeaze;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Stores extends Fragment {

    FirebaseAuth auth;
    Button logoutButton;
    ImageButton cartButton;
    ImageButton ordersButton;
    ImageButton storeButton;

    TextView name_display;
    FirebaseUser user;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.stores_page, container, false);
        logoutButton = view.findViewById(R.id.logout);
        cartButton = view.findViewById(R.id.cartButton);
        ordersButton = view.findViewById(R.id.ordersButton);
        name_display = view.findViewById(R.id.user_details);
        storeButton = view.findViewById(R.id.Store1);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if (user==null){
            NavHostFragment.findNavController(Stores.this)
                    .navigate(R.id.action_stores_page_to_welcomeScreen);
        }
        else{
            name_display.setText(user.getEmail());
        }

        logoutButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                FirebaseAuth.getInstance().signOut();
                //Intent intent = new Intent(getActivity(), WelcomeScreen.class);
                //startActivity(intent);
                NavHostFragment.findNavController(Stores.this)
                        .navigate(R.id.action_stores_page_to_welcomeScreen);
            }
        });

        cartButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                NavHostFragment.findNavController(Stores.this)
                        .navigate(R.id.action_stores_page_to_cart_page);
            }
        });

        ordersButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                NavHostFragment.findNavController(Stores.this)
                        .navigate(R.id.action_stores_page_to_orders_shopper);
            }
        });

        storeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                NavHostFragment.findNavController(Stores.this)
                        .navigate(R.id.action_stores_page_to_store_products_page);
            }
        });

        return view;
    }
}
