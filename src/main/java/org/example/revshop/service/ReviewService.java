package org.example.revshop.service;

import org.example.revshop.dtos.ReviewView;
import org.example.revshop.dtos.SellerReviewView;
import org.example.revshop.model.Product;
import org.example.revshop.model.Review;

import java.util.List;

public interface ReviewService {
    public ReviewView giveReview(Integer userId,
                           Long productId,
                           Integer rating,
                           String comment) ;
    List<ReviewView> viewReviewsForProduct(Long productId);

    public List<SellerReviewView> viewReviewsForSeller(Long sellerId);
}
