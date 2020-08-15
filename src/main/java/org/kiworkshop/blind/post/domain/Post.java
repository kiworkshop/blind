package org.kiworkshop.blind.post.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.kiworkshop.blind.comment.domain.Comment;
import org.kiworkshop.blind.domain.AuditorEntity;
import org.kiworkshop.blind.like.LikeAction;
import org.kiworkshop.blind.user.domain.User;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
@NoArgsConstructor
@Entity
public class Post extends AuditorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "post")
    private List<LikeAction> likes = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

    @Builder
    private Post(String title, String content) {
        Assert.hasLength(title, "title should not be empty");
        Assert.hasLength(content, "content should not be empty");
        this.title = title;
        this.content = content;
    }

    public void update(Post postToUpdate) {
        this.title = postToUpdate.getTitle();
        this.content = postToUpdate.getContent();
    }

    public void addLike(User user) {
        likes.add(new LikeAction(this, user));
    }
}

