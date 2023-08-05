package com.example.shopeaze;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.List;

public class ProductListFragment extends Fragment implements ProductAdapter.OnItemClickListener, ProductList.OnProductsLoadedListener {

    private static final String TAG = "ProductListFragment";
    private static final String ARG_STORE_ID = "store_id";
    private ProductAdapter adapter;
    private RecyclerView recyclerViewProducts;
    private ProductList productList;

    public static ProductListFragment newInstance(String storeID) {
        Log.d(TAG, "Creating new ProductListFragment for store ID: " + storeID);
        ProductListFragment fragment = new ProductListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_STORE_ID, storeID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "Creating view for ProductListFragment");
        View rootView = inflater.inflate(R.layout.fragment_product_list, container, false);

        recyclerViewProducts = rootView.findViewById(R.id.recyclerViewProducts);
        recyclerViewProducts.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        String storeID = getArguments().getString(ARG_STORE_ID);
        Log.d(TAG, "Store ID: " + storeID);

        productList = new ProductList(storeID);
        productList.setOnProductsLoadedListener(this);

        return rootView;
    }

    @Override
    public void onProductsLoaded(List<Product> products) {
        adapter = new ProductAdapter(products, this);
        recyclerViewProducts.setAdapter(adapter);
    }

    @Override
    public void onItemClick(Product product) {
        Log.d(TAG, "Product clicked: " + product.getName());
        openStoreProductDetailsFragment(product.getProductID());
    }

    private void openStoreProductDetailsFragment(String productID) {
        Log.d(TAG, "Opening StoreProductDetailsFragment for product ID: " + productID);
        StoreProductDetailsFragment fragment = StoreProductDetailsFragment.newInstance(productID);
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void showAddProductDialog() {
        // Create and show a dialog to gather product information from the user
        AddProductDialog dialog = new AddProductDialog();
        dialog.show(requireActivity().getSupportFragmentManager(), "AddProductDialog");
    }
}

