package org.example.revshop.service;

import org.example.revshop.model.Product;

import java.util.List;

public interface ProductService {
    public boolean addProduct(Product product) ;

    public List<Product> viewAllProducts() ;

    public List<Product> viewProductsByCategory(int categoryId);


    public List<Product> searchProducts(String keyword);

    public List<Product> viewSellerProducts(int sellerId) ;


    public boolean updateProduct(Product product);


    public boolean deleteProduct(int productId, int sellerId) ;
}
