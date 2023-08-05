package com.example.shopeaze;

import java.util.List;

public class Order {
    private String orderId;
    private String userId;
    private String status;

    //List of items (if item class exists)
    private List<Product> productList;

    // Empty constructor needed for Firestore
    public Order() {}

    public Order(String userId, String status, List<Product> productList) {
        this.userId = userId;
        this.status = status;
        this.productList = productList;
    }

    // Getter methods
    public String getOrderId() {
        return orderId;
    }

    public String getUserId() {
        return userId;
    }

    public String getStatus() {
        return status;
    }

    public List<Product> getProductList() {
        return productList;
    }

    // Setter methods
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
