package tictactoe.domain.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;

import tictactoe.datasource.model.Role;
import tictactoe.datasource.model.UserEntity;
import tictactoe.datasource.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean existsByLogin(String login) {
        return userRepository.findByLogin(login).isPresent();
    }

    @Override
    public UUID createUser(String login, String password, Role role) {
        UserEntity user = new UserEntity();
        user.setId(UUID.randomUUID());
        user.setLogin(login);
        user.setPassword(password);
        user.setRoles(new HashSet<>(Set.of(role)));

        userRepository.save(user);
        return user.getId();
    }

    @Override
    public Optional<UserEntity> findByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    @Override
    public Optional<UserEntity> findById(UUID id) {
        return userRepository.findById(id);
    }

}
