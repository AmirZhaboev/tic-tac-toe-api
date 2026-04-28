package tictactoe.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tictactoe.domain.service.AuthService;
import tictactoe.security.JwtAuthentication;
import tictactoe.web.model.JwtRequest;
import tictactoe.web.model.JwtResponse;
import tictactoe.web.model.RefreshJwtRequest;
import tictactoe.web.model.SignUpRequest;
import tictactoe.web.model.UserResponse;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<Boolean> signUp(@RequestBody SignUpRequest request) {
        boolean registered = authService.register(request);
        if (!registered) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(true);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<JwtResponse> signIn(@RequestBody JwtRequest request) {
        JwtResponse response = authService.authorize(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/access")
    public ResponseEntity<JwtResponse> refreshAccessToken(@RequestBody RefreshJwtRequest refreshJwtRequest) {
        return ResponseEntity.ok(authService.updateAccessToken(refreshJwtRequest.getRefreshToken()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refreshUpdateToken(@RequestBody RefreshJwtRequest refreshJwtRequest) {
        return ResponseEntity.ok(authService.updateRefreshToken(refreshJwtRequest.getRefreshToken()));
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getUserInfo() {
        try {
            JwtAuthentication jwtAuthentication = authService.getAuth();
            return ResponseEntity.ok(new UserResponse(jwtAuthentication.getUserId(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }
}
