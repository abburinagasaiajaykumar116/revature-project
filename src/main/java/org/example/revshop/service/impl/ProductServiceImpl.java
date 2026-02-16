package org.example.revshop.service.impl;

import org.example.revshop.model.Product;
import org.example.revshop.repos.ProductRepository;
import org.example.revshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository repo;

    @Override
    public boolean addProduct(Product product) {

        if (product.getPrice() <= 0)
            throw new RuntimeException("Invalid price");

        repo.save(product);
        return true;
    }

    @Override
    public List<Product> viewAllProducts() {
        return repo.findAll();
    }

    @Override
    public List<Product> viewProductsByCategory(int categoryId) {
        return repo.findByCategoryId(categoryId);
    }

    @Override
    public List<Product> searchProducts(String keyword) {
        return repo.findByNameContainingIgnoreCase(keyword);
    }

    @Override
    public List<Product> viewSellerProducts(int sellerId) {
        return repo.findBySellerId(sellerId);
    }

    @Override
    public boolean updateProduct(Product product) {
        repo.save(product);
        return true;
    }

    @Override
    public boolean deleteProduct(int productId, int sellerId) {

        Product p = repo.findById(productId)
                .orElseThrow();

        p.setIsActive(false);
        repo.save(p);

        return true;
    }
}
