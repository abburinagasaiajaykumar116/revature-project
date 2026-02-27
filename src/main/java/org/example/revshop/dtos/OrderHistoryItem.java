package org.example.revshop.dtos;

import java.util.List;

public class OrderHistoryItem {

    private Long orderId;
    private Double totalAmount;
    private String status;
    private List<OrderItem> items;

    public OrderHistoryItem(Long orderId,
                           Double totalAmount,
                           String status,
                           List<OrderItem> items) {
        this.orderId = orderId;
        this.totalAmount = totalAmount;
        this.status = status;
        this.items = items;
    }

    public Long getOrderId() { return orderId; }
    public Double getTotalAmount() { return totalAmount; }
    public String getStatus() { return status; }
    public List<OrderItem> getItems() { return items; }
}