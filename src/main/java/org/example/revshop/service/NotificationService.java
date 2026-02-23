package org.example.revshop.service;

import org.example.revshop.model.Notification;

import java.util.List;

public interface NotificationService {
    public void notifyUser(Integer userId, String msg);

    public List<Notification> getUserNotifications(Integer userId) ;
}
