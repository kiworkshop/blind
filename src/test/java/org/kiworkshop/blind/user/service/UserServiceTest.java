package org.kiworkshop.blind.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.util.ReflectionUtils;
import org.kiworkshop.blind.user.controller.dto.UserRequestDto;
import org.kiworkshop.blind.user.domain.User;
import org.kiworkshop.blind.user.repository.UserRepository;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {


    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository);
    }

    @Test
    @DisplayName("회원가입에 성공한다.")
    void createUser() {
        //given
        User user = getUserFixture();
        given(userRepository.save(any(User.class))).willReturn(user);

        //when
        Long id = userService.createUser(getUserRequestDtoFixture());

        //then
        then(userRepository).should().save(any(User.class));
        assertThat(id).isEqualTo(1L);
    }

    private UserRequestDto getUserRequestDtoFixture(){
        return UserRequestDto.builder().email("harris").password("1234").build();
    }

    private User getUserFixture(){
        User user = getUserRequestDtoFixture().toEntity();
        ReflectionTestUtils.setField(user, "id", 1L);
        return user;
    }
}