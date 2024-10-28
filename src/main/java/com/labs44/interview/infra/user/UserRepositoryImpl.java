package com.labs44.interview.infra.user;

import com.labs44.interview.domain.user.User;
import com.labs44.interview.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
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
    public List<User> findAll() {
        return null;
    }

    @Override
    public boolean deleteById(int userId) {
        return false;
    }
}
