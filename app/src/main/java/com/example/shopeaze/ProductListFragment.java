package com.example.shopeaze;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ProductListFragment extends Fragment {
    private List<Product> products;
    private ProductAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_product_list, container, false);

        RecyclerView recyclerViewProducts = rootView.findViewById(R.id.recyclerViewProducts);
        recyclerViewProducts.setLayoutManager(new LinearLayoutManager(getActivity()));

        ProductList productList = new ProductList();
        products = productList.getAllProducts();

        adapter = new ProductAdapter(products);
        recyclerViewProducts.setAdapter(adapter);

        FloatingActionButton fabAddProduct = rootView.findViewById(R.id.fabAddProduct);
        fabAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddProductDialog();
            }
        });

        return rootView;
    }

    private void showAddProductDialog() {
        // Create and show a dialog to gather product information from the user
        AddProductDialog dialog = new AddProductDialog();
        dialog.show(getParentFragmentManager(), "AddProductDialog");
    }

    // Method to add a new product to the list and refresh the RecyclerView
    public void addProduct(Product product) {
        products.add(product);
        adapter.notifyDataSetChanged();
    }
}
