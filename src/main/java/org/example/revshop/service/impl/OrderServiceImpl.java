package org.example.revshop.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.revshop.dtos.OrderHistoryItem;
import org.example.revshop.dtos.OrderItem;
import org.example.revshop.model.Order;
import org.example.revshop.repos.OrderRepository;
import org.example.revshop.service.OrderService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Override
    public List<OrderHistoryItem> getOrderHistory(Integer userId) {

        List<Order> orders = orderRepository.findByUser_UserId(userId);

        return orders.stream().map(order -> {

            List<OrderItem> items = order.getOrderItems().stream()
                    .map(oi -> new OrderItem(
                            oi.getProduct().getName(),
                            oi.getQuantity(),
                            oi.getPrice()
                    ))
                    .toList();

            return new OrderHistoryItem(
                    order.getOrderId(),
                    order.getTotalAmount(),
                    order.getStatus(),
                    items
            );

        }).toList();
    }
}
