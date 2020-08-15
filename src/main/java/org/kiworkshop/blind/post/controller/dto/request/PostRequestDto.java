package org.kiworkshop.blind.post.controller.dto.request;

import lombok.Builder;
import lombok.NoArgsConstructor;
import org.kiworkshop.blind.post.domain.Post;

import lombok.Getter;
import org.kiworkshop.blind.user.domain.User;

@Getter
@NoArgsConstructor
public class PostRequestDto {

    private String title;
    private String content;

    @Builder
    public PostRequestDto(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public Post toEntity(User author) {
        return Post.builder()
            .title(title)
            .content(content)
            .author(author)
            .build();
    }
}
