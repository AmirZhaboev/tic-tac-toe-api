package tictactoe.domain.service;

import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import tictactoe.datasource.model.Role;
import tictactoe.datasource.model.UserEntity;
import tictactoe.security.JwtAuthentication;
import tictactoe.security.JwtProvider;
import tictactoe.web.model.JwtRequest;
import tictactoe.web.model.JwtResponse;
import tictactoe.web.model.SignUpRequest;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserService userService;
    private final JwtProvider jwtProvider;

    public AuthServiceImpl(UserService userService, JwtProvider jwtProvider) {
        this.userService = userService;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public boolean register(SignUpRequest signUpRequest) {
        if (signUpRequest == null || signUpRequest.getLogin() == null || signUpRequest.getPassword() == null) {
            return false;
        }

        if (signUpRequest.getLogin().isBlank() || signUpRequest.getPassword().isBlank()) {
            return false;
        }

        if (userService.existsByLogin(signUpRequest.getLogin())) {
            return false;
        }

        userService.createUser(signUpRequest.getLogin(), signUpRequest.getPassword(), Role.USER);
        return true;
    }

    @Override
    public JwtResponse authorize(JwtRequest request) {
        UserEntity userEntity = userService.findByLogin(request.getLogin())
                .orElseThrow(() -> new IllegalArgumentException("Нет такого пользователя"));
        if (!request.getPassword().equals(userEntity.getPassword())) {
            throw new IllegalArgumentException("Неверный пароль");

        } else {
            return new JwtResponse(jwtProvider.genAccessToken(userEntity), jwtProvider.genRefreshToken(userEntity));
        }
    }

    @Override
    public JwtResponse updateAccessToken(String refreshToken) {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            Claims claims = jwtProvider.getClaims(refreshToken);
            UserEntity userEntity = userService.findById(UUID.fromString(claims.get("id").toString()))
                    .orElseThrow(() -> new IllegalArgumentException("Нет такого пользователя"));
            return new JwtResponse(jwtProvider.genAccessToken(userEntity), refreshToken);
        } else {
            throw new IllegalArgumentException("Невалидный токен");
        }
    }

    @Override
    public JwtResponse updateRefreshToken(String refreshToken) {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            Claims claims = jwtProvider.getClaims(refreshToken);
            UserEntity userEntity = userService.findById(UUID.fromString(claims.get("id").toString()))
                    .orElseThrow(() -> new IllegalArgumentException("Нет такого пользователя"));
            return new JwtResponse(null, jwtProvider.genRefreshToken(userEntity));
        } else {
            throw new IllegalArgumentException("Невалидный токен");
        }
    }

    @Override
    public JwtAuthentication getAuth() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth instanceof JwtAuthentication) {
            JwtAuthentication jwtAuthentication = (JwtAuthentication) auth;
            return jwtAuthentication;
        } else {
            throw new IllegalArgumentException("Пользователь не авторизован");

        }
    }

}
