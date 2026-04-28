package tictactoe.domain.service;

import java.util.Optional;
import java.util.UUID;

import tictactoe.datasource.model.Role;
import tictactoe.datasource.model.UserEntity;

public interface UserService {
    boolean existsByLogin(String login);

    UUID createUser(String login, String password, Role role);

    Optional<UserEntity> findByLogin(String login);

    Optional<UserEntity> findById(UUID id);
}
