package org.example.revshop.service;

import lombok.RequiredArgsConstructor;
import org.example.revshop.dtos.SellerOrderView;
import org.example.revshop.repos.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;


public interface SellerService {
    public List<SellerOrderView> viewOrdersForSeller(Long sellerId) ;

}
