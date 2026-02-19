package org.example.revshop.service;

import org.example.revshop.model.Cart;
import org.example.revshop.model.CartItem;

import java.util.List;

public interface CartService{

        public Cart getOrCreateCart(Long userId) ;
        public void add(Long userId, Long productId, int qty) ;

        public List<CartItem> view(Long userId) ;
        public void remove(Long userId, Long productId);
    public void updateQuantity(Long userId,
                               Long productId,
                               int quantity) ;


    public void clear(Long userId) ;
}


