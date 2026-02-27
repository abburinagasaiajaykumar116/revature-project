package org.example.revshop.dtos;

public class SellerReviewView {

    private String productName;
    private String customerName;
    private Integer rating;
    private String comment;

    public SellerReviewView(String productName,
                            String customerName,
                            Integer rating,
                            String comment) {
        this.productName = productName;
        this.customerName = customerName;
        this.rating = rating;
        this.comment = comment;
    }

    public String getProductName() { return productName; }
    public String getCustomerName() { return customerName; }
    public Integer getRating() { return rating; }
    public String getComment() { return comment; }
}
