package org.example.revshop.dtos;
public class CartRequest {

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    private Long productId;
    private int quantity;
    public int getQuantity() {
        return quantity;
    }

    public Long getProductId() {
        return productId;
    }


}
