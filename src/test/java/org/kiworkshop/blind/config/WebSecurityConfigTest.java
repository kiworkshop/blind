package org.kiworkshop.blind.config;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kiworkshop.blind.user.controller.dto.UserRequestDto;
import org.kiworkshop.blind.user.domain.User;
import org.kiworkshop.blind.user.repository.UserRepository;
import org.kiworkshop.blind.user.service.UserService;
import org.kiworkshop.blind.user.util.PasswordEncryptor;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
@WebAppConfiguration
@SpringBootTest
class WebSecurityConfigTest {
    @Autowired
    private WebApplicationContext context;

    @Mock
    private UserRepository userRepository;

    @Autowired
    UserService userService;
    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply(springSecurity())
            .build();
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("login이 되면 302 response를 받는다")
    public void login_test() throws Exception {
        when(userRepository.findByEmail("harris")).thenReturn(Optional.of(getUserFixture()));
        mvc.perform(post("/login")
            .with(user("harris").password("1234"))
            .contentType("application/json"))
            .andExpect(status().is(HttpStatus.MOVED_TEMPORARILY.value()));
    }

    private User getUserFixture() {
        return User.builder()
            .email("harris")
            .password(PasswordEncryptor.encrypt("1234"))
            .build();
    }

    @Test
    @DisplayName("formLogin으로 로그인 성공 테스트")
    void formLoginSuccessTest() throws Exception {
        userService.createUser(UserRequestDto.builder().email("harris").password("1234").build());
        mvc.perform(formLogin().user("harris").password("1234"))
                .andExpect(authenticated());
    }

    @Test
    @DisplayName("formLogin으로 로그인 실패 테스트")
    void formLoginFailTest() throws Exception {
        userService.createUser(UserRequestDto.builder().email("harris").password("1234").build());
        mvc.perform(formLogin().user("myang").password("1234"))
                .andExpect(unauthenticated());
    }

    @Test
    @DisplayName("logout으로 로그아웃 성공 테스트")
    void formLogoutTest() throws Exception {
        userService.createUser(UserRequestDto.builder().email("harris").password("1234").build());
        mvc.perform(formLogin().user("harris").password("1234"))
                .andExpect(authenticated());
        mvc.perform(logout("/logout")).andExpect(unauthenticated());
    }
}