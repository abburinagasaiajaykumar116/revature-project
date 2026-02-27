package org.example.revshop.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.revshop.dtos.ReviewView;
import org.example.revshop.dtos.SellerReviewView;
import org.example.revshop.model.Product;
import org.example.revshop.model.Review;
import org.example.revshop.model.User;
import org.example.revshop.repos.ProductRepository;
import org.example.revshop.repos.ReviewRepository;
import org.example.revshop.repos.UserRepository;
import org.example.revshop.service.ReviewService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepo;
    private final ProductRepository productRepo;
    private  final UserRepository userRepo;
    @Transactional
    public ReviewView giveReview(Integer userId,
                                 Long productId,
                                 Integer rating,
                                 String comment) {

        User user = userRepo.findById(userId).orElseThrow();
        Product product = productRepo.findById(productId).orElseThrow();

        Review review = new Review(user, product, rating, comment);

        reviewRepo.save(review);

        return new ReviewView(
                user.getName(),   // or getFullName()
                rating,
                comment
        );
    }
    @Override
    public List<ReviewView> viewReviewsForProduct(Long productId) {
        return reviewRepo.findReviewsByProduct(productId);
    }
    public List<SellerReviewView> viewReviewsForSeller(Long sellerId) {
        return reviewRepo.findReviewsForSeller(sellerId);
    }
}
