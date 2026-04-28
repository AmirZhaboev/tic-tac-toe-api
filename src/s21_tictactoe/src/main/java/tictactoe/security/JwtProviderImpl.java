package tictactoe.security;

import java.util.Date;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import tictactoe.datasource.model.UserEntity;

@Component
public class JwtProviderImpl implements JwtProvider {

    private final String secretString = "veryLongAndHardSecretStringVeryLongAndHardSecretString";
    private final byte[] bytedString = secretString.getBytes();
    private final SecretKey key = Keys.hmacShaKeyFor(bytedString);

    @Override
    public String genAccessToken(UserEntity userEntity) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + TimeUnit.MINUTES.toMillis(15));
        return Jwts.builder().claim("id", userEntity.getId()).claim("roles", userEntity.getRoles())
                .issuedAt(now).expiration(expirationDate).signWith(key).compact();
    }

    @Override
    public String genRefreshToken(UserEntity userEntity) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + TimeUnit.MINUTES.toMillis(120));
        return Jwts.builder().claim("id", userEntity.getId())
                .issuedAt(now).expiration(expirationDate).signWith(key).compact();
    }

    @Override
    public boolean validateAccessToken(String accessToken) {
        try {
            Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(accessToken)
                    .getPayload();
            if (claims.containsKey("id") && claims.containsKey("roles") && !(claims.get("id") == null)
                    && !(claims.get("roles") == null)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean validateRefreshToken(String refreshToken) {
        try {
            Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(refreshToken)
                    .getPayload();
            if (claims.containsKey("id") && !(claims.get("id") == null)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Claims getClaims(String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
    }

}
