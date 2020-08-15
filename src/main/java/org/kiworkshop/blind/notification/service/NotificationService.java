package org.kiworkshop.blind.notification.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.kiworkshop.blind.notification.model.Notification;
import org.kiworkshop.blind.notification.model.NotificationRepository;
import org.kiworkshop.blind.notification.model.Watch;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public void createNotification(List<Watch> watches, Notification.EventType event) {
        List<Notification> notifications = watches.stream()
            .map(watch -> watch.createNotification(event))
            .collect(Collectors.toList());
        notificationRepository.saveAll(notifications);
    }

    public List<Notification> getNotifications(Long userId) {
        List<Notification> notifications = notificationRepository.findAllByUserIdOrderByIdDesc(userId);
        return notifications;
    }

    public void readNotification(Long notificationId, Long userId) {
        Notification notification = findNotificationBy(notificationId);
        if (!notification.isOwner(userId)) {
            throw new IllegalStateException("해당 유저의 알림이 아닙니다.");
        }
        notification.readNotification();
    }

    public void deleteNotification(Long notificationId, Long userId) {
        Notification notification = findNotificationBy(notificationId);
        if (!notification.isOwner(userId)) {
            throw new IllegalStateException("해당 유저의 알림이 아닙니다.");
        }
        notificationRepository.deleteById(notificationId);
    }

    private Notification findNotificationBy(Long notificationId) {
        return notificationRepository.findById(notificationId)
            .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 알림입니다."));
    }
}
