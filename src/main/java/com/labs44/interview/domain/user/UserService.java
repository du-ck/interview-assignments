package com.labs44.interview.domain.user;

import com.labs44.interview.support.exception.AlreadyExistEmailException;
import com.labs44.interview.support.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public Optional<User> addUser(User addUser) throws Exception {

        //기존 유저 조회
        Optional<User> findUser = userRepository.findByEmail(addUser.getEmail());
        if (findUser.isPresent()) {
            throw new AlreadyExistEmailException("이미 존재하는 이메일입니다");
        }

        //addUser 데이터 처리 (날짜)
        addUser.addUser(passwordEncoder.encode(addUser.getPassword()));

        //user add
        Optional<User> user = userRepository.save(addUser);

        //추가 실패
        if (!user.isPresent()) {
            throw new Exception("유저 추가 실패");
        }
        return user;
    }

    public List<User> getUsers(int page, int size) throws Exception {
        List<User> users = userRepository.findAll(page, size);
        if (CollectionUtils.isEmpty(users)) {
            throw new ResourceNotFoundException("검색결과 없음");
        }
        return users;
    }

    public boolean updateUser(User updateUser) throws Exception {

        //유저 조회
        Optional<User> findUser = userRepository.findById(updateUser.getId());
        if (!findUser.isPresent()) {
            throw new IllegalArgumentException("없는 유저 id 입니다");
        }
        User findUserGet = findUser.get();

        //비밀번호를 다시 encode 안하도록 matches 로 처리
        //false 일 경우 비밀번호도 변경된 것
        if (!passwordEncoder.matches(updateUser.getPassword(), findUser.get().getPassword())) {
            updateUser = updateUser.toBuilder()
                    .password(passwordEncoder.encode(updateUser.getPassword()))
                    .build();
        } else {
            //비밀번호가 바뀐게 아닐 경우
            //조회해서 가져온 비밀번호를 다시 넣어줌
            updateUser = updateUser.toBuilder()
                    .password(findUserGet.getPassword())
                    .build();
        }

        //업데이트 데이터 처리
        findUserGet.updateUser(updateUser);

        //user update
        Optional<User> user = userRepository.save(findUserGet);
        if (!user.isPresent()) {
            throw new Exception("유저정보 수정 실패");
        }
        return true;
    }

    public boolean deleteUser(int id) throws Exception {
        //유저 조회
        Optional<User> findUser = userRepository.findById(id);
        if (!findUser.isPresent()) {
            throw new IllegalArgumentException("없는 유저 id 입니다");
        }
        User findUserGet = findUser.get();

        if (findUserGet.getDeleted() == 1) {
            throw new IllegalStateException("이미 삭제된 유저입니다");
        }

        //삭제 데이터 처리
        findUserGet.deleteUser();

        //user delete
        Optional<User> user = userRepository.save(findUserGet);
        if (!user.isPresent()) {
            throw new Exception("유저정보 삭제 실패");
        }
        return true;
    }
}
