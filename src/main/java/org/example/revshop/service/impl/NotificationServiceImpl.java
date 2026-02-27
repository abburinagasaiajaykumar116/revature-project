package org.example.revshop.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.revshop.model.Notification;
import org.example.revshop.repos.NotificationRepository;
import org.example.revshop.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository repo;

    public NotificationServiceImpl(NotificationRepository repo) {
        this.repo = repo;
    }

    public void notifyUser(Integer userId, String msg) {

        Notification n = new Notification();
        n.setUserId(userId);
        n.setMessage(msg);

        repo.save(n);
    }

    public List<Notification> getUserNotifications(Integer userId) {
        return repo.findByUserIdOrderByCreatedAtDesc(userId);
    }
}
