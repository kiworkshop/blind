package org.kiworkshop.blind.comment.service;

import lombok.RequiredArgsConstructor;
import org.kiworkshop.blind.comment.controller.dto.CommentRequest;
import org.kiworkshop.blind.comment.controller.dto.CommentResponse;
import org.kiworkshop.blind.comment.domain.Comment;
import org.kiworkshop.blind.comment.repository.CommentRepository;
import org.kiworkshop.blind.post.domain.Post;
import org.kiworkshop.blind.post.service.PostService;
import org.kiworkshop.blind.user.domain.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private static final int COMMENT_SIZE = 5;

    private final CommentRepository commentRepository;
    private final PostService postService;

    @Transactional
    public CommentResponse addComment(Long postId, CommentRequest request) {
        Post post = postService.findById(postId);
        Comment comment = post.addComment(request.getContent());
        return CommentModelMapper.createCommentResponse(comment);
    }

    public List<CommentResponse> getAllComments(Long postId) {
        Post post = postService.findById(postId);
        return post.getComments().stream()
            .map(CommentModelMapper::createCommentResponse)
            .collect(Collectors.toList());
    }

    @Transactional
    public CommentResponse update(Long id, CommentRequest request, UserDetails userDetails) {
        Comment comment = findCommentById(id);
        validateOwner(comment, userDetails);
        comment.update(request.getContent());
        return CommentModelMapper.createCommentResponse(comment);
    }

    public void delete(Long id, UserDetails userDetails) {
        Comment comment = findCommentById(id);
        validateOwner(comment, userDetails);
        commentRepository.delete(comment);
    }

    private Comment findCommentById(Long id) {
        return commentRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당 id의 comment가 존재하지 않습니다. id=" + id));
    }

    private void validateOwner(Comment comment, UserDetails userDetails) {
        User author = comment.getCreatedBy();
        if (!author.matchEmail(userDetails.getUsername())) {
            throw new IllegalArgumentException("사용자 권한이 없습니다.");
        }
    }

    public List<CommentResponse> getTopNComments(Post post) {
        List<Comment> comments = commentRepository.findAllByPostOrderById(post, PageRequest.of(0, COMMENT_SIZE));
        return comments.stream()
            .map(CommentModelMapper::createCommentResponse)
            .collect(Collectors.toList());
    }

    public List<CommentResponse> getAfterIdComments(Post post, Long id) {
        List<Comment> comments = commentRepository.findAllByPostAndIdGreaterThan(post, id);
        return comments.stream()
            .map(CommentModelMapper::createCommentResponse)
            .collect(Collectors.toList());
    }

}
