package com.github.richardjwild.blather.user;

import java.sql.SQLException;
import java.util.Optional;

public interface UserRepository {
    Optional<User> find(String name) throws SQLException;

    void save(User user) throws SQLException;
}
