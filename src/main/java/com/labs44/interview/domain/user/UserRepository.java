package com.labs44.interview.domain.user;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    Optional<User> save(User user);

    Optional<User> findByEmail(String email);
    List<User> findAll();
    boolean deleteById(int userId);
}
