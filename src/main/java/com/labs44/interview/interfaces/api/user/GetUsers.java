package com.labs44.interview.interfaces.api.user;

import com.labs44.interview.domain.user.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;


public class GetUsers {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        @NotNull(message = "page가 없습니다")
        int page;
        @NotNull(message = "size가 없습니다")
        int size;
    }

    @Builder
    @Getter
    public static class Response {
        List<User> users;
    }
}
