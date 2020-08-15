package org.kiworkshop.blind.post.domain;

import static org.kiworkshop.blind.user.domain.UserTest.*;

import org.kiworkshop.blind.user.domain.User;
import org.springframework.test.util.ReflectionTestUtils;

public class PostTest {
    public static Post getPostFixture() {
        User user = getAuthorFixture();
        Post post = Post.builder()
            .title("title")
            .content("content")
            .build();
        ReflectionTestUtils.setField(post, "id", 1L);
        return post;
    }
}
