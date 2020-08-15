package org.kiworkshop.blind.post.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.kiworkshop.blind.like.LikeAction;
import org.kiworkshop.blind.user.domain.User;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
@NoArgsConstructor
@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private User author;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "post")
    private List<LikeAction> likes = new ArrayList<>();

    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime lastUpdatedAt;

    @Builder
    private Post(String title, String content, User author) {
        Assert.hasLength(title, "title should not be empty");
        Assert.hasLength(content, "content should not be empty");
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public void update(Post postToUpdate) {
        this.title = postToUpdate.title;
        this.content = postToUpdate.content;
    }

    public void addLike(User user) {
        likes.add(new LikeAction(this, user));
    }

    public int getLikeCount() {
        return likes.size();
    }

    public String getTitleSummary() {
        if (title.length() < 10) {
            return title;
        }
        return title.substring(0, 10);
    }
}

