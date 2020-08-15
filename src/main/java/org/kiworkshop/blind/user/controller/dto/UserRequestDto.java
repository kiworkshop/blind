package org.kiworkshop.blind.user.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.kiworkshop.blind.user.domain.User;
import org.kiworkshop.blind.user.util.PasswordEncryptor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class UserRequestDto {

    @NotBlank
    private String email;
    @NotBlank
    private String password;
    private String name;

    @Builder
    public UserRequestDto(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public User toEntity() {
        return User.builder()
            .email(email)
            .password(PasswordEncryptor.encrypt(password))
            .name(name).build();
    }
}
