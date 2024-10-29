package com.labs44.interview.interfaces.api.user;

import com.labs44.interview.domain.user.Authority;
import com.labs44.interview.domain.user.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


public class UpdateUser {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {

        @NotNull(message = "id가 없습니다")
        int id;

        @NotBlank(message = "닉네임이 없습니다")
        String nickname;

        @NotBlank(message = "비밀번호가 없습니다")
        String password;

        @NotNull(message = "권한 데이터가 없습니다")
        Authority role;
    }

    @Builder
    @Getter
    public static class Response {
        boolean result;
    }
}
