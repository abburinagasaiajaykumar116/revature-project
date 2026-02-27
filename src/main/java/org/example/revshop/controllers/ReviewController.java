package org.example.revshop.controllers;

import lombok.RequiredArgsConstructor;
import org.example.revshop.dtos.ReviewRequest;
import org.example.revshop.dtos.ReviewView;
import org.example.revshop.dtos.SellerReviewView;
import org.example.revshop.model.User;
import org.example.revshop.service.ReviewService;
import org.example.revshop.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final UserService userService;

    @PostMapping("/{productId}")
    public ReviewView giveReview(Authentication auth,
                                 @PathVariable Long productId,
                                 @RequestBody ReviewRequest req) {

        User user = userService.getByEmail(auth.getName());

        return reviewService.giveReview(
                user.getUserId(),
                productId,
                req.getRating(),
                req.getComment()
        );
    }
    @GetMapping("/product/{productId}")
    public List<ReviewView> productReviews(@PathVariable Long productId) {
        return reviewService.viewReviewsForProduct(productId);
    }
    @GetMapping("/seller/{sellerId}")
    public List<SellerReviewView> sellerReviews(@PathVariable Long sellerId) {
        return reviewService.viewReviewsForSeller(sellerId);
    }
}
