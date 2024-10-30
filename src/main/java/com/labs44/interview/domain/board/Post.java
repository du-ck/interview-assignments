package com.labs44.interview.domain.board;

import com.labs44.interview.domain.user.Authority;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;


@Builder(toBuilder = true)
@Getter
public class Post {

    private int id;
    private int boardId;
    private int userId;
    private String title;
    private String content;
    private int deleted;
    private String created_at;
    private String updated_at;
    private String deleted_at;

    public void updatePost(Post updatePost) {
        this.id = updatePost.getId();
        this.boardId = updatePost.getBoardId();
        this.userId = updatePost.getUserId();
        this.title = updatePost.getTitle();
        this.content = updatePost.getContent();
        this.updated_at = LocalDateTime.now().toString();
    }
}
