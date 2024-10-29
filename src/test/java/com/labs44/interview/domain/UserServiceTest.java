package com.labs44.interview.domain;

import com.labs44.interview.domain.auth.AuthService;
import com.labs44.interview.domain.user.Authority;
import com.labs44.interview.domain.user.User;
import com.labs44.interview.domain.user.UserRepository;
import com.labs44.interview.domain.user.UserService;
import com.labs44.interview.support.exception.AlreadyExistEmailException;
import com.labs44.interview.support.exception.ResourceNotFoundException;
import com.labs44.interview.support.utils.JwtTokenProvider;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    private String email = "test@email.co.kr";
    private String password = "1234";


    @Test
    void 유저추가() throws Exception {
        User user = User.builder()
                .id(1)
                .email(email)
                .nickname("test")
                .password(password)
                .role(Authority.USER)
                .deleted(0)
                .created_at(LocalDateTime.now().toString())
                .updated_at(LocalDateTime.now().toString())
                .build();
        given(userRepository.findByEmail(anyString()))
                .willReturn(Optional.empty());

        given(userRepository.save(any(User.class)))
                .willReturn(Optional.of(user));

        Optional<User> addUser = userService.addUser(user);
        Assertions.assertTrue(addUser.isPresent());
    }

    @Test
    void 유저추가_존재하는이메일() throws Exception {
        User user = User.builder()
                .id(1)
                .email(email)
                .nickname("test")
                .password(password)
                .role(Authority.USER)
                .deleted(0)
                .created_at(LocalDateTime.now().toString())
                .updated_at(LocalDateTime.now().toString())
                .build();
        given(userRepository.findByEmail(anyString()))
                .willReturn(Optional.of(user));

        Exception exception = Assertions.assertThrows(AlreadyExistEmailException.class,
                () -> userService.addUser(user));

        Assertions.assertEquals("이미 존재하는 이메일입니다", exception.getMessage());
    }

    @Test
    void 유저추가_실패() throws Exception {
        User user = User.builder()
                .id(1)
                .email(email)
                .nickname("test")
                .password(password)
                .role(Authority.USER)
                .deleted(0)
                .created_at(LocalDateTime.now().toString())
                .updated_at(LocalDateTime.now().toString())
                .build();
        given(userRepository.findByEmail(anyString()))
                .willReturn(Optional.empty());

        given(userRepository.save(any(User.class)))
                .willReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(Exception.class,
                () -> userService.addUser(user));

        Assertions.assertEquals("유저 추가 실패", exception.getMessage());
    }

    @Test
    void 유저조회() throws Exception {
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

        given(userRepository.findAll(0, 10))
                .willReturn(users);

        List<User> resultUsers = userService.getUsers(0, 10);
        Assertions.assertEquals(users.size(), resultUsers.size());
    }

    @Test
    void 유저조회_결과없음() throws Exception {
        List<User> users = new ArrayList<>();

        given(userRepository.findAll(0, 10))
                .willReturn(users);

        Exception exception = Assertions.assertThrows(ResourceNotFoundException.class,
                () -> userService.getUsers(0, 10));

        Assertions.assertEquals("검색결과 없음", exception.getMessage());
    }

    @Test
    void 유저정보수정() throws Exception {
        User updateUser = User.builder()
                .id(1)
                .nickname("test")
                .password(password)
                .role(Authority.USER)
                .build();

        given(userRepository.findById(anyInt()))
                .willReturn(Optional.of(updateUser));

        given(userRepository.save(any(User.class)))
                .willReturn(Optional.of(updateUser));

        boolean result = userService.updateUser(updateUser);

        Assertions.assertTrue(result);
    }

    @Test
    void 유저정보수정_유저id없음() throws Exception {
        User updateUser = User.builder()
                .id(1)
                .nickname("test")
                .password(password)
                .role(Authority.USER)
                .build();

        given(userRepository.findById(anyInt()))
                .willReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> userService.updateUser(updateUser));

        Assertions.assertEquals("없는 유저 id 입니다", exception.getMessage());
    }

    @Test
    void 유저정보수정_실패() throws Exception {
        User updateUser = User.builder()
                .id(1)
                .nickname("test")
                .password(password)
                .role(Authority.USER)
                .build();

        given(userRepository.findById(anyInt()))
                .willReturn(Optional.of(updateUser));

        given(userRepository.save(any(User.class)))
                .willReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(Exception.class,
                () -> userService.updateUser(updateUser));

        Assertions.assertEquals("유저정보 수정 실패", exception.getMessage());
    }

    @Test
    void 유저정보삭제() throws Exception {
        User deleteUser = User.builder()
                .id(1)
                .deleted(0)
                .build();

        given(userRepository.findById(anyInt()))
                .willReturn(Optional.of(deleteUser));

        given(userRepository.save(any(User.class)))
                .willReturn(Optional.of(deleteUser));

        boolean result = userService.deleteUser(deleteUser.getId());

        Assertions.assertTrue(result);
    }

    @Test
    void 유저정보삭제_유저id없음() {
        User deleteUser = User.builder()
                .id(1)
                .deleted(0)
                .build();

        given(userRepository.findById(anyInt()))
                .willReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> userService.deleteUser(deleteUser.getId()));

        Assertions.assertEquals("없는 유저 id 입니다", exception.getMessage());
    }

    @Test
    void 유저정보삭제_이미삭제된유저() {
        User deleteUser = User.builder()
                .id(1)
                .deleted(1)
                .build();

        given(userRepository.findById(anyInt()))
                .willReturn(Optional.of(deleteUser));

        Exception exception = Assertions.assertThrows(IllegalStateException.class,
                () -> userService.deleteUser(deleteUser.getId()));

        Assertions.assertEquals("이미 삭제된 유저입니다", exception.getMessage());
    }
}
