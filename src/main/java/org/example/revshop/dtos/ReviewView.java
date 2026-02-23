package org.example.revshop.dtos;

public class ReviewView {

    private String userName;
    private Integer rating;
    private String comment;

    public ReviewView(String userName, Integer rating, String comment) {
        this.userName = userName;
        this.rating = rating;
        this.comment = comment;
    }

    public String getUserName() { return userName; }
    public Integer getRating() { return rating; }
    public String getComment() { return comment; }
}