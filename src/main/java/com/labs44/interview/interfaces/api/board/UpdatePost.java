package com.labs44.interview.interfaces.api.board;

import com.labs44.interview.domain.board.Post;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


public class UpdatePost {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {

        @NotNull(message = "post id 가 없습니다")
        int postId;

        @NotNull(message = "board id 가 없습니다")
        int boardId;

        @NotNull(message = "user id 가 없습니다")
        int userId;

        @NotBlank(message = "제목이 없습니다")
        String title;

        @NotBlank(message = "내용이 없습니다")
        String content;
    }

    @Builder
    @Getter
    public static class Response {
        Post post;
    }
}
