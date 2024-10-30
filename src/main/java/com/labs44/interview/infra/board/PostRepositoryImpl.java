package com.labs44.interview.infra.board;

import com.labs44.interview.domain.board.Board;
import com.labs44.interview.domain.board.BoardRepository;
import com.labs44.interview.domain.board.Post;
import com.labs44.interview.domain.board.PostRepository;
import com.labs44.interview.infra.user.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepository {

    private final PostJpaRepository jpaRepository;

    @Override
    public Optional<Post> save(Post post) {
        Optional<PostEntity> entity = Optional.of(jpaRepository.save(PostEntity.toEntity(post)));
        if (entity.isPresent()) {
            return Optional.of(PostEntity.toDomain(entity.get()));
        }
        return Optional.empty();
    }

    @Override
    public List<Post> findByBoardId(int boardId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<PostEntity> entities = jpaRepository.findByBoardId(boardId, pageable).getContent();
        return PostEntity.toDomainList(entities);
    }

    @Override
    public Optional<Post> findById(int id) {
        Optional<PostEntity> entity = jpaRepository.findById((long) id);
        if (entity.isPresent()) {
            return Optional.of(PostEntity.toDomain(entity.get()));
        }
        return Optional.empty();
    }
}
