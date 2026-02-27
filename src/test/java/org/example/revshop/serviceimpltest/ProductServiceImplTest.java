package org.example.revshop.serviceimpltest;


import org.example.revshop.exception.BadRequestException;
import org.example.revshop.exception.ResourceNotFoundException;
import org.example.revshop.model.Product;
import org.example.revshop.repos.ProductRepository;
import org.example.revshop.service.impl.ProductServiceImpl;
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
class ProductServiceImplTest {

    @Mock
    private ProductRepository repo;

    @InjectMocks
    private ProductServiceImpl productService;

    // ✅ ADD PRODUCT SUCCESS
    @Test
    void testAddProduct_Success() {

        Product product = new Product();
        product.setPrice(1000.0);

        boolean result = productService.addProduct(product);

        assertTrue(result);
        verify(repo).save(product);
    }

    // ❌ ADD PRODUCT NULL
    @Test
    void testAddProduct_Null() {

        assertThrows(BadRequestException.class,
                () -> productService.addProduct(null));
    }

    // ❌ ADD PRODUCT INVALID PRICE
    @Test
    void testAddProduct_InvalidPrice() {

        Product product = new Product();
        product.setPrice(0D);

        assertThrows(BadRequestException.class,
                () -> productService.addProduct(product));
    }

    // ✅ VIEW ALL PRODUCTS (Image cleanup logic)
    @Test
    void testViewAllProducts_ImageCleanup() {

        Product p1 = new Product();
        p1.setImageUrl("null");

        Product p2 = new Product();
        p2.setImageUrl(null);

        Product p3 = new Product();
        p3.setImageUrl("image.jpg");

        when(repo.findByIsActiveTrue())
                .thenReturn(List.of(p1, p2, p3));

        List<Product> result = productService.viewAllProducts();

        assertNull(result.get(0).getImageUrl());
        assertNull(result.get(1).getImageUrl());
        assertEquals("image.jpg", result.get(2).getImageUrl());
    }

    // ✅ GET PRODUCT BY ID
    @Test
    void testGetProductById_Success() {

        Product product = new Product();
        product.setProductId(1L);

        when(repo.findById(1L))
                .thenReturn(Optional.of(product));

        Product result = productService.getProductById(1L);

        assertEquals(1L, result.getProductId());
    }

    // ❌ GET PRODUCT NOT FOUND
    @Test
    void testGetProductById_NotFound() {

        when(repo.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> productService.getProductById(1L));
    }

    // ✅ SEARCH PRODUCTS
    @Test
    void testSearchProducts_Success() {

        when(repo.findByNameContainingIgnoreCaseAndIsActiveTrue("lap"))
                .thenReturn(List.of(new Product()));

        List<Product> result = productService.searchProducts("lap");

        assertEquals(1, result.size());
    }

    // ❌ SEARCH EMPTY KEYWORD
    @Test
    void testSearchProducts_Invalid() {

        assertThrows(BadRequestException.class,
                () -> productService.searchProducts(" "));
    }

    // ✅ UPDATE PRODUCT SUCCESS
    @Test
    void testUpdateProduct_Success() {

        Product product = new Product();
        product.setProductId(1L);

        when(repo.findById(1L))
                .thenReturn(Optional.of(product));

        boolean result = productService.updateProduct(product);

        assertTrue(result);
        verify(repo).save(product);
    }

    // ❌ UPDATE PRODUCT NO ID
    @Test
    void testUpdateProduct_NoId() {

        Product product = new Product();

        assertThrows(BadRequestException.class,
                () -> productService.updateProduct(product));
    }

    // ❌ UPDATE PRODUCT NOT FOUND
    @Test
    void testUpdateProduct_NotFound() {

        Product product = new Product();
        product.setProductId(1L);

        when(repo.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> productService.updateProduct(product));
    }

    // ✅ DELETE PRODUCT SUCCESS
    @Test
    void testDeleteProduct_Success() {

        Product product = new Product();
        product.setProductId(1L);
        product.setSellerId(10);
        product.setIsActive(true);

        when(repo.findById(1L))
                .thenReturn(Optional.of(product));

        boolean result = productService.deleteProduct(1L, 10);

        assertTrue(result);
        assertFalse(product.getIsActive());
        verify(repo).save(product);
    }

    // ❌ DELETE PRODUCT WRONG SELLER
    @Test
    void testDeleteProduct_WrongSeller() {

        Product product = new Product();
        product.setProductId(1L);
        product.setSellerId(10);

        when(repo.findById(1L))
                .thenReturn(Optional.of(product));

        assertThrows(BadRequestException.class,
                () -> productService.deleteProduct(1L, 99));
    }

    // ❌ DELETE PRODUCT NOT FOUND
    @Test
    void testDeleteProduct_NotFound() {

        when(repo.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> productService.deleteProduct(1L, 10));
    }
}