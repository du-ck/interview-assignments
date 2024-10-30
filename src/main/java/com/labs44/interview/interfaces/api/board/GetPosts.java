package com.labs44.interview.interfaces.api.board;

import com.labs44.interview.domain.board.Post;
import com.labs44.interview.domain.user.Authority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;


public class GetPosts {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        @NotNull(message = "board Id 가 없습니다")
        int boardId;
        @NotNull(message = "page가 없습니다")
        int page;
        @NotNull(message = "size가 없습니다")
        int size;
    }

    @Builder
    @Getter
    public static class Response {
        List<Post> posts;
    }
}
