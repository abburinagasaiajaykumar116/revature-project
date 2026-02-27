package org.example.revshop.serviceimpltest;


import org.example.revshop.dtos.SellerOrderView;
import org.example.revshop.repos.OrderRepository;
import org.example.revshop.service.impl.SellerServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SellerServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private SellerServiceImpl sellerService;

    // ✅ SUCCESS CASE
    @Test
    void testViewOrdersForSeller_Success() {

        SellerOrderView orderView = mock(SellerOrderView.class);

        when(orderRepository.findOrdersForSeller(10L))
                .thenReturn(List.of(orderView));

        List<SellerOrderView> result =
                sellerService.viewOrdersForSeller(10L);

        assertEquals(1, result.size());
        verify(orderRepository, times(1))
                .findOrdersForSeller(10L);
    }

    // ✅ EMPTY LIST CASE
    @Test
    void testViewOrdersForSeller_Empty() {

        when(orderRepository.findOrdersForSeller(10L))
                .thenReturn(List.of());

        List<SellerOrderView> result =
                sellerService.viewOrdersForSeller(10L);

        assertTrue(result.isEmpty());
        verify(orderRepository).findOrdersForSeller(10L);
    }
}