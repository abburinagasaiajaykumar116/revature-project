package org.example.revshop.controllers;


import org.example.revshop.model.Product;
import org.example.revshop.service.ProductService;
import org.example.revshop.service.UserService;
import org.example.revshop.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    // ✅ SELLER → Add product
    @PostMapping
    @PreAuthorize("hasRole('SELLER')")
    public String addProduct(@RequestBody Product product,
                             Authentication authentication) {

        String email = authentication.getName();

        User seller = userService.getByEmail(email);

        // map sellerId from JWT
        product.setSellerId(seller.getUserId().intValue());

        productService.addProduct(product);

        return "Product added successfully";
    }

    // ✅ PUBLIC → View all products
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.viewAllProducts();
    }

    // ✅ PUBLIC → Search
    @GetMapping("/search")
    public List<Product> searchProducts(@RequestParam String keyword) {
        return productService.searchProducts(keyword);
    }

    // ✅ PUBLIC → Filter by category
    @GetMapping("/category/{categoryId}")
    public List<Product> byCategory(@PathVariable int categoryId) {
        return productService.viewProductsByCategory(categoryId);
    }

    // ✅ SELLER → Update
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SELLER')")
    public String updateProduct(@PathVariable int id,
                                @RequestBody Product product) {

        product.setProductId((long) id);

        productService.updateProduct(product);

        return "Product updated";
    }

    // ✅ SELLER → Delete
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SELLER')")
    public String deleteProduct(@PathVariable int id,
                                Authentication authentication) {

        String email = authentication.getName();

        User seller = userService.getByEmail(email);

        productService.deleteProduct(id, seller.getUserId().intValue());

        return "Product deleted";
    }

    // ✅ SELLER → My inventory
    @GetMapping("/seller")
    @PreAuthorize("hasRole('SELLER')")
    public List<Product> sellerProducts(Authentication authentication) {

        String email = authentication.getName();

        User seller = userService.getByEmail(email);

        return productService.viewSellerProducts(seller.getUserId().intValue());
    }
}
