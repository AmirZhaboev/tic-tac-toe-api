package tictactoe.security;

import java.io.IOException;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthFilter extends GenericFilterBean {
    private final JwtProvider jwtProvider;
    private final JwtUtil jwtUtil;

    public AuthFilter(JwtProvider jwtProvider, JwtUtil jwtUtil) {
        this.jwtProvider = jwtProvider;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        String authHeader = httpServletRequest.getHeader("Authorization");

        String requestUri = httpServletRequest.getRequestURI();

        if (requestUri.equals("/auth/sign-up") || requestUri.equals("/auth/sign-in")
                || requestUri.equals("/auth/access") || requestUri.equals("/auth/refresh")) {
            chain.doFilter(request, response);
            return;
        }
        try {
            if (authHeader != null && !authHeader.isBlank() && authHeader.startsWith("Bearer ")) {
                String token = authHeader.split(" ")[1];
                if (jwtProvider.validateAccessToken(token)) {
                    Claims claims = jwtProvider.getClaims(token);
                    JwtAuthentication jwtAuthentication = jwtUtil.createJwtAuthentication(claims);
                    SecurityContextHolder.getContext().setAuthentication(jwtAuthentication);
                    chain.doFilter(request, response);
                } else {
                    throw new Exception();
                }
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
