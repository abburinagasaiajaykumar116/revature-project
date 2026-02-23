package org.example.revshop.repos;

import org.example.revshop.dtos.ReviewView;
import org.example.revshop.dtos.SellerReviewView;
import org.example.revshop.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("""
        SELECT new org.example.revshop.dtos.SellerReviewView(
            p.name,
            r.rating,
            r.comment
        )
        FROM Review r
        JOIN r.product p
        WHERE p.sellerId = :sellerId
    """)
    List<SellerReviewView> findReviewsForSeller(Long sellerId);
    @Query("""
SELECT new org.example.revshop.dtos.ReviewView(
    r.user.name,
    r.rating,
    r.comment
)
FROM Review r
WHERE r.product.productId = :productId
""")
    List<ReviewView> findReviewsByProduct(Long productId);
}
