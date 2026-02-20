package org.example.revshop.repos;

import org.example.revshop.dtos.FavoriteView;
import org.example.revshop.model.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    @Query("""
    SELECT new org.example.revshop.dtos.FavoriteView(
        p.productId,
        p.name,
        p.description,
        p.price
    )
    FROM Favorite f
    JOIN f.product p
    WHERE f.userId = :userId
    """)
    List<FavoriteView> findFavoritesByUser(@Param("userId") Integer userId);

    boolean existsByUserIdAndProduct_ProductId(Integer userId, Integer productId);
}
