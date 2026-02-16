package org.example.revshop.controllers;



import org.example.revshop.dtos.UserProfileResponse;
import org.example.revshop.model.User;
import org.example.revshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public UserProfileResponse getProfile(Authentication authentication) {

        String email = authentication.getName();

        User user = userService.getByEmail(email);

        return new UserProfileResponse(
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getBusinessDetails()
        );
    }
}

