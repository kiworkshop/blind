package org.kiworkshop.blind.notification.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.kiworkshop.blind.notification.exception.WatchException;
import org.kiworkshop.blind.notification.model.Watch;
import org.kiworkshop.blind.notification.model.WatchRepository;
import org.kiworkshop.blind.post.controller.dto.response.PostSummaryResponsDto;
import org.kiworkshop.blind.post.domain.Post;
import org.kiworkshop.blind.post.repository.PostRepository;
import org.kiworkshop.blind.user.controller.dto.UserSummaryResponseDto;
import org.kiworkshop.blind.user.domain.User;
import org.kiworkshop.blind.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class WatchService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final WatchRepository watchRepository;

    public Long startWatch(Long postId, Long userId) {
        Post post = findPostBy(postId);
        User user = findUserBy(userId);
        if (watchRepository.existsByPostAndUser(post, user)) {
            throw new IllegalStateException("이미 받아보기 상태입니다.");
        }
        Watch watch = Watch.builder()
            .post(post)
            .user(user)
            .build();
        return watchRepository.save(watch).getId();
    }

    public void stopWatch(Long postId, Long userId) {
        Post post = findPostBy(postId);
        User user = findUserBy(userId);
        Watch watch = findWatchByPostIdAndUserId(post, user);
        watchRepository.delete(watch);
    }

    public Boolean isWatching(Long postId, Long userId) {
        Post post = findPostBy(postId);
        User user = findUserBy(userId);
        return watchRepository.existsByPostAndUser(post, user);
    }

    public List<PostSummaryResponsDto> getWatchList(Long userId) {
        User user = findUserBy(userId);
        List<Watch> watches = watchRepository.findAllByUser(user);
        return watches.stream()
            .map(Watch::getPost)
            .map(PostSummaryResponsDto::from)
            .collect(Collectors.toList());
    }

    public List<UserSummaryResponseDto> getWatchers(Long postId) {
        Post post = findPostBy(postId);
        List<Watch> watches = watchRepository.findAllByPost(post);
        return watches.stream()
            .map(Watch::getUser)
            .map(UserSummaryResponseDto::from)
            .collect(Collectors.toList());
    }

    private Post findPostBy(Long postId) {
        return postRepository.findById(postId)
            .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 post입니다."));
    }

    private User findUserBy(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 user입니다."));
    }

    private Watch findWatchByPostIdAndUserId(Post post, User user) {
        return watchRepository.findByPostAndUser(post, user)
            .orElseThrow(() -> new WatchException("현재 받아보기 상태가 아닙니다."));
    }
}
