package org.example.revshop.controllers;

import org.example.revshop.dtos.CartRequest;
import org.example.revshop.dtos.CartResponse;
import org.example.revshop.dtos.UpdateCartRequest;
import org.example.revshop.model.CartItem;
import org.example.revshop.service.CartService;
import org.example.revshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;
    @Autowired private UserService userService;

    private Integer getUserId(Authentication auth) {
        return Integer.valueOf(userService.getByEmail(auth.getName()).getUserId());
    }

    @PostMapping
    public ResponseEntity<Map<String,String>> add(Authentication auth,
                      @RequestBody CartRequest req) {

        cartService.add(
                getUserId(auth),
                req.getProductId(),
                req.getQuantity()
        );

        return ResponseEntity.ok().body(Map.of("message", "Added to cart"));

    }
        @GetMapping
    public List<CartResponse> view(Authentication auth) {
        return cartService.view(getUserId(auth));
    }


    //Update quantity
    @PutMapping("/{productId}")
    public String updateQuantity(Authentication auth,
                                 @PathVariable Long productId,
                                 @RequestBody UpdateCartRequest req) {

        cartService.updateQuantity(
                getUserId(auth),
                productId,
                req.getQuantity()
        );

        return "Quantity updated";
    }
    // Clear entire cart
    @DeleteMapping("/clear")
    public String clear(Authentication auth) {

        cartService.clear(getUserId(auth));
        return "Cart cleared";
    }
    @DeleteMapping("/{productId}")
    public String removeItem(Authentication auth,
                             @PathVariable Long productId) {

        cartService.removeItem(getUserId(auth), productId);
        return "Item removed";
    }

}
