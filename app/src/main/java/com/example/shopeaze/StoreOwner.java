package com.example.shopeaze;

import com.example.shopeaze.AccountOwner;
import com.google.firebase.ktx.Firebase;

import java.util.ArrayList;

public class StoreOwner extends Account {
    String storeName;

   public ProductList products;



    public StoreOwner(String username, String password, String storeName){
        this.storeName = storeName;
        products = new ProductList();
    }

    public String getStoreName(){
        return storeName;
    }

    @Override
    public boolean login(String username, String password) {
        return false;
    }

    public Object getProducts() {
        return products.getAllProducts();
    }

    public void addProduct(Product product) {
        products.addProduct(product);
    }
}
