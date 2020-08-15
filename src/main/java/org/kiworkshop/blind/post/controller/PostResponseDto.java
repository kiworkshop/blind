package org.kiworkshop.blind.post.controller;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.kiworkshop.blind.comment.controller.dto.CommentResponse;
import org.kiworkshop.blind.like.LikeResponse;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class PostResponseDto {

    private Long id;
    private String title;
    private String content;
    private String authorName;
    private LikeResponse likeResponse;
    private List<CommentResponse> commentResponseList;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdatedAt;

    @Builder
    public PostResponseDto(Long id, String title, String content, String authorName, LikeResponse likeResponse, List<CommentResponse> commentResponseList, LocalDateTime createdAt, LocalDateTime lastUpdatedAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.authorName = authorName;
        this.likeResponse = likeResponse;
        this.commentResponseList = commentResponseList;
        this.createdAt = createdAt;
        this.lastUpdatedAt = lastUpdatedAt;
    }

}
