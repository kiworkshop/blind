package org.kiworkshop.blind.comment.service;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.kiworkshop.blind.comment.controller.dto.CommentResponse;
import org.kiworkshop.blind.comment.domain.Comment;
import org.kiworkshop.blind.comment.util.NameTagExtractor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentModelMapper {

    public static CommentResponse createCommentResponse(Comment comment) {
        List<String> nameTags = NameTagExtractor.extractNameTags(comment.getContent());
        return CommentResponse.builder()
            .id(comment.getId())
            .content(comment.getContent())
            .nameTags(nameTags)
            .authorName(comment.getCreatedBy().getName())
            .postId(comment.getPost().getId())
            .createdAt(comment.getCreatedDate())
            .lastUpdatedAt(comment.getLastModifiedDate())
            .build();
    }
}
