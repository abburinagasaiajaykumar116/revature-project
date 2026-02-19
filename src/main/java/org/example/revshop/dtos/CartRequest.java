package org.example.revshop.dtos;
public class CartRequest {

    private Long productId;

    public int getQuantity() {
        return quantity;
    }

    public Long getProductId() {
        return productId;
    }

    private int quantity;
}
