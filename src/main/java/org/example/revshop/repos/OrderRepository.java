package org.example.revshop.repos;

import org.example.revshop.dtos.OrderHistoryItem;
import org.example.revshop.dtos.SellerOrderView;
import org.example.revshop.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("""
SELECT new org.example.revshop.dtos.SellerOrderView(
    o.orderId,
    p.name,
    o.user.name,
    oi.quantity,
    oi.price,
    o.status,
    o.shippingAddress
)
FROM Order o
JOIN o.orderItems oi
JOIN oi.product p
WHERE p.sellerId = :sellerId
ORDER BY o.orderId DESC
""")
    List<SellerOrderView> findOrdersForSeller(@Param("sellerId") Long sellerId);
    List<Order> findByUser_UserId(Integer userId);




}


