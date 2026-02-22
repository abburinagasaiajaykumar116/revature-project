package org.example.revshop.dtos;

public class SellerReviewView {

    private String productName;
    private Integer rating;
    private String comment;

    public SellerReviewView(String productName,
                            Integer rating,
                            String comment) {
        this.productName = productName;
        this.rating = rating;
        this.comment = comment;
    }

    public String getProductName() { return productName; }
    public Integer getRating() { return rating; }
    public String getComment() { return comment; }
}
