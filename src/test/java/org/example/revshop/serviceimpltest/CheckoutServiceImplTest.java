package org.example.revshop.serviceimpltest;

import org.example.revshop.exception.BadRequestException;
import org.example.revshop.model.*;
import org.example.revshop.repos.*;
import org.example.revshop.service.NotificationService;
import org.example.revshop.service.PaymentService;
import org.example.revshop.service.impl.CheckoutServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CheckoutServiceImplTest {

    @Mock private CartRepository cartRepo;
    @Mock private CartItemRepository itemRepo;
    @Mock private ProductRepository productRepo;
    @Mock private OrderRepository orderRepo;
    @Mock private OrderItemRepository orderItemRepo;
    @Mock private PaymentService paymentService;
    @Mock private NotificationService notificationService;
    @Mock private UserRepository userRepo;

    @InjectMocks
    private CheckoutServiceImpl checkoutService;

    private Cart cart;
    private User user;
    private Product product;
    private CartItem cartItem;

    @BeforeEach
    void setup() {

        cart = new Cart();
        cart.setCartId(1L);
        cart.setUserId(1);

        user = new User();
        user.setUserId(1);

        product = new Product();
        product.setProductId(100L);
        product.setPrice(1000.0);
        product.setStockQuantity(10);
        product.setStockThreshold(2);
        product.setSellerId(5);
        product.setName("Laptop");

        cartItem = new CartItem();
        cartItem.setCartId(1L);
        cartItem.setProductId(100L);
        cartItem.setQuantity(2);
    }

    // ✅ SUCCESS CASE
    @Test
    void testCheckout_Success() {

        when(cartRepo.findByUserId(1)).thenReturn(Optional.of(cart));
        when(userRepo.findById(1)).thenReturn(Optional.of(user));
        when(itemRepo.findByCartId(1L)).thenReturn(List.of(cartItem));
        when(productRepo.findById(100L)).thenReturn(Optional.of(product));
        when(paymentService.processPayment(2000.0, "UPI")).thenReturn(true);

        Order savedOrder = new Order();
        savedOrder.setOrderId(10L);
        when(orderRepo.saveAndFlush(any(Order.class)))
                .thenAnswer(invocation -> {
                    Order orderArg = invocation.getArgument(0);
                    orderArg.setOrderId(10L);   // simulate DB generated ID
                    return orderArg;
                });

        Long orderId = checkoutService.checkout(
                1,
                "Shipping Address",
                "Billing Address",
                "UPI"
        );

        assertEquals(10L, orderId);

        verify(orderRepo).saveAndFlush(any(Order.class));
        verify(orderItemRepo).save(any(OrderItem.class));
        verify(productRepo, atLeastOnce()).save(product);
        verify(notificationService, atLeastOnce()).notifyUser(anyInt(), anyString());
        verify(itemRepo).deleteByCartId(1L);
    }

    // ❌ Cart Empty
    @Test
    void testCheckout_EmptyCart() {

        when(cartRepo.findByUserId(1)).thenReturn(Optional.of(cart));
        when(userRepo.findById(1)).thenReturn(Optional.of(user));
        when(itemRepo.findByCartId(1L)).thenReturn(List.of());

        assertThrows(RuntimeException.class, () ->
                checkoutService.checkout(
                        1,
                        "Shipping",
                        "Billing",
                        "UPI"
                ));
    }

    // ❌ Payment Failure
    @Test
    void testCheckout_PaymentFailed() {

        when(cartRepo.findByUserId(1)).thenReturn(Optional.of(cart));
        when(userRepo.findById(1)).thenReturn(Optional.of(user));
        when(itemRepo.findByCartId(1L)).thenReturn(List.of(cartItem));
        when(productRepo.findById(100L)).thenReturn(Optional.of(product));
        when(paymentService.processPayment(2000.0, "INVALID"))
                .thenReturn(false);

        assertThrows(IllegalArgumentException.class, () ->
                checkoutService.checkout(
                        1,
                        "Shipping",
                        "Billing",
                        "INVALID"
                ));
    }

    // ❌ Validation Error - Null User
    @Test
    void testCheckout_UserNull() {

        assertThrows(BadRequestException.class, () ->
                checkoutService.checkout(
                        null,
                        "Shipping",
                        "Billing",
                        "UPI"
                ));
    }

    // ❌ Validation Error - Missing Shipping
    @Test
    void testCheckout_InvalidShipping() {

        assertThrows(BadRequestException.class, () ->
                checkoutService.checkout(
                        1,
                        "",
                        "Billing",
                        "UPI"
                ));
    }
}