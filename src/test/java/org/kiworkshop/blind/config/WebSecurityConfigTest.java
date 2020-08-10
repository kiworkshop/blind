package org.kiworkshop.blind.config;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;

import javax.servlet.http.Cookie;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kiworkshop.blind.user.domain.User;
import org.kiworkshop.blind.user.repository.UserRepository;
import org.kiworkshop.blind.user.util.PasswordEncryptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
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
    private UserRepository repository;

    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply(springSecurity())
            .build();
    }

    @Test
    @DisplayName("login이 되면 200 response를 받는다")
    public void login_test() throws Exception {
        when(repository.findByEmail("harris")).thenReturn(Optional.of(getUserFixture()));
        MvcResult result = mvc.perform(post("/login")
            .with(user("harris").password("1234"))
            .contentType("application/json"))
            .andExpect(status().is(HttpStatus.MOVED_TEMPORARILY.value())).andReturn();
        Cookie cookie = result.getResponse().getCookie("JSESSIONID");
        System.out.println(cookie);
    }

    private User getUserFixture() {
        return User.builder()
            .email("harris")
            .password(PasswordEncryptor.encrypt("1234"))
            .build();
    }

}