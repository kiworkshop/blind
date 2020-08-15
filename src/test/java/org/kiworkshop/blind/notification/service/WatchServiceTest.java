package org.kiworkshop.blind.notification.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.kiworkshop.blind.post.domain.PostTest.*;
import static org.kiworkshop.blind.user.domain.UserTest.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kiworkshop.blind.notification.model.Watch;
import org.kiworkshop.blind.notification.model.WatchRepository;
import org.kiworkshop.blind.post.controller.dto.response.PostSummaryResponseDto;
import org.kiworkshop.blind.post.domain.Post;
import org.kiworkshop.blind.post.repository.PostRepository;
import org.kiworkshop.blind.user.controller.dto.UserSummaryResponseDto;
import org.kiworkshop.blind.user.domain.User;
import org.kiworkshop.blind.user.repository.UserRepository;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class WatchServiceTest {
    private static final Post POST = getPostFixture();
    private static final User WATCHER = getWatcherFixture();
    private static final Watch WATCH = getWatchFixture();
    private WatchService watchService;
    @Mock
    private PostRepository postRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private WatchRepository watchRepository;

    @BeforeEach
    void setUp() {
        watchService = new WatchService(postRepository, userRepository, watchRepository);
    }

    @Test
    void startWatch() {
        given(postRepository.findById(anyLong())).willReturn(Optional.of(POST));
        given(userRepository.findById(anyLong())).willReturn(Optional.of(WATCHER));
        given(watchRepository.existsByPostAndUser(POST, WATCHER)).willReturn(false);
        given(watchRepository.save(any(Watch.class))).willReturn(WATCH);

        watchService.startWatch(POST.getId(), WATCHER.getId());

        verify(watchRepository).save(any(Watch.class));
    }

    @Test
    void startWatchException() {
        given(postRepository.findById(anyLong())).willReturn(Optional.of(POST));
        given(userRepository.findById(anyLong())).willReturn(Optional.of(WATCHER));
        given(watchRepository.existsByPostAndUser(POST, WATCHER)).willReturn(true);

        assertThatThrownBy(() -> watchService.startWatch(POST.getId(), WATCHER.getId()))
            .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void stopWatch() {
        given(postRepository.findById(anyLong())).willReturn(Optional.of(POST));
        given(userRepository.findById(anyLong())).willReturn(Optional.of(WATCHER));
        given(watchRepository.findByPostAndUser(POST, WATCHER)).willReturn(Optional.of(WATCH));

        watchService.stopWatch(POST.getId(), WATCHER.getId());

        verify(watchRepository).delete(WATCH);
    }

    @Test
    void stopWatchException() {
        given(postRepository.findById(anyLong())).willReturn(Optional.of(POST));
        given(userRepository.findById(anyLong())).willReturn(Optional.of(WATCHER));

        assertThatThrownBy(() -> watchService.stopWatch(POST.getId(), WATCHER.getId()))
            .isInstanceOf(RuntimeException.class);
    }

    @Test
    void isWatching() {
        given(postRepository.findById(anyLong())).willReturn(Optional.of(POST));
        given(userRepository.findById(anyLong())).willReturn(Optional.of(WATCHER));
        given(watchRepository.existsByPostAndUser(POST, WATCHER)).willReturn(true);

        Boolean isWatching = watchService.isWatching(POST.getId(), WATCHER.getId());

        assertThat(isWatching).isTrue();
    }

    @Test
    void isNotWatching() {
        given(postRepository.findById(anyLong())).willReturn(Optional.of(POST));
        given(userRepository.findById(anyLong())).willReturn(Optional.of(WATCHER));
        given(watchRepository.existsByPostAndUser(POST, WATCHER)).willReturn(false);

        Boolean isWatching = watchService.isWatching(POST.getId(), WATCHER.getId());

        assertThat(isWatching).isFalse();
    }

    @Test
    void getWatchList() {
        given(userRepository.findById(anyLong())).willReturn(Optional.of(WATCHER));
        List<Watch> watches = Collections.singletonList(WATCH);
        given(watchRepository.findAllByUser(WATCHER)).willReturn(watches);

        List<PostSummaryResponseDto> posts = watchService.getWatchList(WATCHER.getId());

        assertThat(posts).size().isEqualTo(1);
        assertThat(posts.get(0).getId()).isEqualTo(POST.getId());
    }

    @Test
    void getWatcherList() {
        given(postRepository.findById(anyLong())).willReturn(Optional.of(POST));
        List<Watch> watches = Collections.singletonList(WATCH);
        given(watchRepository.findAllByPost(POST)).willReturn(watches);

        List<UserSummaryResponseDto> users = watchService.getWatchers(POST.getId());

        assertThat(users).size().isEqualTo(1);
        assertThat(users.get(0).getId()).isEqualTo(WATCHER.getId());
    }

    public static Watch getWatchFixture() {
        Watch watch = Watch.builder()
            .post(POST)
            .user(WATCHER)
            .build();
        ReflectionTestUtils.setField(watch, "id", 1L);
        ReflectionTestUtils.setField(watch, "createdAt", LocalDateTime.now());
        ReflectionTestUtils.setField(watch, "updatedAt", LocalDateTime.now());
        return watch;
    }
}
