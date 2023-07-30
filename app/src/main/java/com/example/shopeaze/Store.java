package com.example.shopeaze;

import java.util.UUID;

public class Store {

    private String storeId;
    private String name;
    private String logoUrl;
    private ProductList productList;

    // constructor
    public Store(String storeId, String name, String logoUrl) {
        this.name = name;
        this.storeId = generateStoreID();
        this.logoUrl = logoUrl;
        this.productList = new ProductList();
    }

    private String generateStoreID() {
        String rand = UUID.randomUUID().toString();
        return rand;
    }

    // getters
    public String getStoreID() {
        return storeId;
    }

    public String getStoreName() {
        return name;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public ProductList getProductList() {
        return productList;
    }

    // setters
    public void setStoreID(String id) {
        this.storeId = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public void setProductList(ProductList productList) {
        this.productList = productList;
    }

}
