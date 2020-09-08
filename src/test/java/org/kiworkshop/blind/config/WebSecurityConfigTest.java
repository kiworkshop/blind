package org.kiworkshop.blind.config;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kiworkshop.blind.user.controller.LoginRequest;
import org.kiworkshop.blind.user.controller.dto.UserRequestDto;
import org.kiworkshop.blind.user.repository.UserRepository;
import org.kiworkshop.blind.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
@WebAppConfiguration
@SpringBootTest
class WebSecurityConfigTest {
    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    private ObjectMapper mapper;

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
    @DisplayName("login이 되면 200 response를 받는다")
    public void login_test() throws Exception {
        String jsonLoginRequest = mapper.writeValueAsString(getLoginRequestFixture());
        userService.createUser(UserRequestDto.builder().email("harris").password("1234").build());
        mvc.perform(post("/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonLoginRequest)
        )
            .andExpect(status().is(HttpStatus.OK.value()));
    }

    private LoginRequest getLoginRequestFixture() {
        return LoginRequest.builder()
            .email("harris")
            .password("1234")
            .build();
    }

    @Test
    @DisplayName("login 실패시 401을 리턴받는다")
    void formLoginFailTest() throws Exception {
        String jsonLoginRequest = mapper.writeValueAsString(getLoginRequestFixture());
        userService.createUser(UserRequestDto.builder().email("harris").password("55").build());
        mvc.perform(post("/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonLoginRequest)
        )
            .andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    @DisplayName("logout으로 로그아웃 성공 테스트")
    void formLogoutTest() throws Exception {
        String jsonLoginRequest = mapper.writeValueAsString(getLoginRequestFixture());
        userService.createUser(UserRequestDto.builder().email("harris").password("1234").build());
        mvc.perform(post("/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonLoginRequest)
        )
            .andExpect(status().is(HttpStatus.OK.value()));
        mvc.perform(post("/logout"))
            .andExpect(status().is(HttpStatus.OK.value()));
    }
}