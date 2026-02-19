package org.example.revshop.service.impl;

import jakarta.transaction.Transactional;
import org.example.revshop.model.Cart;
import org.example.revshop.model.CartItem;
import org.example.revshop.repos.CartItemRepository;
import org.example.revshop.repos.CartRepository;
import org.example.revshop.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class CartServiceImpl implements CartService {
    @Autowired
    private CartRepository cartRepo;
    @Autowired private CartItemRepository itemRepo;

    public Cart getOrCreateCart(Long userId) {

        return cartRepo.findByUserId(userId)
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    cart.setUserId(Math.toIntExact(userId));
                    return cartRepo.save(cart);
                });
    }

    public void add(Long userId, Long productId, int qty) {

        Cart cart = getOrCreateCart(userId);

        CartItem item = itemRepo
                .findByCartIdAndProductId(cart.getCartId(), productId)
                .orElse(new CartItem());

        item.setCartId(cart.getCartId());
        item.setProductId(productId);
        item.setQuantity(item.getQuantity() + qty);

        itemRepo.save(item);
    }

    public List<CartItem> view(Long userId) {

        Cart cart = getOrCreateCart(userId);
        return itemRepo.findByCartId(cart.getCartId());
    }

    public void remove(Long userId, Long productId) {

        Cart cart = getOrCreateCart(userId);

        itemRepo.findByCartIdAndProductId(cart.getCartId(), productId)
                .ifPresent(itemRepo::delete);
    }
    public void updateQuantity(Long userId,
                               Long productId,
                               int quantity) {

        if (quantity <= 0) {
            throw new RuntimeException("Quantity must be > 0");
        }

        Cart cart = getOrCreateCart(userId);

        CartItem item = itemRepo
                .findByCartIdAndProductId(cart.getCartId(), productId)
                .orElseThrow(() -> new RuntimeException("Item not found in cart"));

        item.setQuantity(quantity);

        itemRepo.save(item);
    }

    public void clear(Long userId) {

        Cart cart = getOrCreateCart(userId);
        itemRepo.deleteByCartId(cart.getCartId());
    }
}

