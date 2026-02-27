package org.example.revshop.serviceimpltest;

import org.example.revshop.dtos.ReviewView;
import org.example.revshop.dtos.SellerReviewView;
import org.example.revshop.model.Product;
import org.example.revshop.model.Review;
import org.example.revshop.model.User;
import org.example.revshop.repos.ProductRepository;
import org.example.revshop.repos.ReviewRepository;
import org.example.revshop.repos.UserRepository;
import org.example.revshop.service.impl.ReviewServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {

    @Mock
    private ReviewRepository reviewRepo;

    @Mock
    private ProductRepository productRepo;

    @Mock
    private UserRepository userRepo;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    // ✅ GIVE REVIEW SUCCESS
    @Test
    void testGiveReview_Success() {

        User user = new User();
        user.setUserId(1);
        user.setName("Ajay");

        Product product = new Product();
        product.setProductId(100L);

        when(userRepo.findById(1)).thenReturn(Optional.of(user));
        when(productRepo.findById(100L)).thenReturn(Optional.of(product));

        ReviewView result = reviewService.giveReview(
                1,
                100L,
                5,
                "Excellent product"
        );

        // Capture saved review
        ArgumentCaptor<Review> captor =
                ArgumentCaptor.forClass(Review.class);

        verify(reviewRepo).save(captor.capture());

        Review savedReview = captor.getValue();

        assertEquals(user, savedReview.getUser());
        assertEquals(product, savedReview.getProduct());
        assertEquals(5, savedReview.getRating());
        assertEquals("Excellent product", savedReview.getComment());

        // Verify returned DTO
        assertEquals("Ajay", result.getUserName());
        assertEquals(5, result.getRating());
        assertEquals("Excellent product", result.getComment());
    }

    // ❌ USER NOT FOUND
    @Test
    void testGiveReview_UserNotFound() {

        when(userRepo.findById(1)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> reviewService.giveReview(
                        1, 100L, 5, "Nice"));
    }

    // ❌ PRODUCT NOT FOUND
    @Test
    void testGiveReview_ProductNotFound() {

        User user = new User();
        user.setUserId(1);

        when(userRepo.findById(1)).thenReturn(Optional.of(user));
        when(productRepo.findById(100L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> reviewService.giveReview(
                        1, 100L, 5, "Nice"));
    }

    // ✅ VIEW REVIEWS FOR PRODUCT
    @Test
    void testViewReviewsForProduct() {

        ReviewView reviewView = new ReviewView(
                "Ajay",
                5,
                "Great!"
        );

        when(reviewRepo.findReviewsByProduct(100L))
                .thenReturn(List.of(reviewView));

        List<ReviewView> result =
                reviewService.viewReviewsForProduct(100L);

        assertEquals(1, result.size());
        assertEquals("Ajay", result.get(0).getUserName());

        verify(reviewRepo).findReviewsByProduct(100L);
    }

    // ✅ VIEW REVIEWS FOR SELLER
    @Test
    void testViewReviewsForSeller() {

        SellerReviewView sellerReview =
                mock(SellerReviewView.class);

        when(reviewRepo.findReviewsForSeller(10L))
                .thenReturn(List.of(sellerReview));

        List<SellerReviewView> result =
                reviewService.viewReviewsForSeller(10L);

        assertEquals(1, result.size());

        verify(reviewRepo).findReviewsForSeller(10L);
    }
}