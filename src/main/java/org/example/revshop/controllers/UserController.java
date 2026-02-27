package org.example.revshop.controllers;



import org.example.revshop.dtos.UserProfileResponse;
import org.example.revshop.model.User;
import org.example.revshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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


    //Get security question
    @GetMapping("/security-question")
    public ResponseEntity<?> getSecurityQuestion(@RequestParam String email) {
        return ResponseEntity.ok(
                Map.of("question", userService.getSecurityQuestion(email))
        );
    }

    @PutMapping("/change-password")
    public Map<String, String> changePassword(Authentication auth,
                                              @RequestBody Map<String, String> body) {

        String newPassword = body.get("newPassword");

        User user = userService.getByEmail(auth.getName());
        userService.changePassword(user.getUserId(), newPassword);

        return Map.of("message", "Password changed successfully");
    }

    //Forgot password
    @PutMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email,
                                            @RequestParam String answer,
                                            @RequestParam String newPassword) {

        userService.forgotPassword(email, answer, newPassword);

        return ResponseEntity.ok(
                Map.of("message", "Password reset successfully")
        );
    }
}

