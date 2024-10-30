package com.labs44.interview.infra.board;

import com.labs44.interview.domain.board.Board;
import com.labs44.interview.domain.board.BoardRepository;
import com.labs44.interview.domain.board.Post;
import com.labs44.interview.domain.user.User;
import com.labs44.interview.domain.user.UserRepository;
import com.labs44.interview.infra.user.UserEntity;
import com.labs44.interview.infra.user.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepository {

    private final BoardJpaRepository jpaRepository;

    @Override
    public Optional<Board> save(Board board) {
        Optional<BoardEntity> entity = Optional.of(jpaRepository.save(BoardEntity.toEntity(board)));
        if (entity.isPresent()) {
            return Optional.of(BoardEntity.toDomain(entity.get()));
        }
        return Optional.empty();
    }

    @Override
    public List<Board> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<BoardEntity> entities = jpaRepository.findAll(pageable).getContent();
        return BoardEntity.toDomainList(entities);
    }

    @Override
    public Optional<Board> findByName(String name) {
        Optional<BoardEntity> entity = jpaRepository.findByName(name);
        if (entity.isPresent()) {
            return Optional.of(BoardEntity.toDomain(entity.get()));
        }
        return Optional.empty();
    }
}
