package org.kiworkshop.blind.comment.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.kiworkshop.blind.comment.domain.Comment;
import org.kiworkshop.blind.comment.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CommentRepositoryTest {

    @Autowired
    CommentRepository commentRepository;

    @AfterEach
    public void cleanup() {
        commentRepository.deleteAll();
    }

    @Test
    void 댓글_저장하기() {
        //given
        String content = "테스트 본문";

        commentRepository.save(Comment.builder()
                .content(content)
                .build());

        //when
        List<Comment> commentsList = commentRepository.findAll();

        //then
        Comment comment = commentsList.get(0);
        assertThat(comment.getContent()).isEqualTo(content);
    }

    @Test
    void jpa_auditing_작동확인() {
        //given
        LocalDateTime now = LocalDateTime.of(2019, 6, 4, 0, 0, 0);
        commentRepository.save(Comment.builder()
                .content("content")
                .build());

        //when
        List<Comment> commentsList = commentRepository.findAll();

        //then
        Comment comment = commentsList.get(0);

        System.out.println(">>>>>>>>> createDate=" + comment.getCreatedDate() + ", modifiedDate=" + comment.getLastModifiedDate());

        assertThat(comment.getCreatedDate()).isAfter(now);
        assertThat(comment.getLastModifiedDate()).isAfter(now);
    }
}
