package org.example.revshop.serviceimpltest;


import org.example.revshop.dtos.OrderHistoryItem;
import org.example.revshop.model.Order;
import org.example.revshop.model.Product;
import org.example.revshop.model.OrderItem ; // ⚠ adjust if needed
import org.example.revshop.repos.OrderRepository;
import org.example.revshop.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    // ✅ Test getOrderHistory success
    @Test
    void testGetOrderHistory_Success() {

        // ----- Product -----
        Product product = new Product();
        product.setName("Laptop");

        // ----- OrderItem (Entity) -----
        org.example.revshop.model.OrderItem entityItem =
                new org.example.revshop.model.OrderItem();
        entityItem.setProduct(product);
        entityItem.setQuantity(2);
        entityItem.setPrice(50000.0);

        // ----- Order -----
        Order order = new Order();
        order.setOrderId(10L);
        order.setTotalAmount(100000.0);
        order.setStatus("DELIVERED");
        order.setOrderItems(List.of(entityItem));

        when(orderRepository.findByUser_UserId(1))
                .thenReturn(List.of(order));

        // ----- Call service -----
        List<OrderHistoryItem> result =
                orderService.getOrderHistory(1);

        // ----- Assertions -----
        assertEquals(1, result.size());

        OrderHistoryItem historyItem = result.get(0);

        assertEquals(10L, historyItem.getOrderId());
        assertEquals(100000.0, historyItem.getTotalAmount());
        assertEquals("DELIVERED", historyItem.getStatus());

        assertEquals(1, historyItem.getItems().size());
        assertEquals("Laptop",
                historyItem.getItems().get(0).getProductName());
        assertEquals(2,
                historyItem.getItems().get(0).getQuantity());
        assertEquals(50000.0,
                historyItem.getItems().get(0).getPrice());

        verify(orderRepository, times(1))
                .findByUser_UserId(1);
    }

    // ✅ Test empty order history
    @Test
    void testGetOrderHistory_Empty() {

        when(orderRepository.findByUser_UserId(1))
                .thenReturn(List.of());

        List<OrderHistoryItem> result =
                orderService.getOrderHistory(1);

        assertTrue(result.isEmpty());
        verify(orderRepository).findByUser_UserId(1);
    }
}