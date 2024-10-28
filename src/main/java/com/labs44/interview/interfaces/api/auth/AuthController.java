package com.labs44.interview.interfaces.api.auth;

import com.labs44.interview.domain.auth.AuthService;
import com.labs44.interview.domain.user.User;
import com.labs44.interview.interfaces.api.dto.ResponseData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ResponseData> login(@RequestBody @Valid Login.Request req) throws Exception {

        String token = authService.login(req.getEmail(), req.getPassword());

        Login.Response response = Login.Response.builder()
                .token(token)
                .build();

        return new ResponseEntity<>(ResponseData.builder()
                .isSuccess(true)
                .code("200")
                .data(response)
                .build(), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseData> register(@RequestBody @Valid Register.Request req) throws Exception {

        Optional<User> user = authService.register(User.builder()
                .email(req.getEmail())
                .nickname(req.getNickname())
                .password(req.getPassword())
                .build());

        Register.Response response = Register.Response.builder()
                .result(user.isPresent())
                .build();

        return new ResponseEntity<>(ResponseData.builder()
                .isSuccess(true)
                .code("200")
                .data(response)
                .build(), HttpStatus.OK);
    }
}
