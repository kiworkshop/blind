package org.kiworkshop.blind.notification.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.test.util.ReflectionTestUtils;

public class NotificationTest {

    @ParameterizedTest
    @CsvSource(value = {"postId", "userId", "message"}, delimiter = ':')
    void creationException(String field) {
        assertThatThrownBy(() ->
            Notification.builder()
                .postId(field.equals("postId") ? null : 1L)
                .userId(field.equals("userId") ? null : 1L)
                .message(field.equals("message") ? "" : "message")
                .build()
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void readNotification() {
        Notification notification = getNotificationFixture();
        notification.readNotification();
        assertThat(notification.isRead()).isTrue();
    }

    public static Notification getNotificationFixture() {
        Notification notification =  Notification.builder()
            .postId(1L)
            .userId(1L)
            .message("1번 게시글이 ... 수정되었습니다.")
            .build();
        ReflectionTestUtils.setField(notification, "id", 1L);
        ReflectionTestUtils.setField(notification, "createdAt", LocalDateTime.now());
        ReflectionTestUtils.setField(notification, "updatedAt", LocalDateTime.now());
        return notification;
    }
}
