package org.kiworkshop.blind.notification.model;

import java.util.List;
import java.util.Optional;

import org.kiworkshop.blind.post.domain.Post;
import org.kiworkshop.blind.user.domain.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface WatchRepository extends JpaRepository<Watch, Long> {
    boolean existsByPostAndUser(Post post, User user);

    Optional<Watch> findByPostAndUser(Post post, User user);

    @EntityGraph(attributePaths = {"post", "post.author", "post.likes"})
    @Query("SELECT w FROM Watch w WHERE w.user = :user")
    List<Watch> findAllByUser(User user);

    @EntityGraph(attributePaths = {"user"})
    @Query("SELECT w FROM Watch w WHERE w.post = :post")
    List<Watch> findAllByPost(Post post);
}
