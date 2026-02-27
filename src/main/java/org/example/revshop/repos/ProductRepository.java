package org.example.revshop.repos;



import org.example.revshop.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategoryId(Integer categoryId);

    List<Product> findBySellerId(Integer sellerId);

    List<Product> findByNameContainingIgnoreCase(String keyword);
    List<Product> findByIsActiveTrue();
    List<Product> findByCategoryIdAndIsActiveTrue(Integer categoryId);
    List<Product> findBySellerIdAndIsActiveTrue(Integer sellerId);
    List<Product> findByNameContainingIgnoreCaseAndIsActiveTrue(String keyword);
}
