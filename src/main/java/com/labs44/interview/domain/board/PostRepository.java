package com.labs44.interview.domain.board;


import java.util.List;
import java.util.Optional;

public interface PostRepository {

    Optional<Post> save(Post post);
    List<Post> findByBoardId(int boardId, int page, int size);

    Optional<Post> findById(int id);
}
