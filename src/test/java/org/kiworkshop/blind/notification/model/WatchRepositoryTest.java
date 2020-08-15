package org.kiworkshop.blind.notification.model;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kiworkshop.blind.post.domain.Post;
import org.kiworkshop.blind.post.repository.PostRepository;
import org.kiworkshop.blind.user.domain.User;
import org.kiworkshop.blind.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class WatchRepositoryTest {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WatchRepository watchRepository;
    private User author;
    private User watcher;
    private Post post;

    @BeforeEach
    void setUp() {
        author = User.builder()
            .email("email")
            .password("password")
            .name("name")
            .build();
        watcher = User.builder()
            .email("email")
            .password("password")
            .name("name")
            .build();
        userRepository.save(author);
        userRepository.save(watcher);
        post = Post.builder()
            .title("title")
            .content("content")
            .author(author)
            .build();
        postRepository.save(post);
    }

    @Test
    void existsWatch() {
        Watch watch = Watch.builder()
            .post(post)
            .user(watcher)
            .build();
        watchRepository.save(watch);
        assertThat(watchRepository.existsByPostAndUser(post, watcher)).isTrue();
    }

    @Test
    void findByPostAndUser() {
        Watch watch = Watch.builder()
            .post(post)
            .user(watcher)
            .build();
        watchRepository.save(watch);
        Watch expected = watchRepository.findByPostAndUser(post, watcher).get();
        assertThat(expected).isEqualTo(watch);
    }

    @Test
    void findByUser() {
        Watch watch = Watch.builder()
            .post(post)
            .user(watcher)
            .build();
        watchRepository.save(watch);
        List<Watch> watches = watchRepository.findAllByUser(watcher);
        assertThat(watches).size().isEqualTo(1);
    }

    @Test
    void findByPost() {
        Watch watch = Watch.builder()
            .post(post)
            .user(watcher)
            .build();
        watchRepository.save(watch);
        List<Watch> watches = watchRepository.findAllByPost(post);
        assertThat(watches).size().isEqualTo(1);
    }
}
