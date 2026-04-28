package tictactoe.domain.service;

import tictactoe.security.JwtAuthentication;
import tictactoe.web.model.JwtRequest;
import tictactoe.web.model.JwtResponse;
import tictactoe.web.model.SignUpRequest;

public interface AuthService {
    boolean register(SignUpRequest signUpRequest);

    JwtResponse authorize(JwtRequest jwtRequest);

    JwtResponse updateAccessToken(String refreshToken);

    JwtResponse updateRefreshToken(String refreshToken);

    JwtAuthentication getAuth();
}
