package org.kiworkshop.blind.user.domain;

import org.springframework.test.util.ReflectionTestUtils;

public class UserTest {
    public static User getAuthorFixture() {
        User user = User.builder()
            .email("authorEmail")
            .password("authroPassword")
            .name("author")
            .build();
        ReflectionTestUtils.setField(user, "id", 1L);
        return user;
    }

    public static User getWatcherFixture() {
        User user = User.builder()
            .email("watcherEmail")
            .password("watcherPassword")
            .name("watcher")
            .build();
        ReflectionTestUtils.setField(user, "id", 2L);
        return user;
    }
}
