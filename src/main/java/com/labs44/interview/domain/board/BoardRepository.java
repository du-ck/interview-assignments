package com.labs44.interview.domain.board;


import java.util.List;
import java.util.Optional;

public interface BoardRepository {
    Optional<Board> save(Board board);
    List<Board> findAll(int page, int size);

    Optional<Board> findByName(String name);
}
