package com.labs44.interview.interfaces.api.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


public class Register {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        @NotBlank(message = "이메일이 없습니다")
        String email;
        @NotBlank(message = "닉네임이 없습니다")
        String nickname;
        @NotBlank(message = "비밀번호가 없습니다")
        String password;
    }

    @Builder
    @Getter
    public static class Response {
        boolean result;
    }
}
