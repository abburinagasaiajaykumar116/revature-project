package org.example.revshop.controllers;

import lombok.RequiredArgsConstructor;
import org.example.revshop.dtos.FavoriteView;
import org.example.revshop.model.User;
import org.example.revshop.service.FavoriteService;
import org.example.revshop.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;
    private final UserService userService;

    // View favorites
    @GetMapping
    public List<FavoriteView> getFavorites(Authentication auth) {

        User user = userService.getByEmail(auth.getName());

        return favoriteService.getFavorites(user.getUserId());
    }

    // Add favorite
    @PostMapping("/{productId}")
    public String addFavorite(Authentication auth,
                              @PathVariable Long productId) {

        User user = userService.getByEmail(auth.getName());

        favoriteService.addFavorite(user.getUserId(), productId);

        return "Added to favorites";
    }
    @DeleteMapping("/{productId}")
    public String removeFavorite(Authentication auth,
                                 @PathVariable Long productId) {

        User user = userService.getByEmail(auth.getName());
        favoriteService.removeFavorite(user.getUserId(), productId);

        return "Removed from favorites";
    }
}
