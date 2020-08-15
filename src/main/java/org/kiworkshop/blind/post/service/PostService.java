package org.kiworkshop.blind.post.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.kiworkshop.blind.like.LikeAction;
import org.kiworkshop.blind.like.LikeResponse;
import org.kiworkshop.blind.post.controller.dto.request.PostRequestDto;
import org.kiworkshop.blind.post.controller.dto.response.PostResponseDto;
import org.kiworkshop.blind.post.domain.Post;
import org.kiworkshop.blind.post.repository.PostRepository;
import org.kiworkshop.blind.user.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public List<PostResponseDto> readAll() {
        return postRepository.findAll().stream()
            .map(this::getPostResponseDto)
            .collect(Collectors.toList());
    }

    public Long createPost(HttpSession session, PostRequestDto postRequestDto) {
        User author = (User)session.getAttribute("LOGIN_USER");
        Post post = postRequestDto.toEntity(author);
        return postRepository.save(post).getId();
    }

    @Transactional(readOnly = true)
    public PostResponseDto readPost(Long id) {
        Post post = findById(id);
        return getPostResponseDto(post);
    }

    private PostResponseDto getPostResponseDto(Post post) {
        LikeResponse likeResponse = createLikeResponse(post.getLikes());
        return PostResponseDto.builder()
            .id(post.getId())
            .title(post.getTitle())
            .content(post.getContent())
            .authorName(post.getAuthor().getName())
            .likeResponse(likeResponse)
            .createdAt(post.getCreatedAt())
            .lastUpdatedAt(post.getLastUpdatedAt())
            .build();
    }

    private LikeResponse createLikeResponse(List<LikeAction> likes) {
        List<String> likeUserNames = likes.stream().map(like -> like.getUser().getName()).collect(Collectors.toList());
        return new LikeResponse(likeUserNames);
    }

    public void updatePost(Long id, PostRequestDto postRequestDto) {
        Post post = findById(id);
        Post postToUpdate = postRequestDto.toEntity(null);
        post.update(postToUpdate);
        postRepository.save(post);
    }

    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    private Post findById(Long id) {
        Post post = postRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 post입니다."));
        return post;
    }

    @Transactional
    public void likePost(HttpSession httpSession, Long id) {
        User loginUser = (User)httpSession.getAttribute("LOGIN_USER");
        Post post = findById(id);
        post.addLike(loginUser);
    }
}
