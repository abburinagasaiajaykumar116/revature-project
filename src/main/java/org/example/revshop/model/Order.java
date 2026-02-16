package org.example.revshop.model;


public class Order {

    private int orderId;
    private int userId;
    private double totalAmount;
    private String status;
    private String shippingAddress;
    private String billingAddress;

    public Order() {}

    public Order(int orderId, int userId, double totalAmount,
                 String status, String shippingAddress,
                 String billingAddress) {
        this.orderId = orderId;
        this.userId = userId;
        this.totalAmount = totalAmount;
        this.status = status;
        this.shippingAddress = shippingAddress;
        this.billingAddress = billingAddress;
    }

    // Getters & Setters
    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getBillingAddress() { return billingAddress; }
    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }
}

