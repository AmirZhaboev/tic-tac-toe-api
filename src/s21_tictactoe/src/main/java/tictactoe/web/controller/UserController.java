package tictactoe.web.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tictactoe.datasource.model.UserEntity;
import tictactoe.domain.service.UserService;
import tictactoe.web.mapper.GameWebMapper;
import tictactoe.web.model.UserResponse;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final GameWebMapper gameWebMapper;

    public UserController(UserService userService, GameWebMapper gameWebMapper) {
        this.userService = userService;
        this.gameWebMapper = gameWebMapper;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserInfo(@PathVariable UUID userId) {
        UserEntity user = userService.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));

        return ResponseEntity.ok(gameWebMapper.toUserResponse(user));
    }
}
