package tictactoe.security;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import tictactoe.datasource.model.Role;

@Component
public class JwtUtil {
    JwtAuthentication createJwtAuthentication(Claims claims) {
        JwtAuthentication jwtAuthentication = new JwtAuthentication();

        if (claims.containsKey("roles")) {
            List<String> roles = claims.get("roles", List.class);
            Set<Role> roleSet = roles.stream().map(Role::valueOf).collect(Collectors.toSet());
            jwtAuthentication.setRoles(roleSet);
        }

        if (claims.containsKey("id") && claims.get("id") != null) {
            UUID userId = UUID.fromString(claims.get("id").toString());
            jwtAuthentication.setUserId(userId);
        }

        jwtAuthentication.setAuthenticated(true);

        return jwtAuthentication;
    }
}
