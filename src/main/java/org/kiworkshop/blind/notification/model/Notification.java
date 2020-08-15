package org.kiworkshop.blind.notification.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.util.Assert;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private Long postId;
    private Long userId;
    private String message;
    private boolean read;

    @Builder
    private Notification(Long postId, Long userId, String message) {
        Assert.notNull(postId, "postId should not be null");
        Assert.notNull(userId, "postId should not be null");
        Assert.hasLength(message, "message should not be empty");

        this.postId = postId;
        this.userId = userId;
        this.message = message;
        this.read = false;
    }

    public void readNotification() {
        read = true;
    }

    public boolean isOwner(Long userId) {
        return this.userId.equals(userId);
    }

    public enum EventType {
        POST_UPDATE,
        COMMENT_CREATION;
    }
}
