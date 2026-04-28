package tictactoe.security;

import java.util.Map;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import tictactoe.datasource.model.UserEntity;

public interface JwtProvider {
    String genAccessToken(UserEntity userEntity);

    String genRefreshToken(UserEntity userEntity);

    boolean validateAccessToken(String accessToken);

    boolean validateRefreshToken(String refreshToken);

    Claims getClaims(String token);

}
