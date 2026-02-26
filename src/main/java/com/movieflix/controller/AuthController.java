package com.movieflix.controller;

import com.movieflix.auth.entity.RefreshToken;
import com.movieflix.auth.entity.User;
import com.movieflix.auth.service.AuthServiceImpl;
import com.movieflix.auth.service.JwtService;
import com.movieflix.auth.service.RefreshTokenService;
import com.movieflix.auth.util.AuthResponse;
import com.movieflix.auth.util.LoginRequest;
import com.movieflix.auth.util.RefreshTokenRequest;
import com.movieflix.auth.util.RegisterRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth/")
public class AuthController {
    private final AuthServiceImpl authService;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;

    public AuthController(AuthServiceImpl authService, RefreshTokenService refreshTokenService, JwtService jwtService) {
        this.authService = authService;
        this.refreshTokenService = refreshTokenService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(authService.register(registerRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        RefreshToken token = refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
        User user = token.getUser();
        String accessToken = jwtService.generateToken(user);
        return ResponseEntity.ok(AuthResponse.builder().accessToken(accessToken).refreshToken(refreshTokenRequest.getRefreshToken()).build());

    }
}
