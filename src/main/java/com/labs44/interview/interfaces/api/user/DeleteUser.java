package com.labs44.interview.interfaces.api.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;


public class DeleteUser {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Jacksonized
    public static class Request {
        @NotNull(message = "id가 없습니다")
        int id;
    }

    @Builder
    @Getter
    public static class Response {
        boolean result;
    }
}
