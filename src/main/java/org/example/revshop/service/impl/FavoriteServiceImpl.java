package org.example.revshop.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.revshop.dtos.FavoriteView;
import org.example.revshop.exception.BadRequestException;
import org.example.revshop.exception.ResourceNotFoundException;
import org.example.revshop.model.Favorite;
import org.example.revshop.model.Product;
import org.example.revshop.repos.FavoriteRepository;
import org.example.revshop.repos.ProductRepository;
import org.example.revshop.service.FavoriteService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final ProductRepository productRepository;

    @Override
    public List<FavoriteView> getFavorites(Integer userId) {
        if (userId == null)
            throw new BadRequestException("User id is required");


        return favoriteRepository.findFavoritesByUser(userId);
    }

    @Override
    public void addFavorite(Integer userId, Long productId) {

        if (userId == null || productId == null)
            throw new BadRequestException("User id and product id are required");
        if (favoriteRepository.existsByUserIdAndProduct_ProductId(userId, productId)) {
            return;
        }


        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found with id " + productId));

        Favorite fav = new Favorite();
        fav.setUserId(userId);
        fav.setProduct(product);

        favoriteRepository.save(fav);
    }
    @Transactional
    @Override
    public void removeFavorite(Integer userId, Long productId) {
        if (userId == null || productId == null)
            throw new BadRequestException("User id and product id are required");
        favoriteRepository.deleteByUserIdAndProduct_ProductId(userId, productId);
    }
}
