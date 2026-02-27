package org.example.revshop.controllers;


import org.example.revshop.model.Product;
import org.example.revshop.service.ImageService;
import org.example.revshop.service.ProductService;
import org.example.revshop.service.UserService;
import org.example.revshop.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;


    @Autowired
    private ImageService imageService;




    // View all products
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.viewAllProducts();
    }

    // Search
    @GetMapping("/search")
    public List<Product> searchProducts(@RequestParam String keyword) {
        return productService.searchProducts(keyword);
    }

    //  Filter by category
    @GetMapping("/category/{categoryId}")
    public List<Product> byCategory(@PathVariable int categoryId) {
        return productService.viewProductsByCategory(categoryId);
    }



    //  View single product
    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }
}
