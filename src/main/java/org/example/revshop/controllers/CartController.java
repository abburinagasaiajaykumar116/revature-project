package org.example.revshop.controllers;

import org.example.revshop.dtos.CartRequest;
import org.example.revshop.dtos.UpdateCartRequest;
import org.example.revshop.model.CartItem;
import org.example.revshop.service.CartService;
import org.example.revshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;
    @Autowired private UserService userService;

    private Long getUserId(Authentication auth) {
        return Long.valueOf(userService.getByEmail(auth.getName()).getUserId());
    }

    @PostMapping
    public String add(Authentication auth,
                      @RequestBody CartRequest req) {

        cartService.add(
                getUserId(auth),
                req.getProductId(),
                req.getQuantity()
        );

        return "Added to cart";
    }

    @GetMapping
    public List<CartItem> view(Authentication auth) {
        return cartService.view(getUserId(auth));
    }

    @DeleteMapping("/{productId}")
    public String remove(Authentication auth,
                         @PathVariable Long productId) {

        cartService.remove(getUserId(auth), productId);
        return "Removed";
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
}
