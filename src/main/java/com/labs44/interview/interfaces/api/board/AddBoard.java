package com.labs44.interview.interfaces.api.board;

import com.labs44.interview.domain.board.Board;
import com.labs44.interview.domain.user.Authority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


public class AddBoard {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        @NotBlank(message = "게시판 이름이 없습니다")
        String name;

        @NotBlank(message = "게시판 설명이 없습니다")
        String description;
    }

    @Builder
    @Getter
    public static class Response {
        Board board;
    }
}
