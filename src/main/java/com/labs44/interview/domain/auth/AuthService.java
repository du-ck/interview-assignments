package com.labs44.interview.domain.auth;

import com.labs44.interview.domain.user.User;
import com.labs44.interview.domain.user.UserRepository;
import com.labs44.interview.support.exception.AlreadyExistEmailException;
import com.labs44.interview.support.utils.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 회원가입
     */
    public Optional<User> register(User user) throws Exception {
        //기존 유저 조회
        Optional<User> findUser = userRepository.findByEmail(user.getEmail());
        if (findUser.isPresent()) {
            throw new AlreadyExistEmailException("이미 존재하는 이메일입니다");
        }

        //회원가입을 위해 user 객체 데이터 처리
        user.register(passwordEncoder.encode(user.getPassword()));

        Optional<User> registerUser = userRepository.save(user);
        if (!registerUser.isPresent()) {
            throw new Exception("회원가입 실패");
        }
        return registerUser;
    }

    /**
     * 로그인
     */
    public String login(String email, String password) throws Exception {

        Optional<User> findUser = userRepository.findByEmail(email);

        if (!findUser.isPresent()) {
            throw new BadCredentialsException("이메일 혹은 비밀번호가 잘못되었습니다");
        }

        if (!passwordEncoder.matches(password, findUser.get().getPassword())) {
            throw new BadCredentialsException("이메일 혹은 비밀번호가 잘못되었습니다");
        }
        //토큰 발급
        return jwtTokenProvider.issue(email, findUser.get().getRole());
    }
}
