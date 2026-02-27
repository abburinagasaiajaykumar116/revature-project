package org.example.revshop.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.revshop.exception.BadRequestException;
import org.example.revshop.model.*;
import org.example.revshop.repos.*;
import org.example.revshop.service.CheckoutService;
import org.example.revshop.service.NotificationService;
import org.example.revshop.service.PaymentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class CheckoutServiceImpl implements CheckoutService {

    private final CartRepository cartRepo;
    private final CartItemRepository itemRepo;
    private final ProductRepository productRepo;
    private final OrderRepository orderRepo;
    private final OrderItemRepository orderItemRepo;
    private final PaymentService paymentService;
    private final NotificationService notificationService;
    private final UserRepository userRepo;

    @Override
    public Long checkout(Integer userId,
                         String shipping,
                         String billing,
                         String paymentMethod) {
        if (userId == null)
            throw new BadRequestException("User id is required");

        if (shipping == null || shipping.trim().isEmpty())
            throw new BadRequestException("Shipping address is required");

        if (billing == null || billing.trim().isEmpty())
            throw new BadRequestException("Billing address is required");

        if (paymentMethod == null || paymentMethod.trim().isEmpty())
            throw new BadRequestException("Payment method is required");

        Cart cart = cartRepo.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<CartItem> items = itemRepo.findByCartId(cart.getCartId());

        if (items.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        double total = 0;

        // 🔹 Calculate total
        for (CartItem item : items) {

            Product product = productRepo.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            total += product.getPrice() * item.getQuantity();
        }

        // 🔹 Process payment
        if (!paymentService.processPayment(total, paymentMethod)) {
            throw new IllegalArgumentException("Invalid payment method: " + paymentMethod);
        }

        // 🔹 Create Order
        Order order = new Order();
        order.setUser(user);
        order.setTotalAmount(total);
        order.setStatus("PLACED");
        order.setShippingAddress(shipping);
        order.setBillingAddress(billing);

        orderRepo.saveAndFlush(order);

        // 🔹 Process each cart item
        for (CartItem item : items) {

            Product product = productRepo.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            // Create order item
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPrice(product.getPrice());

            orderItemRepo.save(orderItem);

            // Update stock
            int updatedStock = product.getStockQuantity() - item.getQuantity();
            product.setStockQuantity(updatedStock);

            productRepo.save(product);

            // 🔔 Notify Seller - New Order
            notificationService.notifyUser(
                    product.getSellerId().intValue(),
                    "New order received (Order ID: " + order.getOrderId() + ")"
            );

            // 🔔 Notify Seller - Low Stock Alert
            if (product.getStockThreshold() != null &&
                    updatedStock <= product.getStockThreshold()) {

                notificationService.notifyUser(
                        product.getSellerId().intValue(),
                        "Low stock alert for product: " + product.getName()
                );
            }
        }

        // 🔔 Notify Buyer
        notificationService.notifyUser(
                userId,
                "Order placed successfully #" + order.getOrderId()
        );

        // 🔹 Clear cart
        itemRepo.deleteByCartId(cart.getCartId());

        return (long) order.getOrderId();
    }
}
