package org.example.revshop.service;

import org.example.revshop.dtos.OrderHistoryItem;

import java.util.List;

public interface OrderService {
    List<OrderHistoryItem> getOrderHistory(Integer userId);

}
