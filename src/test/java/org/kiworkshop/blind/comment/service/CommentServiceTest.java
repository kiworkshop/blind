package org.kiworkshop.blind.comment.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kiworkshop.blind.comment.controller.dto.CommentRequest;
import org.kiworkshop.blind.comment.controller.dto.CommentResponse;
import org.kiworkshop.blind.comment.repository.CommentRepository;
import org.kiworkshop.blind.post.domain.Post;
import org.kiworkshop.blind.post.service.PostService;
import org.kiworkshop.blind.user.domain.User;
import org.kiworkshop.blind.user.service.UserService;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {
//
//    @Mock
//    private CommentRepository commentRepository;
//
//    @Mock
//    private PostService postService;
//
//    private CommentService commentService;
//
//    @BeforeEach
//    void setUp() {
//        commentService = new CommentService(commentRepository, postService);
//    }
//
//    @Transactional
//    @Test
//    void 다른_유저의_댓글을_수정하려할때() {
//        //given
//        String postContent = "글입니다";
//        String postTitle = "제목입니다";
//        Post post = createPost(postContent, postTitle);
//
//        CommentRequest createRequest = CommentRequest.builder()
//            .content("댓글 내용입니다")
//            .build();
//
//        CommentRequest updateRequest = CommentRequest.builder()
//            .content("수정된 댓글 내용입니다")
//            .build();
//
//        CommentResponse createResponse = commentService.create(post, createRequest);
//
//        //when, then
//        assertThatThrownBy(() -> commentService.update(createResponse.getId(), updateRequest))
//            .isInstanceOf(IllegalArgumentException.class)
//            .hasMessageContaining("사용자 권한이 없습니다.");
//    }
//
//
//    @Transactional
//    @Test
//    void getTopNCommentsTest() {
//        //given
//        // 댓글 9개쯤 달자
//        //given
//        String userEmail = "naruto1@leaf.town";
//        String userPassword = "password";
//        String userName = "naruto1";
//
//        String postContent = "글입니다";
//        String postTitle = "제목입니다";
//
//        CommentRequest commentRequest = CommentRequest.builder()
//            .content("댓글 내용입니다")
//            .build();
//
//        //when
//        createUser(userEmail, userPassword, userName);
//        login(userEmail, userPassword);
//        Post post = createPost(postContent, postTitle);
//        for (int i = 0; i < 9; i++) {
//            CommentResponse commentResponse = commentService.create(post, commentRequest);
//        }
//        List<CommentResponse> comments = commentService.getTopNComments(post);
//
//        //then
//        assertThat(comments.size()).isEqualTo(5);
//    }
//
//    @Transactional
//    @Test
//    void getAfterIndexCommentsTest() {
//        //given
//        // 댓글 9개쯤 달자
//        //given
//        String userEmail = "naruto1@leaf.town";
//        String userPassword = "password";
//        String userName = "naruto1";
//
//        String postContent = "글입니다";
//        String postTitle = "제목입니다";
//
//        CommentRequest commentRequest = CommentRequest.builder()
//            .content("댓글 내용입니다")
//            .build();
//
//        //when
//        createUser(userEmail, userPassword, userName);
//        login(userEmail, userPassword);
//        Post post = createPost(postContent, postTitle);
//        Post post2 = createPost(postContent, postTitle);
//        for (int i = 0; i < 9; i++) {
//            commentService.create(post, commentRequest);
//            commentService.create(post2, commentRequest);
//        }
//
//        //then
//        List<CommentResponse> comments = commentService.getTopNComments(post);
//        int commentSize = commentService.getAfterIdComments(post, comments.get(comments.size() - 1).getId()).size();
//        assertThat(commentSize).isEqualTo(4);
//    }

}
