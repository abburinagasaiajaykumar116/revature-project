package org.example.revshop.dtos;

public class SellerOrderView {

    private Long orderId;
    private String productName;
    private String customerName;
    private Integer quantity;
    private Double price;
    private String shippingAddress;

    public Long getOrderId() {
        return orderId;
    }

    public String getProductName() {
        return productName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Double getPrice() {
        return price;
    }

    public String getStatus() {
        return status;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    private String status;

    public SellerOrderView(Long orderId,
                           String productName,
                           String customerName,
                           Integer quantity,
                           Double price,
                           String status,
                           String shippingAddress) {
        this.orderId = orderId;
        this.productName = productName;
        this.customerName = customerName;
        this.quantity = quantity;
        this.price = price;
        this.status = status;
        this.shippingAddress = shippingAddress;
    }
}
