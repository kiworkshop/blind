package org.kiworkshop.blind.comment.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class CommentResponse {

    private Long id;
    private String content;
    private List<String> nameTags;
    private String authorName;
    private Long postId;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdatedAt;

    @Builder
    public CommentResponse(Long id, String content, List<String> nameTags, String authorName, Long postId, LocalDateTime createdAt, LocalDateTime lastUpdatedAt) {
        this.id = id;
        this.content = content;
        this.nameTags = nameTags;
        this.authorName = authorName;
        this.postId = postId;
        this.createdAt = createdAt;
        this.lastUpdatedAt = lastUpdatedAt;
    }
}
