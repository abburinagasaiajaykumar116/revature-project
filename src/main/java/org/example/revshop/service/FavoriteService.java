package org.example.revshop.service;

import org.example.revshop.dtos.FavoriteView;

import java.util.List;

public interface FavoriteService {

    List<FavoriteView> getFavorites(Integer userId);

    void addFavorite(Integer userId, Long productId);

    public void removeFavorite(Integer userId, Long productId);
}
