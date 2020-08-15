package org.kiworkshop.blind.comment.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.kiworkshop.blind.comment.controller.dto.CommentRequest;
import org.kiworkshop.blind.comment.controller.dto.CommentResponse;
import org.kiworkshop.blind.comment.domain.Comment;
import org.kiworkshop.blind.post.controller.PostRequestDto;
import org.kiworkshop.blind.post.domain.Post;
import org.kiworkshop.blind.post.service.PostService;
import org.kiworkshop.blind.user.controller.LoginRequest;
import org.kiworkshop.blind.user.controller.dto.UserRequestDto;
import org.kiworkshop.blind.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @Autowired
    private HttpSession httpSession;

    @AfterEach
    void tearDown() {
        httpSession.invalidate();
    }

    @Transactional
    @Test
    void 로그인한_유저가_댓글달때() {
        //given
        String userEmail = "naruto1@leaf.town";
        String userPassword = "password";
        String userName = "naruto1";

        String postContent = "글입니다";
        String postTitle = "제목입니다";

        CommentRequest commentRequest = CommentRequest.builder()
                .content("댓글 내용입니다")
                .build();

        //when
        createUser(userEmail, userPassword, userName);
        login(userEmail, userPassword);
        Post post = createPost(postContent, postTitle);
        CommentResponse commentResponse = commentService.create(httpSession, post, commentRequest);

        //then
        assertThat(commentResponse.getPostId()).isEqualTo(post.getId());
        assertThat(commentResponse.getAuthorName()).isEqualTo(userName);
        assertThat(commentResponse.getContent()).isEqualTo("댓글 내용입니다");
    }

    @Transactional
    @Test
    void 로그인하지_않은_유저가_댓글달때() {
        //given
        String userEmail = "naruto1@leaf.town";
        String userPassword = "password";
        String userName = "naruto1";

        String postContent = "글입니다";
        String postTitle = "제목입니다";

        CommentRequest commentRequest = CommentRequest.builder()
                .content("댓글 내용입니다")
                .build();

        //when
        createUser(userEmail, userPassword, userName);
        login(userEmail, userPassword);
        Post post = createPost(postContent, postTitle);
        httpSession.invalidate();

        //then
        assertThatThrownBy(() -> commentService.create(httpSession, post, commentRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("로그인하지 않은 사용자입니다.");
    }

    @Transactional
    @Test
    void 로그인한_유저가_댓글을_수정할때() {
        //given
        String userEmail = "naruto1@leaf.town";
        String userPassword = "password";
        String userName = "naruto1";

        String postContent = "글입니다";
        String postTitle = "제목입니다";

        CommentRequest createRequest = CommentRequest.builder()
                .content("댓글 내용입니다")
                .build();

        CommentRequest updateRequest = CommentRequest.builder()
                .content("수정된 댓글 내용입니다")
                .build();

        //when
        createUser(userEmail, userPassword, userName);
        login(userEmail, userPassword);
        Post post = createPost(postContent, postTitle);
        CommentResponse createResponse = commentService.create(httpSession, post, createRequest);
        CommentResponse updateResponse = commentService.update(httpSession, createResponse.getId(), updateRequest);

        //then
        assertThat(updateResponse.getPostId()).isEqualTo(post.getId());
        assertThat(updateResponse.getAuthorName()).isEqualTo(userName);
        assertThat(updateResponse.getContent()).isEqualTo("수정된 댓글 내용입니다");
    }

    @Transactional
    @Test
    void 로그인하지_않은_유저가_댓글을_수정할때() {
        //given
        String userEmail = "naruto1@leaf.town";
        String userPassword = "password";
        String userName = "naruto1";

        String postContent = "글입니다";
        String postTitle = "제목입니다";

        CommentRequest createRequest = CommentRequest.builder()
                .content("댓글 내용입니다")
                .build();

        CommentRequest updateRequest = CommentRequest.builder()
                .content("수정된 댓글 내용입니다")
                .build();

        //when
        createUser(userEmail, userPassword, userName);
        login(userEmail, userPassword);
        Post post = createPost(postContent, postTitle);
        CommentResponse createResponse = commentService.create(httpSession, post, createRequest);
        httpSession.invalidate();

        //then
        assertThatThrownBy(() -> commentService.update(httpSession, createResponse.getId(), updateRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("로그인하지 않은 사용자입니다.");
    }

    @Transactional
    @Test
    void 다른_유저의_댓글을_수정하려할때() {
        //given
        String userEmail1 = "naruto1@leaf.town";
        String userPassword1 = "password";
        String userName1 = "naruto1";

        String userEmail2 = "naruto2@leaf.town";
        String userPassword2 = "password";
        String userName2 = "naruto2";

        String postContent = "글입니다";
        String postTitle = "제목입니다";

        CommentRequest createRequest = CommentRequest.builder()
                .content("댓글 내용입니다")
                .build();

        CommentRequest updateRequest = CommentRequest.builder()
                .content("수정된 댓글 내용입니다")
                .build();

        //when
        createUser(userEmail1, userPassword1, userName1);
        login(userEmail1, userPassword1);
        Post post = createPost(postContent, postTitle);
        CommentResponse createResponse = commentService.create(httpSession, post, createRequest);
        userService.logout(httpSession);

        createUser(userEmail2, userPassword2, userName2);
        login(userEmail2, userPassword2);

        //then
        assertThatThrownBy(() -> commentService.update(httpSession, createResponse.getId(), updateRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("사용자 권한이 없습니다.");
    }

    @Transactional
    @Test
    void getTopNCommentsTest() {
        //given
        // 댓글 9개쯤 달자
        //given
        String userEmail = "naruto1@leaf.town";
        String userPassword = "password";
        String userName = "naruto1";

        String postContent = "글입니다";
        String postTitle = "제목입니다";

        CommentRequest commentRequest = CommentRequest.builder()
                .content("댓글 내용입니다")
                .build();

        //when
        createUser(userEmail, userPassword, userName);
        login(userEmail, userPassword);
        Post post = createPost(postContent, postTitle);
        for (int i = 0; i < 9; i++) {
            CommentResponse commentResponse = commentService.create(httpSession, post, commentRequest);
        }
        List<CommentResponse> comments = commentService.getTopNComments(post);

        //then
        assertThat(comments.size()).isEqualTo(5);
    }

    @Transactional
    @Test
    void getAfterIndexCommentsTest() {
        //given
        // 댓글 9개쯤 달자
        //given
        String userEmail = "naruto1@leaf.town";
        String userPassword = "password";
        String userName = "naruto1";

        String postContent = "글입니다";
        String postTitle = "제목입니다";

        CommentRequest commentRequest = CommentRequest.builder()
                .content("댓글 내용입니다")
                .build();

        //when
        createUser(userEmail, userPassword, userName);
        login(userEmail, userPassword);
        Post post = createPost(postContent, postTitle);
        Post post2 = createPost(postContent, postTitle);
        for (int i = 0; i < 9; i++) {
            commentService.create(httpSession, post, commentRequest);
            commentService.create(httpSession, post2, commentRequest);
        }

        //then
        List<CommentResponse> comments = commentService.getTopNComments(post);
        int commentSize = commentService.getAfterIdComments(post, comments.get(comments.size() - 1).getId()).size();
        assertThat(commentSize).isEqualTo(4);
    }

    void createUser(String userEmail, String userPassword, String userName) {
        UserRequestDto userRequestDto = UserRequestDto.builder()
                .email(userEmail)
                .password(userPassword)
                .name(userName)
                .build();

        userService.createUser(userRequestDto);
    }

    void login(String userEmail, String userPassword) {
        LoginRequest loginRequest = LoginRequest.builder()
                .email(userEmail)
                .password(userPassword)
                .build();

        userService.login(httpSession, loginRequest);
    }

    Post createPost(String postContent, String postTitle) {
        PostRequestDto postRequestDto = PostRequestDto.builder()
                .content(postContent)
                .title(postTitle)
                .build();

        Long postId = postService.createPost(httpSession, postRequestDto);
        Post post = postService.findById(postId);
        return post;
    }
}