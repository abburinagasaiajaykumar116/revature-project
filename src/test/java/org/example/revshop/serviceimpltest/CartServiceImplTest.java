package org.example.revshop.serviceimpltest;



import org.example.revshop.dtos.CartResponse;
import org.example.revshop.exception.BadRequestException;
import org.example.revshop.exception.ResourceNotFoundException;
import org.example.revshop.model.Cart;
import org.example.revshop.model.CartItem;
import org.example.revshop.model.Product;
import org.example.revshop.repos.CartItemRepository;
import org.example.revshop.repos.CartRepository;
import org.example.revshop.repos.ProductRepository;
import org.example.revshop.service.impl.CartServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

    @Mock
    private CartRepository cartRepo;

    @Mock
    private CartItemRepository itemRepo;

    @Mock
    private ProductRepository productRepo;

    @InjectMocks
    private CartServiceImpl cartService;

    private Cart cart;

    @BeforeEach
    void setup() {
        cart = new Cart();
        cart.setCartId(1L);
        cart.setUserId(1);
    }

    // ✅ Test getOrCreateCart - existing cart
    @Test
    void testGetOrCreateCart_Existing() {
        when(cartRepo.findByUserId(1)).thenReturn(Optional.of(cart));

        Cart result = cartService.getOrCreateCart(1);

        assertEquals(1L, result.getCartId());
        verify(cartRepo, times(1)).findByUserId(1);
    }

    // ✅ Test getOrCreateCart - create new
    @Test
    void testGetOrCreateCart_New() {
        when(cartRepo.findByUserId(1)).thenReturn(Optional.empty());
        when(cartRepo.save(any(Cart.class))).thenReturn(cart);

        Cart result = cartService.getOrCreateCart(1);

        assertNotNull(result);
        verify(cartRepo).save(any(Cart.class));
    }

    // ✅ Test add product successfully
    @Test
    void testAddProduct() {

        Product product = new Product();
        product.setProductId(100L);

        when(productRepo.findById(100L)).thenReturn(Optional.of(product));
        when(cartRepo.findByUserId(1)).thenReturn(Optional.of(cart));
        when(itemRepo.findByCartIdAndProductId(1L, 100L))
                .thenReturn(Optional.empty());

        cartService.add(1, 100L, 2);

        verify(itemRepo, times(1)).save(any(CartItem.class));
    }

    // ❌ Test add with invalid quantity
    @Test
    void testAdd_InvalidQuantity() {
        assertThrows(BadRequestException.class,
                () -> cartService.add(1, 100L, 0));
    }

    // ❌ Test add with product not found
    @Test
    void testAdd_ProductNotFound() {
        when(productRepo.findById(100L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> cartService.add(1, 100L, 2));
    }

    // ✅ Test view cart
    @Test
    void testViewCart() {

        Product product = new Product();
        product.setProductId(100L);
        product.setName("Laptop");
        product.setPrice(50000.0);
        product.setMrp(55000.0);
        product.setImageUrl("image.jpg");

        CartItem item = new CartItem();
        item.setCartItemId(10L);
        item.setCartId(1L);
        item.setProductId(100L);
        item.setQuantity(2);

        when(cartRepo.findByUserId(1)).thenReturn(Optional.of(cart));
        when(itemRepo.findByCartId(1L)).thenReturn(List.of(item));
        when(productRepo.findById(100L)).thenReturn(Optional.of(product));

        List<CartResponse> responses = cartService.view(1);

        assertEquals(1, responses.size());
        assertEquals("Laptop", responses.get(0).getProductName());
    }

    // ❌ Test view cart not found
    @Test
    void testViewCart_NotFound() {
        when(cartRepo.findByUserId(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> cartService.view(1));
    }

    // ✅ Test update quantity
    @Test
    void testUpdateQuantity() {

        CartItem item = new CartItem();
        item.setCartItemId(10L);
        item.setCartId(1L);
        item.setProductId(100L);
        item.setQuantity(2);

        when(cartRepo.findByUserId(1)).thenReturn(Optional.of(cart));
        when(itemRepo.findByCartIdAndProductId(1L, 100L))
                .thenReturn(Optional.of(item));

        cartService.updateQuantity(1, 100L, 5);

        assertEquals(5, item.getQuantity());
        verify(itemRepo).save(item);
    }

    // ❌ Test update with invalid quantity
    @Test
    void testUpdateQuantity_Invalid() {
        assertThrows(BadRequestException.class,
                () -> cartService.updateQuantity(1, 100L, 0));
    }

    // ✅ Test clear cart
    @Test
    void testClearCart() {
        when(cartRepo.findByUserId(1)).thenReturn(Optional.of(cart));

        cartService.clear(1);

        verify(itemRepo).deleteByCartId(1L);
    }

    // ✅ Test remove item
    @Test
    void testRemoveItem() {

        CartItem item = new CartItem();
        item.setCartId(1L);
        item.setProductId(100L);

        when(cartRepo.findByUserId(1)).thenReturn(Optional.of(cart));
        when(itemRepo.findByCartIdAndProductId(1L, 100L))
                .thenReturn(Optional.of(item));

        cartService.removeItem(1, 100L);

        verify(itemRepo).delete(item);
    }
}
