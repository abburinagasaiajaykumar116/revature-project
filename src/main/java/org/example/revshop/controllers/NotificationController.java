package org.example.revshop.controllers;



import lombok.RequiredArgsConstructor;
import org.example.revshop.model.Notification;
import org.example.revshop.model.User;
import org.example.revshop.service.NotificationService;
import org.example.revshop.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final UserService userService;
    // Get notifications for user
    @GetMapping
    public List<Notification> getNotifications(Authentication auth) {

        User user = userService.getByEmail(auth.getName());

        return notificationService.getUserNotifications(user.getUserId());
    }

    //  Create notification (for testing/admin)
    @PostMapping("/send")
    public String sendNotification(@RequestParam Integer userId,
                                   @RequestParam String message) {

        notificationService.notifyUser(userId, message);
        return "Notification sent";
    }

}
