package com.labs44.interview.interfaces;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.labs44.interview.domain.auth.AuthService;
import com.labs44.interview.domain.user.Authority;
import com.labs44.interview.domain.user.User;
import com.labs44.interview.domain.user.UserService;
import com.labs44.interview.interfaces.api.auth.Login;
import com.labs44.interview.interfaces.api.auth.Register;
import com.labs44.interview.interfaces.api.user.*;
import com.labs44.interview.support.config.SecurityConfig;
import com.labs44.interview.support.utils.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)})
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    private String email = "test@email.co.kr";
    private String password = "1234";

    @Test
    void addUser() throws Exception {

        AddUser.Request req = AddUser.Request.builder()
                .email(email)
                .nickname("test")
                .password(password)
                .role(Authority.USER)
                .deleted(0)
                .build();

        User user = User.builder()
                .id(1)
                .email(req.getEmail())
                .nickname(req.getNickname())
                .password(req.getPassword())
                .role(req.getRole())
                .deleted(req.getDeleted())
                .created_at(LocalDateTime.now().toString())
                .updated_at(LocalDateTime.now().toString())
                .build();

        given(userService.addUser(any(User.class)))
                .willReturn(Optional.of(user));

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").exists())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.id").exists())
                .andExpect(jsonPath("$.data.email").exists())
                .andExpect(jsonPath("$.data.nickname").exists())
                .andExpect(jsonPath("$.data.password").exists())
                .andExpect(jsonPath("$.data.role").exists())
                .andExpect(jsonPath("$.data.deleted").exists())
                .andExpect(jsonPath("$.data.created_at").exists())
                .andExpect(jsonPath("$.data.updated_at").exists());
    }

    @Test
    void addUser_파라미터없음() throws Exception {

        AddUser.Request req = AddUser.Request.builder()
                .nickname("test")
                .password(password)
                .role(Authority.USER)
                .deleted(0)
                .build();

        given(userService.addUser(any(User.class)))
                .willReturn(Optional.empty());


        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objMapper.writeValueAsString(req)))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.code").exists())
                .andExpect(jsonPath("$.message").value("이메일이 없습니다"));


        req = AddUser.Request.builder()
                .email("test@email.co.kr")
                .password(password)
                .role(Authority.USER)
                .deleted(0)
                .build();

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objMapper.writeValueAsString(req)))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.code").exists())
                .andExpect(jsonPath("$.message").value("닉네임이 없습니다"));

        req = AddUser.Request.builder()
                .email("test@email.co.kr")
                .nickname("test")
                .role(Authority.USER)
                .deleted(0)
                .build();

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objMapper.writeValueAsString(req)))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.code").exists())
                .andExpect(jsonPath("$.message").value("비밀번호가 없습니다"));

        req = AddUser.Request.builder()
                .email("test@email.co.kr")
                .nickname("test")
                .password(password)
                .deleted(0)
                .build();

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objMapper.writeValueAsString(req)))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.code").exists())
                .andExpect(jsonPath("$.message").value("권한 데이터가 없습니다"));
    }

    @Test
    void getUsers() throws Exception {
        List<User> users = new ArrayList<>();
        users.add(User.builder()
                .id(1)
                .email(email)
                .password(password)
                .role(Authority.USER)
                .created_at(LocalDateTime.now().toString())
                .updated_at(LocalDateTime.now().toString())
                .deleted(0)
                .nickname("test")
                .build());

        users.add(User.builder()
                .id(2)
                .email("test22@test.co.kr")
                .password(password)
                .role(Authority.USER)
                .created_at(LocalDateTime.now().toString())
                .updated_at(LocalDateTime.now().toString())
                .deleted(0)
                .nickname("test22")
                .build());

        users.add(User.builder()
                .id(3)
                .email("test33@test.co.kr")
                .password(password)
                .role(Authority.USER)
                .created_at(LocalDateTime.now().toString())
                .updated_at(LocalDateTime.now().toString())
                .deleted(0)
                .nickname("test33")
                .build());
        
        GetUsers.Request req = GetUsers.Request.builder()
                .page(0)
                .size(10)
                .build();

        given(userService.getUsers(req.getPage(), req.getSize()))
                .willReturn(users);

        mockMvc.perform(get("/users/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", String.valueOf(req.getPage()))
                        .param("size", String.valueOf(req.getSize()))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").exists())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.users").exists())
                .andExpect(jsonPath("$.data.users[*].id").exists())
                .andExpect(jsonPath("$.data.users[*].email").exists())
                .andExpect(jsonPath("$.data.users[*].nickname").exists())
                .andExpect(jsonPath("$.data.users[*].password").exists())
                .andExpect(jsonPath("$.data.users[*].role").exists())
                .andExpect(jsonPath("$.data.users[*].deleted").exists())
                .andExpect(jsonPath("$.data.users[*].created_at").exists())
                .andExpect(jsonPath("$.data.users[*].updated_at").exists());
    }

    @Test
    void updateUser() throws Exception {
        UpdateUser.Request req = UpdateUser.Request.builder()
                .id(2)
                .nickname("test")
                .password(password)
                .role(Authority.USER)
                .build();

        User updateUser = User.builder()
                .id(req.getId())
                .nickname(req.getNickname())
                .password(req.getPassword())
                .role(req.getRole())
                .build();

        given(userService.updateUser(updateUser))
                .willReturn(true);

        mockMvc.perform(put("/users/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").exists())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.result").exists());
    }

    @Test
    void updateUsers_파라미터없음() throws Exception {

        UpdateUser.Request req = UpdateUser.Request.builder()
                .id(2)
                .password(password)
                .role(Authority.USER)
                .build();

        mockMvc.perform(put("/users/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objMapper.writeValueAsString(req)))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.code").exists())
                .andExpect(jsonPath("$.message").value("닉네임이 없습니다"));

        req = UpdateUser.Request.builder()
                .id(2)
                .nickname("test")
                .role(Authority.USER)
                .build();

        mockMvc.perform(put("/users/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objMapper.writeValueAsString(req)))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.code").exists())
                .andExpect(jsonPath("$.message").value("비밀번호가 없습니다"));

        req = UpdateUser.Request.builder()
                .id(2)
                .nickname("test")
                .password(password)
                .build();

        mockMvc.perform(put("/users/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objMapper.writeValueAsString(req)))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.code").exists())
                .andExpect(jsonPath("$.message").value("권한 데이터가 없습니다"));


    }

    @Test
    void deleteUser() throws Exception {
        DeleteUser.Request req = DeleteUser.Request.builder()
                .id(2)
                .build();

        given(userService.deleteUser(req.getId()))
                .willReturn(true);

        mockMvc.perform(delete("/users/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").exists())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.result").exists());
    }

}
