package org.kiworkshop.blind.post.controller.dto.response;

import org.kiworkshop.blind.post.domain.Post;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PostSummaryResponseDto {
    private Long id;
    private String title;
    private String username;
    private int likeCount;

    @Builder
    private PostSummaryResponseDto(Long id, String title, String username, int likeCount) {
        this.id = id;
        this.title = title;
        this.username = username;
        this.likeCount = likeCount;
    }

    public static PostSummaryResponseDto from(Post post) {
        return PostSummaryResponseDto.builder()
            .id(post.getId())
            .title(post.getTitleSummary())
            .username(post.getAuthor().getName())
            .likeCount(post.getLikeCount())
            .build();
    }
}
