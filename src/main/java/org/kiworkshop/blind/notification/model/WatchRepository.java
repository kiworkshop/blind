package org.kiworkshop.blind.notification.model;

import java.util.List;
import java.util.Optional;

import org.kiworkshop.blind.post.domain.Post;
import org.kiworkshop.blind.user.domain.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WatchRepository extends JpaRepository<Watch, Long> {
    boolean existsByPostAndUser(Post post, User user);

    Optional<Watch> findByPostAndUser(Post post, User user);

    @EntityGraph(attributePaths = {"post", "post.author", "post.likes"})
    List<Watch> findAllByUser(User user);

    @EntityGraph(attributePaths = {"user"})
    List<Watch> findAllByPost(Post post);
}
