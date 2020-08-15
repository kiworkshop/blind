package org.kiworkshop.blind.post.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kiworkshop.blind.comment.controller.dto.CommentResponse;
import org.kiworkshop.blind.comment.domain.Comment;
import org.kiworkshop.blind.comment.util.NameTagExtractor;
import org.kiworkshop.blind.like.LikeAction;
import org.kiworkshop.blind.like.LikeResponse;
import org.kiworkshop.blind.post.controller.PostRequestDto;
import org.kiworkshop.blind.post.controller.PostResponseDto;
import org.kiworkshop.blind.post.domain.Post;
import org.kiworkshop.blind.post.repository.PostRepository;
import org.kiworkshop.blind.user.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public Long createPost(HttpSession session, PostRequestDto postRequestDto) {
        Post post = postRequestDto.toEntity();
        return postRepository.save(post).getId();
    }

    public List<PostResponseDto> readAll() {
        return postRepository.findAll().stream()
            .map(this::getPostResponseDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PostResponseDto readPost(Long id) {
        Post post = findById(id);
        return getPostResponseDto(post);
    }

    public Post findById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 post입니다."));
        return post;
    }

    private PostResponseDto getPostResponseDto(Post post) {
        LikeResponse likeResponse = createLikeResponse(post.getLikes());
        List<CommentResponse> commentResponseList = createCommentResponseList(post.getComments());
        return PostResponseDto.builder()
            .id(post.getId())
            .title(post.getTitle())
            .content(post.getContent())
            .authorName(post.getCreatedBy().getName())
            .commentResponseList(commentResponseList)
            .likeResponse(likeResponse)
            .createdAt(post.getCreatedDate())
            .lastUpdatedAt(post.getLastModifiedDate())
            .build();
    }

    private List<CommentResponse> createCommentResponseList(List<Comment> comments) {
        return comments.stream()
                .map(this::createCommentResponse)
                .collect(Collectors.toList());
    }

    private CommentResponse createCommentResponse(Comment comment) {
        List<String> nameTags = NameTagExtractor.extractNameTags(comment.getContent());

        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .nameTags(nameTags)
                .authorName(comment.getCreatedBy().getName())
                .postId(comment.getPost().getId())
                .createdAt(comment.getCreatedDate())
                .lastUpdatedAt(comment.getLastModifiedDate())
                .build();
    }

    private LikeResponse createLikeResponse(List<LikeAction> likes) {
        List<String> likeUserNames = likes.stream().map(like -> like.getUser().getName()).collect(Collectors.toList());
        return new LikeResponse(likeUserNames);
    }

    @Transactional
    public void updatePost(Long id, PostRequestDto postRequestDto) {
        Post post = findById(id);
        Post postToUpdate = postRequestDto.toEntity();
        post.update(postToUpdate);
        postRepository.save(post);
    }

    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    @Transactional
    public void likePost(HttpSession httpSession, Long id) {
        User loginUser = (User) httpSession.getAttribute("LOGIN_USER");
        Post post = findById(id);
        post.addLike(loginUser);
    }
}
