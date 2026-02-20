package org.example.revshop.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.revshop.dtos.FavoriteView;
import org.example.revshop.model.Favorite;
import org.example.revshop.model.Product;
import org.example.revshop.repos.FavoriteRepository;
import org.example.revshop.repos.ProductRepository;
import org.example.revshop.service.FavoriteService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final ProductRepository productRepository;

    @Override
    public List<FavoriteView> getFavorites(Integer userId) {
        return favoriteRepository.findFavoritesByUser(userId);
    }

    @Override
    public void addFavorite(Integer userId, Integer productId) {

        if (favoriteRepository.existsByUserIdAndProduct_ProductId(userId, productId)) {
            return;
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Favorite fav = new Favorite();
        fav.setUserId(userId);
        fav.setProduct(product);

        favoriteRepository.save(fav);
    }
}
