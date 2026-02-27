package org.example.revshop.serviceimpltest;

import org.example.revshop.dtos.FavoriteView;
import org.example.revshop.exception.BadRequestException;
import org.example.revshop.exception.ResourceNotFoundException;
import org.example.revshop.model.Favorite;
import org.example.revshop.model.Product;
import org.example.revshop.repos.FavoriteRepository;
import org.example.revshop.repos.ProductRepository;
import org.example.revshop.service.impl.FavoriteServiceImpl;
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
class Favoriteserviceimpltest {

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private FavoriteServiceImpl favoriteService;

    // ✅ getFavorites success
    @Test
    void testGetFavorites_Success() {

        FavoriteView view = mock(FavoriteView.class);

        when(favoriteRepository.findFavoritesByUser(1))
                .thenReturn(List.of(view));

        List<FavoriteView> result = favoriteService.getFavorites(1);

        assertEquals(1, result.size());
        verify(favoriteRepository).findFavoritesByUser(1);
    }

    // ❌ getFavorites null user
    @Test
    void testGetFavorites_NullUser() {

        assertThrows(BadRequestException.class,
                () -> favoriteService.getFavorites(null));
    }

    // ✅ addFavorite success
    @Test
    void testAddFavorite_Success() {

        Product product = new Product();
        product.setProductId(100L);

        when(favoriteRepository.existsByUserIdAndProduct_ProductId(1, 100L))
                .thenReturn(false);

        when(productRepository.findById(100L))
                .thenReturn(Optional.of(product));

        favoriteService.addFavorite(1, 100L);

        verify(favoriteRepository).save(any(Favorite.class));
    }

    // ✅ addFavorite already exists (should not save again)
    @Test
    void testAddFavorite_AlreadyExists() {

        when(favoriteRepository.existsByUserIdAndProduct_ProductId(1, 100L))
                .thenReturn(true);

        favoriteService.addFavorite(1, 100L);

        verify(favoriteRepository, never()).save(any());
        verify(productRepository, never()).findById(any());
    }

    // ❌ addFavorite product not found
    @Test
    void testAddFavorite_ProductNotFound() {

        when(favoriteRepository.existsByUserIdAndProduct_ProductId(1, 100L))
                .thenReturn(false);

        when(productRepository.findById(100L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> favoriteService.addFavorite(1, 100L));
    }

    // ❌ addFavorite null values
    @Test
    void testAddFavorite_InvalidInput() {

        assertThrows(BadRequestException.class,
                () -> favoriteService.addFavorite(null, 100L));

        assertThrows(BadRequestException.class,
                () -> favoriteService.addFavorite(1, null));
    }

    // ✅ removeFavorite success
    @Test
    void testRemoveFavorite_Success() {

        favoriteService.removeFavorite(1, 100L);

        verify(favoriteRepository)
                .deleteByUserIdAndProduct_ProductId(1, 100L);
    }

    // ❌ removeFavorite invalid input
    @Test
    void testRemoveFavorite_InvalidInput() {

        assertThrows(BadRequestException.class,
                () -> favoriteService.removeFavorite(null, 100L));

        assertThrows(BadRequestException.class,
                () -> favoriteService.removeFavorite(1, null));
    }
}