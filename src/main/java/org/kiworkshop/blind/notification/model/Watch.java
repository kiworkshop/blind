package org.kiworkshop.blind.notification.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.kiworkshop.blind.post.domain.Post;
import org.kiworkshop.blind.user.domain.User;
import org.springframework.util.Assert;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Watch {
    private static final String NOTIFICATION_MESAGE_FORMAT = "%d번 게시물 '%s' 게시글%s";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    private Watch(Post post, User user) {
        Assert.notNull(post, "post should not be null");
        Assert.notNull(user, "user should not be null");
        this.post = post;
        this.user = user;
    }

    public Notification createNotification(Notification.EventType event) {
        Long postId = post.getId();
        String mesage = generateNotificationMessage(event, postId);
        return Notification.builder()
            .postId(postId)
            .userId(user.getId())
            .message(mesage)
            .build();
    }

    public boolean isOwner(Long userId) {
        return user.getId().equals(userId);
    }

    private String generateNotificationMessage(Notification.EventType event, Long postId) {
        String tailMessage;
        switch (event) {
            case POST_UPDATE:
                tailMessage = "이 수정되었습니다.";
                break;
            case COMMENT_CREATION:
                tailMessage = "에 새로운 댓글이 달렸습니다.";
                break;
            default:
                tailMessage = "에 새로운 이벤트가 발생했습니다";
        }
        return String.format(NOTIFICATION_MESAGE_FORMAT, postId, post.getTitleSummary(), tailMessage);
    }
}
