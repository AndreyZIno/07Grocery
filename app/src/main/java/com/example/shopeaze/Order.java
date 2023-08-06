package com.example.shopeaze;

public class Order {
    private String userId;
    private String status;

    public Order() {}  // Default constructor required for Firebase

    public Order(String userId, String status) {
        this.userId = userId;
        this.status = status;
    }

    // Getters and setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
