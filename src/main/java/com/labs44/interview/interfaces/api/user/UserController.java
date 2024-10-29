package com.labs44.interview.interfaces.api.user;

import com.labs44.interview.domain.user.User;
import com.labs44.interview.domain.user.UserService;
import com.labs44.interview.interfaces.api.dto.ResponseData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * User CRUD api
 * 관리자용
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    /**
     * User 추가
     * 회원가입과 비슷한 기능이지만
     * 권한을 설정할 수 있다.
     */
    @PostMapping
    public ResponseEntity<ResponseData> addUser(@RequestBody @Valid AddUser.Request req) throws Exception {
        User addUser = User.builder()
                .email(req.getEmail())
                .nickname(req.getNickname())
                .password(req.getPassword())
                .role(req.getRole())
                .deleted(req.getDeleted())
                .build();

        Optional<User> user = userService.addUser(addUser);

        return new ResponseEntity<>(ResponseData.builder()
                .isSuccess(true)
                .code("200")
                .data(user.get())
                .build(), HttpStatus.OK);
    }

    /**
     * User 목록 조회
     * @return
     */
    @GetMapping("/")
    public ResponseEntity<ResponseData> getUsers(@Valid GetUsers.Request req) throws Exception {
        List<User> users = userService.getUsers(req.getPage(), req.getSize());

        GetUsers.Response response = GetUsers.Response.builder()
                .users(users)
                .build();

        return new ResponseEntity<>(ResponseData.builder()
                .isSuccess(true)
                .code("200")
                .data(response)
                .build(), HttpStatus.OK);
    }

    /**
     * user 정보 변경
     * 이메일은 변경 불가 ( user id 역할 )
     *
     */
    @PutMapping("/update")
    public ResponseEntity<ResponseData> updateUser(@RequestBody @Valid UpdateUser.Request req) throws Exception {
        User updateUser = User.builder()
                .id(req.getId())
                .nickname(req.getNickname())
                .password(req.getPassword())
                .role(req.getRole())
                .build();

        boolean updateResult = userService.updateUser(updateUser);
        UpdateUser.Response response = UpdateUser.Response.builder()
                .result(updateResult)
                .build();

        return new ResponseEntity<>(ResponseData.builder()
                .isSuccess(true)
                .code("200")
                .data(response)
                .build(), HttpStatus.OK);
    }

    /**
     * user정보 삭제 (soft delete)
     *
     * deleted = 1 처리
     */
    @DeleteMapping("/delete")
    public ResponseEntity<ResponseData> deleteUser(@RequestBody @Valid DeleteUser.Request req) throws Exception {
        boolean updateResult = userService.deleteUser(req.getId());

        DeleteUser.Response response = DeleteUser.Response.builder()
                .result(updateResult)
                .build();

        return new ResponseEntity<>(ResponseData.builder()
                .isSuccess(true)
                .code("200")
                .data(response)
                .build(), HttpStatus.OK);
    }

}
