package com.labs44.interview.infra.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostJpaRepository extends JpaRepository<PostEntity, Long> {
    Page<PostEntity> findByBoardId(int boardId, Pageable pageable);
}
