package org.kiworkshop.blind.notification.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.kiworkshop.blind.notification.service.WatchServiceTest.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kiworkshop.blind.notification.model.Notification;
import org.kiworkshop.blind.notification.model.NotificationRepository;
import org.kiworkshop.blind.notification.model.Watch;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {
    private static final Notification NOTIFICATION = getNotificationFixture();
    private NotificationService notificationService;
    @Mock
    private NotificationRepository notificationRepository;

    @BeforeEach
    void setUp() {
        notificationService = new NotificationService(notificationRepository);
    }

    @Test
    void createNotification() {
        List<Watch> watches = Collections.singletonList(getWatchFixture());

        notificationService.createNotification(watches, Notification.EventType.POST_UPDATE);

        verify(notificationRepository).saveAll(anyList());
    }

    @Test
    void findAllByUserId() {
        given(notificationRepository.findAllByUserIdOrderByIdDesc(anyLong())).willReturn(Collections.singletonList(NOTIFICATION));

        List<Notification> notifications = notificationService.getNotifications(NOTIFICATION.getUserId());

        assertThat(notifications).size().isEqualTo(1);
    }

    @Test
    void findAllByUserId2() {
        given(notificationRepository.findAllByUserIdOrderByIdDesc(anyLong())).willReturn(Collections.EMPTY_LIST);

        List<Notification> notifications = notificationService.getNotifications(NOTIFICATION.getUserId());

        assertThat(notifications).size().isEqualTo(0);
    }

    @Test
    void readNotification() {
        given(notificationRepository.findById(anyLong())).willReturn(Optional.of(NOTIFICATION));

        notificationService.readNotification(NOTIFICATION.getId(), NOTIFICATION.getUserId());

        assertThat(NOTIFICATION.isRead()).isTrue();
    }

    @Test
    void readNotificationException() {
        given(notificationRepository.findById(anyLong())).willReturn(Optional.of(NOTIFICATION));

        assertThatThrownBy(() -> notificationService.readNotification(NOTIFICATION.getId(), null))
            .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void deleteNotification() {
        given(notificationRepository.findById(anyLong())).willReturn(Optional.of(NOTIFICATION));

        notificationService.deleteNotification(NOTIFICATION.getId(), NOTIFICATION.getUserId());

        verify(notificationRepository).deleteById(NOTIFICATION.getId());
    }

    @Test
    void deleteNotificationException() {
        given(notificationRepository.findById(anyLong())).willReturn(Optional.of(NOTIFICATION));

        assertThatThrownBy(() -> notificationService.deleteNotification(NOTIFICATION.getId(), null))
            .isInstanceOf(IllegalStateException.class);
    }

    public static Notification getNotificationFixture() {
        Notification notification = Notification.builder()
            .postId(1L)
            .userId(1L)
            .message("새로운 댓글이 생성되었습니다.")
            .build();
        ReflectionTestUtils.setField(notification, "id", 1L);
        ReflectionTestUtils.setField(notification, "createdAt", LocalDateTime.now());
        ReflectionTestUtils.setField(notification, "updatedAt", LocalDateTime.now());
        return notification;
    }
}
