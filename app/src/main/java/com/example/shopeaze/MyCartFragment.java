package com.example.shopeaze;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MyCartFragment extends Fragment {

    private DatabaseReference productsRef;
    FirebaseAuth auth;

    RecyclerView recyclerView;
    MyCartAdapter cartAdapter;
    List<MyCartModel> cartModelList;

    public MyCartFragment(){
        //
    }

    @Override
    public View onCreateView(LayoutInflater inflator, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflator.inflate(R.layout.activity_cart, container, false);


        productsRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Shopper").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Cart");
        auth = FirebaseAuth.getInstance();
        recyclerView = root.findViewById(R.id.recyclerCartView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        cartModelList = new ArrayList<>();
        cartAdapter = new MyCartAdapter(getActivity(), cartModelList);
        recyclerView.setAdapter(cartAdapter);

        // access the firebase database at productsRef and update the cartModelList
        productsRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                MyCartModel cartModel = dataSnapshot.getValue(MyCartModel.class);
                cartModelList.add(cartModel);
                cartAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                // Handle changes to existing cart items if needed
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                // Handle removal of cart items if needed
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                // Handle movement of cart items if needed
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error if needed
            }
        });


        ImageButton storesButton = root.findViewById(R.id.button_stores);
        storesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = NavHostFragment.findNavController(MyCartFragment.this);
                navController.navigate(R.id.action_Cart_to_StoreList);
            }
        });

        ImageButton cartButton = root.findViewById(R.id.button_cart);
        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = NavHostFragment.findNavController(MyCartFragment.this);
                NavDestination currentDestination = navController.getCurrentDestination();
                if (currentDestination != null && currentDestination.getId() == R.id.Cart) {
                    // User is already on Cart fragment, do nothing
                    return;
                }
            }
        });

        Button checkoutButton = root.findViewById(R.id.CheckoutButton);
        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = NavHostFragment.findNavController(MyCartFragment.this);
                navController.navigate(R.id.action_Cart_to_order_confirm);

                // ###########################
                // add list to orders in firebase
                // ###########################
            }
        });

        return root;

    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final NavController navController = NavHostFragment.findNavController(this);
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                ImageButton storesButton = view.findViewById(R.id.button_stores);
                ImageButton cartButton = view.findViewById(R.id.button_cart);
                TextView storesText = view.findViewById(R.id.textViewStoresText);
                TextView cartText = view.findViewById(R.id.textViewCartText);
                ImageView storeIcon = view.findViewById(R.id.storeIcon);
                ImageView cartIcon = view.findViewById(R.id.cartIcon);
                if (destination.getId() == R.id.StoreList) {
                    storesButton.setImageResource(R.drawable.focused_nav_button);
                    cartButton.setImageResource(R.drawable.nav_gradient);
                    storesText.setTextColor(ContextCompat.getColor(getContext(), R.color.navy_blue));
                    cartText.setTextColor(ContextCompat.getColor(getContext(), R.color.light_gray));
                    storeIcon.setImageResource(R.drawable.black_store);
                    cartIcon.setImageResource(R.drawable.white_cart);
                } else if (destination.getId() == R.id.Cart) {
                    storesButton.setImageResource(R.drawable.nav_gradient);
                    cartButton.setImageResource(R.drawable.focused_nav_button);
                    storesText.setTextColor(ContextCompat.getColor(getContext(), R.color.light_gray));
                    cartText.setTextColor(ContextCompat.getColor(getContext(), R.color.navy_blue));
                    storeIcon.setImageResource(R.drawable.white_store);
                    cartIcon.setImageResource(R.drawable.black_cart);
                }
            }
        });
    }




}
