package org.kiworkshop.blind.comment.service;

import lombok.RequiredArgsConstructor;
import org.kiworkshop.blind.comment.controller.dto.CommentRequest;
import org.kiworkshop.blind.comment.controller.dto.CommentResponse;
import org.kiworkshop.blind.comment.domain.Comment;
import org.kiworkshop.blind.comment.repository.CommentRepository;
import org.kiworkshop.blind.comment.util.NameTagExtractor;
import org.kiworkshop.blind.post.domain.Post;
import org.kiworkshop.blind.user.domain.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommentService {

    private static final int COMMENT_SIZE = 5;

    private final CommentRepository commentRepository;

    public CommentResponse create(HttpSession session, Post post, CommentRequest request) {
        validateLoginUser(session);

        Comment comment = Comment.builder()
                .content(request.getContent())
                .post(post)
                .build();

        Comment saved = commentRepository.save(comment);
        return createCommentResponse(saved);
    }

    public List<CommentResponse> getAll(Post post) {
        return post.getComments().stream()
                .map(this::createCommentResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public CommentResponse update(HttpSession httpSession, Long id, CommentRequest request) {
        Comment comment = findCommentById(id);
        validateLoginUser(httpSession);
        validateCorrectUser(httpSession, comment);

        comment.update(request.getContent());

        return createCommentResponse(comment);
    }

    public void delete(HttpSession httpSession, Long id) {
        Comment comment = findCommentById(id);
        validateLoginUser(httpSession);
        validateCorrectUser(httpSession, comment);

        commentRepository.delete(comment);
    }

    private Comment findCommentById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 id의 comment가 존재하지 않습니다. id=" + id));
    }

    private void validateLoginUser(HttpSession httpSession) {
        User loginUser = (User) httpSession.getAttribute("LOGIN_USER");
        if (loginUser == null) throw new IllegalArgumentException("로그인하지 않은 사용자입니다.");
    }

    private void validateCorrectUser(HttpSession httpSession, Comment comment) {
        User loginUser = (User) httpSession.getAttribute("LOGIN_USER");
        User author = comment.getCreatedBy();
        if (!(author.getId().equals(loginUser.getId()))) throw new IllegalArgumentException("사용자 권한이 없습니다.");
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

    public List<CommentResponse> getTopNComments(Post post) {
        List<Comment> comments = commentRepository.findAllByPostOrderById(post, PageRequest.of(0, COMMENT_SIZE));
        return comments.stream()
                .map(this::createCommentResponse)
                .collect(Collectors.toList());
    }

    public List<CommentResponse> getAfterIdComments(Post post, Long id) {
        List<Comment> comments = commentRepository.findAllByPostAndIdGreaterThan(post, id);
        return comments.stream()
                .map(this::createCommentResponse)
                .collect(Collectors.toList());
    }
}
