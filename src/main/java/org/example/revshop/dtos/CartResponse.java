package org.example.revshop.dtos;

public class CartResponse {

    private Long cartItemId;
    private Long productId;
    private String productName;

    public Long getCartItemId() {
        return cartItemId;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public double getPrice() {
        return price;
    }

    public double getMrp() {
        return mrp;
    }

    public int getQuantity() {
        return quantity;
    }

    private String imageUrl;
    private double price;
    private double mrp;
    private int quantity;

    // constructor
    public CartResponse(Long cartItemId,
                        Long productId,
                        String productName,
                        String imageUrl,
                        double price,
                        double mrp,
                        int quantity) {
        this.cartItemId = cartItemId;
        this.productId = productId;
        this.productName = productName;
        this.imageUrl = imageUrl;
        this.price = price;
        this.mrp = mrp;
        this.quantity = quantity;
    }

}
