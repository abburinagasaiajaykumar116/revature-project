package org.example.revshop.service;

import org.example.revshop.dtos.CartResponse;
import org.example.revshop.model.Cart;
import org.example.revshop.model.CartItem;

import java.util.List;

public interface CartService{

    public Cart getOrCreateCart(Integer userId) ;
    public void add(Integer userId, Long productId, int qty) ;

    public List<CartResponse> view(Integer userId) ;
    public void remove(Integer userId, Long productId);
    public void updateQuantity(Integer userId,
                               Long productId,
                               int quantity) ;
    public void removeItem(Integer userId, Long productId);

    public void clear(Integer userId) ;
}


