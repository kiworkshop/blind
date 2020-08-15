package org.kiworkshop.blind.user.controller.dto;

import org.kiworkshop.blind.user.domain.User;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserSummaryResponseDto {
    private Long id;
    private String username;

    @Builder
    private UserSummaryResponseDto(Long id, String username) {
        this.id = id;
        this.username = username;
    }

    public static UserSummaryResponseDto from(User user) {
        return UserSummaryResponseDto.builder()
            .id(user.getId())
            .username(user.getName())
            .build();
    }
}
