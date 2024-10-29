package com.labs44.interview.infra.user;

import com.labs44.interview.domain.user.User;
import com.labs44.interview.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository jpaRepository;

    @Override
    public Optional<User> save(User user) {
        Optional<UserEntity> entity = Optional.of(jpaRepository.save(UserEntity.toEntity(user)));
        if (entity.isPresent()) {
            return Optional.of(UserEntity.toDomain(entity.get()));
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        Optional<UserEntity> entity = jpaRepository.findByEmail(email);
        if (entity.isPresent()) {
            return Optional.of(UserEntity.toDomain(entity.get()));
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findById(int id) {
        Optional<UserEntity> entity = jpaRepository.findById((long) id);
        if (entity.isPresent()) {
            return Optional.of(UserEntity.toDomain(entity.get()));
        }
        return Optional.empty();
    }

    @Override
    public List<User> findAll(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        List<UserEntity> entities = jpaRepository.findAll(pageable).getContent();

        return UserEntity.toDomainList(entities);
    }
}
