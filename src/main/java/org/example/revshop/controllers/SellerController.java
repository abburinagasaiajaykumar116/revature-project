package org.example.revshop.controllers;

import lombok.RequiredArgsConstructor;
import org.example.revshop.dtos.SellerOrderView;
import org.example.revshop.dtos.SellerReviewView;
import org.example.revshop.model.Product;
import org.example.revshop.model.User;
import org.example.revshop.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/seller")
@RequiredArgsConstructor
public class SellerController {

    private final SellerService sellerService;
    private  final ReviewService reviewService;
    @Autowired
    private UserService userService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private ProductService productService;

    @GetMapping("/orders")
    public List<SellerOrderView> getSellerOrders(Authentication auth) {

        User seller = userService.getByEmail(auth.getName());
        return sellerService.viewOrdersForSeller((long) seller.getUserId());
    }
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Map<String, String>> addProduct(
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam Double price,
            @RequestParam Double mrp,
            @RequestParam Double discount,
            @RequestParam Integer stockQuantity,
            @RequestParam Integer stockThreshold,
            @RequestParam Integer categoryId,
            @RequestParam("image") MultipartFile image,
            Authentication authentication
    ) {

        String email = authentication.getName();
        User seller = userService.getByEmail(email);

        String imageUrl = imageService.uploadImage(image);

        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setMrp(mrp);
        product.setDiscount(discount);
        product.setStockQuantity(stockQuantity);
        product.setStockThreshold(stockThreshold);
        product.setCategoryId(categoryId);
        product.setSellerId(seller.getUserId());
        product.setImageUrl(imageUrl);
        product.setIsActive(true);
        productService.addProduct(product);

        return  ResponseEntity.ok(
                Map.of("message", "Product added successfully")
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> updateProduct(
            @PathVariable Long id,
            @RequestBody Product product) {

        product.setProductId(id);
        productService.updateProduct(product);

        return ResponseEntity.ok(
                Map.of("message", "Product updated successfully")
        );
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id,
                                           Authentication authentication) {

        String email = authentication.getName();
        User seller = userService.getByEmail(email);

        productService.deleteProduct(id, seller.getUserId().intValue());

        return ResponseEntity.ok(
                Map.of("message", "Product deleted successfully")
        );
    }
    @GetMapping("/inventory")
    public List<Product> sellerProducts(Authentication authentication) {

        String email = authentication.getName();

        User seller = userService.getByEmail(email);

        return productService.viewSellerProducts(seller.getUserId().intValue());
    }
    @GetMapping("/reviews")
    public List<SellerReviewView> getSellerReviews(Authentication auth) {
        User seller = userService.getByEmail(auth.getName());

        return reviewService.viewReviewsForSeller((long) seller.getUserId());
    }
}



