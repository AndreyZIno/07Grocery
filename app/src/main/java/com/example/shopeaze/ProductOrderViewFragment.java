package com.example.shopeaze;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ProductOrderViewFragment extends Fragment {
    //Firebase reference
    private static final String ARG_PRODUCT_ID = "product_id";

    public static ProductOrderViewFragment newInstance(String productID) {
        ProductOrderViewFragment fragment = new ProductOrderViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PRODUCT_ID, productID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_product_order_view, container, false);
        return rootView;
    }

}