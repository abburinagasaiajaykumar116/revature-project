package org.example.revshop.controllers;



import lombok.RequiredArgsConstructor;
import org.example.revshop.dtos.CheckoutRequest;
import org.example.revshop.dtos.OrderHistoryItem;
import org.example.revshop.model.User;
import org.example.revshop.service.CheckoutService;
import org.example.revshop.service.OrderService;
import org.example.revshop.service.UserService;
import org.example.revshop.service.impl.CheckoutServiceImpl;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final CheckoutService checkoutService;
    private final UserService userService;
    private final OrderService orderService;
    @PostMapping("/checkout")
    public Map<String, Object> checkout(Authentication auth,
                           @RequestBody CheckoutRequest req) {

        // Get logged-in user
        User user = userService.getByEmail(auth.getName());

        Long orderId = checkoutService.checkout(
                user.getUserId().intValue(),
                req.getShippingAddress(),
                req.getBillingAddress(),
                req.getPaymentMethod()
        );

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Order placed successfully");
        response.put("orderId", orderId);

        return response;
    }

    @GetMapping("/history")
    public List<OrderHistoryItem> getHistory(Authentication auth) {

        User user = userService.getByEmail(auth.getName());
        return orderService.getOrderHistory(user.getUserId());
    }

}
