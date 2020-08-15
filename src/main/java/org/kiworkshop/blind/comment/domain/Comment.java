package org.kiworkshop.blind.comment.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.kiworkshop.blind.domain.AuditorEntity;
import org.kiworkshop.blind.post.domain.Post;
import org.kiworkshop.blind.user.domain.User;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Comment extends AuditorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    @Builder
    public Comment(String content, Post post) {
        this.content = content;
        this.post = post;
    }

    public void update(String content) {
        this.content = content;
    }
}
