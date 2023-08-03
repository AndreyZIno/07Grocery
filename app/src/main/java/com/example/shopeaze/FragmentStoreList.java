package com.example.shopeaze;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FragmentStoreList extends Fragment implements StoreAdapter.OnItemClickListener{

    private List<Store> stores;
    private StoreAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_store_list, container, false);

        RecyclerView recyclerViewStores = rootView.findViewById(R.id.recyclerViewStores);
        recyclerViewStores.setLayoutManager(new LinearLayoutManager(getActivity()));

        StoreList storeList = new StoreList();
        stores = storeList.getAllStores();

        adapter = new StoreAdapter(stores, this);
        recyclerViewStores.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onItemClick(Store store) {
        openProductsOfferedFragment(store.getStoreID());
    }

    private void openProductsOfferedFragment(String storeID) {
        ProductsOfferedFragment fragment = ProductsOfferedFragment.newInstance(storeID);
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}

