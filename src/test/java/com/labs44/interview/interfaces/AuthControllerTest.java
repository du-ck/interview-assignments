package com.labs44.interview.interfaces;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.labs44.interview.domain.auth.AuthService;
import com.labs44.interview.domain.user.User;
import com.labs44.interview.interfaces.api.auth.AuthController;
import com.labs44.interview.interfaces.api.auth.Login;
import com.labs44.interview.interfaces.api.auth.Register;
import com.labs44.interview.support.config.SecurityConfig;
import com.labs44.interview.support.filter.JwtFilter;
import com.labs44.interview.support.utils.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)})
class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objMapper;

    @MockBean
    private AuthService authService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Test
    void login() throws Exception {

        Login.Request req = Login.Request.builder()
                .email("test@email.co.kr")
                .password("1234")
                .build();

        given(authService.login(anyString(), anyString()))
                .willReturn("test");

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").exists())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.token").exists());
    }

    @Test
    void login_파라미터없음() throws Exception {
        Login.Request req = Login.Request.builder()
                .email("test@email.co.kr")
                .build();

        given(authService.login(anyString(), anyString()))
                .willReturn("test");

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objMapper.writeValueAsString(req)))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.code").exists())
                .andExpect(jsonPath("$.message").value("비밀번호가 없습니다"));

        req = Login.Request.builder()
                .password("1234")
                .build();

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objMapper.writeValueAsString(req)))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.code").exists())
                .andExpect(jsonPath("$.message").value("이메일이 없습니다"));
    }

    @Test
    void register() throws Exception {

        Register.Request req = Register.Request.builder()
                .email("test@email.co.kr")
                .nickname("test")
                .password("1234")
                .build();

        User user = User.builder()
                .email(req.getEmail())
                .nickname(req.getNickname())
                .password(req.getPassword())
                .build();

        user.register(req.getPassword());

        given(authService.register(user))
                .willReturn(Optional.of(user));

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").exists())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.result").exists());
    }

    @Test
    void register_파라미터없음() throws Exception {
        /*Register.Request req = Register.Request.builder()
                .email("test@email.co.kr")
                .nickname("test")
                .password("1234")
                .build();*/
        Register.Request req = Register.Request.builder()
                .nickname("test")
                .password("1234")
                .build();

        User user = User.builder()
                .email(req.getEmail())
                .nickname(req.getNickname())
                .password(req.getPassword())
                .build();
        user.register(req.getPassword());

        given(authService.register(user))
                .willReturn(Optional.of(user));

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objMapper.writeValueAsString(req)))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.code").exists())
                .andExpect(jsonPath("$.message").value("이메일이 없습니다"));

        req = Register.Request.builder()
                .email("test@email.co.kr")
                .password("1234")
                .build();

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objMapper.writeValueAsString(req)))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.code").exists())
                .andExpect(jsonPath("$.message").value("닉네임이 없습니다"));

        req = Register.Request.builder()
                .email("test@email.co.kr")
                .nickname("test")
                .build();

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objMapper.writeValueAsString(req)))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.code").exists())
                .andExpect(jsonPath("$.message").value("비밀번호가 없습니다"));
    }
}
