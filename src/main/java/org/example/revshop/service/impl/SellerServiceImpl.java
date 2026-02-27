package org.example.revshop.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.revshop.dtos.SellerOrderView;
import org.example.revshop.repos.OrderRepository;
import org.example.revshop.service.SellerService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class SellerServiceImpl implements SellerService {
    private final OrderRepository orderRepository;

    public List<SellerOrderView> viewOrdersForSeller(Long sellerId) {
        return orderRepository.findOrdersForSeller(sellerId);
    }
}
