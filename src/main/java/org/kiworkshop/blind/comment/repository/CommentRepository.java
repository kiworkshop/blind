package org.kiworkshop.blind.comment.repository;

import org.kiworkshop.blind.comment.domain.Comment;
import org.kiworkshop.blind.post.domain.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByPostOrderById(Post postId, Pageable pageable);

    List<Comment> findAllByPostAndIdGreaterThan(Post post, Long id);
}
