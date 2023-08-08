package com.example.shopeaze;

import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.List;
import com.example.shopeaze.CartItem;
import com.google.firebase.database.ValueEventListener;


public class MyCartFragment extends Fragment {

    private DatabaseReference productsRef;
    private RecyclerView recyclerView;
    private MyCartAdapter cartAdapter;
    private ArrayList<CartItem> products;
    private ArrayList<String> storesList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflator, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflator.inflate(R.layout.activity_cart, container, false);

        recyclerView = root.findViewById(R.id.recyclerCartView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        products = new ArrayList<>();
        cartAdapter = new MyCartAdapter(getActivity(), products);

        recyclerView.setAdapter(cartAdapter);
        productsRef = FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child("Shoppers")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("Cart");

        productsRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                CartItem cartItem = dataSnapshot.getValue(CartItem.class);
                products.add(cartItem);
                cartAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                CartItem updatedCartItem = dataSnapshot.getValue(CartItem.class);
                int position = findCartItemPosition(updatedCartItem);
                if (position != -1) {
                    products.set(position, updatedCartItem);
                    cartAdapter.notifyItemChanged(position);
                }
            }

            private int findCartItemPosition(CartItem updatedCartItem) {
                for (int i = 0; i < products.size(); i++) {
                    CartItem cartItem = products.get(i);
                    if (cartItem.getcartProductID().equals(updatedCartItem.getcartProductID())) {
                        return i;
                    }
                }
                return -1; // Item not found
            }


            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                CartItem removedProduct = dataSnapshot.getValue(CartItem.class);
                if (removedProduct != null) {
                    products.remove(removedProduct);
                    cartAdapter.notifyDataSetChanged();
                }
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

                addToOrders(products);
            }
        });

        return root;

    }


    // add a list of Product objects to the Orders database in Firebase, under Shoppers

    private void addToOrders(List<CartItem> cartItems) {
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");
        DatabaseReference shopperRef = usersRef.child("Shoppers").child(userID);
        DatabaseReference ordersRef = shopperRef.child("Orders");

        // add each cart item in the list to the Orders database
        for (CartItem cartItem : cartItems) {
            cartItem.setStatus("Received");
            ordersRef.push().setValue(cartItem);
        }

        // owners side
        DatabaseReference storeOwnerRef = usersRef.child("StoreOwner");
        DatabaseReference ordersOwner;

        for (CartItem cartItem : cartItems) {
            // if cartItem.storeName is not in storesList, add it
            if (!storesList.contains(cartItem.getStoreName())) {
                storesList.add(cartItem.getStoreName());
            }
        }

        for (String storeName : storesList) {
            storeOwnerRef.orderByChild("StoreName").equalTo(storeName).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            for (CartItem cartItem : cartItems) {
                                if (cartItem.getStoreName().equals(storeName)) {
                                    // push cartItem to the Orders database under the current store owner
                                    snapshot.getRef().child("Orders").push().setValue(cartItem);

                                }
                            }


                            CartItem existingCartItem = snapshot.getValue(CartItem.class);
                            int newQuantity = existingCartItem.getCartQuantity() + 1;
                            snapshot.getRef().child("cartQuantity").setValue(newQuantity);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle any errors that might occur during the query
                    Log.d("MyCartAdapter", "onCancelled", databaseError.toException());
                }
            });



                ordersOwner = storeOwnerRef.child(storeName).child("Orders");
            for (CartItem cartItem : cartItems) {
                if (cartItem.getStoreName().equals(storeName)) {
                    ordersOwner.push().setValue(cartItem);
                }
            }
        }
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