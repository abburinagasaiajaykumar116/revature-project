package org.example.revshop.dtos;

public class FavoriteView {

    private Long productId;
    private String productName;
    private String description;
    private Double price;

    public FavoriteView(Long productId,
                        String productName,
                        String description,
                        Double price) {
        this.productId = productId;
        this.productName = productName;
        this.description = description;
        this.price = price;
    }

    public Long getProductId() { return productId; }
    public String getProductName() { return productName; }
    public String getDescription() { return description; }
    public Double getPrice() { return price; }
}
