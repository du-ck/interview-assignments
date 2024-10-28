package com.labs44.interview.domain;

import com.labs44.interview.domain.auth.AuthService;
import com.labs44.interview.domain.user.Authority;
import com.labs44.interview.domain.user.User;
import com.labs44.interview.domain.user.UserRepository;
import com.labs44.interview.support.exception.AlreadyExistEmailException;
import com.labs44.interview.support.utils.JwtTokenProvider;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    private String email = "test@email.co.kr";
    private String password = "1234";

    @Test
    void 로그인() throws Exception {
        String encodedPassword = passwordEncoder.encode(password);
        User user = User.builder()
                .id(1)
                .email(email)
                .password(encodedPassword)
                .role(Authority.USER)
                .created_at(LocalDateTime.now().toString())
                .updated_at(LocalDateTime.now().toString())
                .deleted(0)
                .nickname("test")
                .build();

        given(userRepository.findByEmail(email))
                .willReturn(Optional.of(user));

        given(passwordEncoder.matches(password, encodedPassword))
                .willReturn(true);

        given(jwtTokenProvider.issue(email))
                .willReturn(Jwts.builder()
                        .setSubject(email)
                        .setExpiration(new Date(System.currentTimeMillis() + 1800000))
                        .signWith(SignatureAlgorithm.HS512, "supersecretkey")
                        .compact());

        String token = authService.login(email, password);

        Assertions.assertNotNull(token);
    }

    @Test
    void 로그인_잘못된정보_이메일() {

        given(userRepository.findByEmail(email))
                .willReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(BadCredentialsException.class,
                () -> authService.login(email, password));

        Assertions.assertEquals("이메일 혹은 비밀번호가 잘못되었습니다", exception.getMessage());
    }

    @Test
    void 로그인_잘못된정보_비밀번호() {
        String encodedPassword = passwordEncoder.encode(password);
        String anotherPassword = "123";

        User user = User.builder()
                .id(1)
                .email(email)
                .password(encodedPassword)
                .role(Authority.USER)
                .created_at(LocalDateTime.now().toString())
                .updated_at(LocalDateTime.now().toString())
                .deleted(0)
                .nickname("test")
                .build();

        given(userRepository.findByEmail(email))
                .willReturn(Optional.of(user));

        Exception exception = Assertions.assertThrows(BadCredentialsException.class,
                () -> authService.login(email, anotherPassword));

        Assertions.assertEquals("이메일 혹은 비밀번호가 잘못되었습니다", exception.getMessage());
    }

    @Test
    void 회원가입() throws Exception {

        User user = User.builder()
                .id(1)
                .email(email)
                .password(password)
                .role(Authority.USER)
                .created_at(LocalDateTime.now().toString())
                .updated_at(LocalDateTime.now().toString())
                .deleted(0)
                .nickname("test")
                .build();

        given(userRepository.findByEmail(anyString()))
                .willReturn(Optional.empty());

        given(userRepository.save(user))
                .willReturn(Optional.of(user));

        Optional<User> registerUser = authService.register(user);

        Assertions.assertNotNull(registerUser);
    }

    @Test
    void 회원가입_이메일중복() {
        User user = User.builder()
                .id(1)
                .email(email)
                .password(password)
                .role(Authority.USER)
                .created_at(LocalDateTime.now().toString())
                .updated_at(LocalDateTime.now().toString())
                .deleted(0)
                .nickname("test")
                .build();

        given(userRepository.findByEmail(anyString()))
                .willReturn(Optional.of(user));

        Exception exception = Assertions.assertThrows(AlreadyExistEmailException.class,
                () -> authService.register(user));

        Assertions.assertEquals("이미 존재하는 이메일입니다", exception.getMessage());
    }

    @Test
    void 회원가입_실패() {
        User user = User.builder()
                .id(1)
                .email(email)
                .password(password)
                .role(Authority.USER)
                .created_at(LocalDateTime.now().toString())
                .updated_at(LocalDateTime.now().toString())
                .deleted(0)
                .nickname("test")
                .build();

        given(userRepository.findByEmail(anyString()))
                .willReturn(Optional.empty());

        given(userRepository.save(user))
                .willReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(Exception.class,
                () -> authService.register(user));

        Assertions.assertEquals("회원가입 실패", exception.getMessage());
    }
}
