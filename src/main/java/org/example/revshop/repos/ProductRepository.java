package org.example.revshop.repos;



import org.example.revshop.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findByCategoryId(Integer categoryId);

    List<Product> findBySellerId(Integer sellerId);

    List<Product> findByNameContainingIgnoreCase(String keyword);

}
