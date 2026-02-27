package org.example.revshop.service.impl;

import jakarta.transaction.Transactional;
import org.example.revshop.dtos.CartResponse;
import org.example.revshop.exception.BadRequestException;
import org.example.revshop.exception.ResourceNotFoundException;
import org.example.revshop.model.Cart;
import org.example.revshop.model.CartItem;
import org.example.revshop.model.Product;
import org.example.revshop.repos.CartItemRepository;
import org.example.revshop.repos.CartRepository;
import org.example.revshop.repos.ProductRepository;
import org.example.revshop.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CartServiceImpl implements CartService {
    @Autowired
    private CartRepository cartRepo;
    @Autowired private CartItemRepository itemRepo;

    public Cart getOrCreateCart(Integer userId) {
        if (userId == null)
            throw new BadRequestException("User id is required");

        return cartRepo.findByUserId(userId)
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    cart.setUserId(userId);
                    return cartRepo.save(cart);
                });
    }

    public void add(Integer userId, Long productId, int qty) {
        if (productId == null)
            throw new BadRequestException("Product id is required");

        if (qty <= 0)
            throw new BadRequestException("Quantity must be greater than 0");

        // ensure product exists
        productRepo.findById(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found with id " + productId));

        Cart cart = getOrCreateCart(userId);

        CartItem item = itemRepo
                .findByCartIdAndProductId(cart.getCartId(), productId)
                .orElse(new CartItem());

        item.setCartId(cart.getCartId());
        item.setProductId(productId);
        item.setQuantity(item.getQuantity() + qty);

        itemRepo.save(item);
    }

    @Autowired
    private ProductRepository productRepo;

    public List<CartResponse> view(Integer userId) {


        Cart cart = cartRepo.findByUserId(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Cart not found for user " + userId));

        List<CartItem> items =
                itemRepo.findByCartId(cart.getCartId());

        return items.stream().map(item -> {

            Product product = productRepo.findById(item.getProductId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Product not found with id " + item.getProductId()));

            return new CartResponse(
                    item.getCartItemId(),
                    product.getProductId(),
                    product.getName(),
                    product.getImageUrl(),
                    product.getPrice(),
                    product.getMrp(),
                    item.getQuantity()
            );

        }).toList();
    }


    public void remove(Integer userId, Long productId) {

        Cart cart = getOrCreateCart(userId);

        itemRepo.findByCartIdAndProductId(cart.getCartId(), productId)
                .ifPresent(itemRepo::delete);
    }
    public void updateQuantity(Integer userId,
                               Long productId,
                               int quantity) {

        if (quantity <= 0)
            throw new BadRequestException("Quantity must be greater than 0");

        Cart cart = getOrCreateCart(userId);

        CartItem item = itemRepo
                .findByCartIdAndProductId(cart.getCartId(), productId)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found in cart"));

        item.setQuantity(quantity);

        itemRepo.save(item);
    }

    public void clear(Integer userId) {

        Cart cart = getOrCreateCart(userId);
        itemRepo.deleteByCartId(cart.getCartId());
    }

    public void removeItem(Integer userId, Long productId) {

        Cart cart = getOrCreateCart(userId);

        itemRepo.findByCartIdAndProductId(cart.getCartId(), productId)
                .ifPresent(itemRepo::delete);
    }


}

