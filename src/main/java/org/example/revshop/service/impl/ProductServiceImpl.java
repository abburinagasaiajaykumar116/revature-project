package org.example.revshop.service.impl;

import org.example.revshop.exception.BadRequestException;
import org.example.revshop.exception.ResourceNotFoundException;
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

        if (product == null)
            throw new BadRequestException("Product data is missing");

        if (product.getPrice() <= 0)
            throw new BadRequestException("Product price must be greater than 0");

        repo.save(product);
        return true;
    }

    @Override
    public List<Product> viewAllProducts() {

        // Fetch only active products
        List<Product> products = repo.findByIsActiveTrue();

        // Clean image URL if needed
        for (Product product : products) {
            if (product.getImageUrl() == null || "null".equals(product.getImageUrl())) {
                product.setImageUrl(null);
            }
        }

        return products;
    }
    @Override
    public Product getProductById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));
    }
    @Override
    public List<Product> viewProductsByCategory(int categoryId) {
        return repo.findByCategoryIdAndIsActiveTrue(categoryId);
    }
    @Override
    public List<Product> searchProducts(String keyword) {
        if (keyword == null || keyword.trim().isEmpty())
            throw new BadRequestException("Search keyword cannot be empty");

        return repo.findByNameContainingIgnoreCaseAndIsActiveTrue(keyword);
    }

    @Override
    public List<Product> viewSellerProducts(int sellerId) {
             return repo.findBySellerIdAndIsActiveTrue(sellerId);
    }

    @Override
    public boolean updateProduct(Product product) {
        if (product.getProductId() == null)
            throw new BadRequestException("Product id is required for update");

        repo.findById(product.getProductId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Cannot update. Product not found with id " + product.getProductId()));
        repo.save(product);
        return true;
    }

    @Override
    public boolean deleteProduct(Long productId, int sellerId) {

        Product p = repo.findById(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found with id " + productId));

        if (p.getSellerId() != sellerId)
            throw new BadRequestException("You are not allowed to delete this product");
        p.setIsActive(false);
        repo.save(p);

        return true;
    }
}
