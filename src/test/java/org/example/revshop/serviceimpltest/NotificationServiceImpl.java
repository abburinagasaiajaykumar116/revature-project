package org.example.revshop.service.impl;

import org.example.revshop.model.Notification;
import org.example.revshop.repos.NotificationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceImplTest {

    @Mock
    private NotificationRepository repo;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    // ✅ Test notifyUser()
    @Test
    void testNotifyUser_Success() {

        notificationService.notifyUser(1, "Order placed successfully");

        ArgumentCaptor<Notification> captor =
                ArgumentCaptor.forClass(Notification.class);

        verify(repo, times(1)).save(captor.capture());

        Notification savedNotification = captor.getValue();

        assertEquals(1, savedNotification.getUserId());
        assertEquals("Order placed successfully",
                savedNotification.getMessage());
    }

    // ✅ Test getUserNotifications()
    @Test
    void testGetUserNotifications_Success() {

        Notification n1 = new Notification();
        n1.setUserId(1);
        n1.setMessage("Message 1");

        Notification n2 = new Notification();
        n2.setUserId(1);
        n2.setMessage("Message 2");

        when(repo.findByUserIdOrderByCreatedAtDesc(1))
                .thenReturn(List.of(n1, n2));

        List<Notification> result =
                notificationService.getUserNotifications(1);

        assertEquals(2, result.size());
        assertEquals("Message 1", result.get(0).getMessage());

        verify(repo, times(1))
                .findByUserIdOrderByCreatedAtDesc(1);
    }
}