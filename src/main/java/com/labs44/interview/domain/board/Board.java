package com.labs44.interview.domain.board;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;


@Builder(toBuilder = true)
@Getter
public class Board {

    private int id;
    private String name;
    private String description;
    private String created_at;
    private String updated_at;

    public void createdBoard(String name, String description) {
        this.name = name;
        this.description = description;
        this.created_at = LocalDateTime.now().toString();
        this.updated_at = LocalDateTime.now().toString();
    }
}
