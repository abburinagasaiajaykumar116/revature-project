package org.example.revshop.dtos;

public class FavoriteView {

    private Long productId;
    private String productName;
    private String description;
    private Double price;
    private String imageUrl;

    public FavoriteView(Long productId,
                        String productName,
                        String description,
                        Double price,String imageUrl) {
        this.productId = productId;
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public Long getProductId() { return productId; }
    public String getProductName() { return productName; }
    public String getDescription() { return description; }
    public Double getPrice() { return price; }

    public String getImageUrl() {
        return imageUrl;
    }
}
