package org.kiworkshop.blind.notification.model;

import static org.assertj.core.api.Assertions.*;
import static org.kiworkshop.blind.post.domain.PostTest.*;
import static org.kiworkshop.blind.user.domain.UserTest.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.kiworkshop.blind.post.domain.Post;
import org.kiworkshop.blind.user.domain.User;

public class WatchTest {
    private static Post post = getPostFixture();
    private static User user = getWatcherFixture();

    @ParameterizedTest
    @ValueSource(strings = {"POST_UPDATE", "COMMENT_CREATION"})
    void createNotification(String eventType) {
        Watch watch = Watch.builder()
            .post(post)
            .user(user)
            .build();
        Notification.EventType event = Notification.EventType.valueOf(eventType);
        Notification notification = watch.createNotification(event);
        assertThat(notification.getPostId()).isNotNull();
        assertThat(notification.getUserId()).isNotNull();
        assertThat(notification.getMessage()).isNotEmpty();
    }

    @ParameterizedTest
    @MethodSource("getCreationExceptionValue")
    void creationException(String field, Object object) {
        assertThatThrownBy(() ->
            Watch.builder()
                .post(field.equals("post") ? (Post)object : null)
                .user(field.equals("user") ? (User)object : null)
                .build()
        ).isInstanceOf(IllegalArgumentException.class);
    }

    private static Stream<Arguments> getCreationExceptionValue() {
        return Stream.of(
            Arguments.of("post", post),
            Arguments.of("user", user)
        );
    }
}
