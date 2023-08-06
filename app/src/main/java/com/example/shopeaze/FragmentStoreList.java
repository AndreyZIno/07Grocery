package com.example.shopeaze;

import android.graphics.Color;
import android.media.Image;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class FragmentStoreList extends Fragment implements StoreAdapter.OnItemClickListener {

    private List<Store> stores;
    private StoreAdapter adapter;
    FirebaseAuth auth;
    Button logoutButton;

    TextView name_display;
    FirebaseUser user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_store_list, container, false);

        final RecyclerView recyclerViewStores = rootView.findViewById(R.id.recyclerViewStores);
        recyclerViewStores.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        final StoreList storeList = new StoreList();
        storeList.setOnStoresLoadedListener(new StoreList.OnStoresLoadedListener() {
            @Override
            public void onStoresLoaded(List<Store> stores) {
                FragmentStoreList.this.stores = stores;
                adapter = new StoreAdapter(stores, FragmentStoreList.this);
                recyclerViewStores.setAdapter(adapter);
                Log.d("FragmentStoreList", "Loaded " + stores.size() + " stores from StoreList");
            }
        });

        // TEMPORARY FOR TESTING PURPOSES
        /*Button logoutButton = rootView.findViewById(R.id.buttonLogout);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                NavController navController = NavHostFragment.findNavController(FragmentStoreList.this);
                navController.navigate(R.id.action_StoreList_to_logout);
            }
        });*/

        ImageButton storesButton = rootView.findViewById(R.id.button_stores);
        storesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = NavHostFragment.findNavController(FragmentStoreList.this);
                NavDestination currentDestination = navController.getCurrentDestination();
                if (currentDestination != null && currentDestination.getId() == R.id.StoreList) {
                    // User is already on StoreList fragment, do nothing
                    return;
                }
            }
        });

        ImageButton cartButton = rootView.findViewById(R.id.button_cart);
        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = NavHostFragment.findNavController(FragmentStoreList.this);
                navController.navigate(R.id.action_StoreList_to_Cart);
            }
        });

        // logout button
        logoutButton = rootView.findViewById(R.id.logoutButton1);
        name_display = rootView.findViewById(R.id.textviewName);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if (user==null){
            NavHostFragment.findNavController(FragmentStoreList.this)
                    .navigate(R.id.action_StoreList_to_WelcomeScreen);
        }
        else{
            name_display.setText(user.getEmail());
        }

        logoutButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                name_display.setText("called");
                FirebaseAuth.getInstance().signOut();
                NavHostFragment.findNavController(FragmentStoreList.this)
                        .navigate(R.id.action_StoreList_to_WelcomeScreen);
            }
        });

        return rootView;
    }

    @Override
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

    @Override
    public void onItemClick(Store store) {
        if (store == null) {
            Log.e("FragmentStoreList", "Store object is null");
        } else {
            Log.d("FragmentStoreList", "FragmentStoreList StoreID is" + store.getStoreID());
        }
        Bundle bundle = new Bundle();
        bundle.putSerializable("store", store);
        NavController navController = NavHostFragment.findNavController(this);
        navController.navigate(R.id.action_StoreList_to_ProductsOffered, bundle);
    }

}
