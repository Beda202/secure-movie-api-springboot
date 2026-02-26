package com.movieflix.auth.service;

import com.movieflix.auth.Enum.UserRole;
import com.movieflix.auth.entity.User;
import com.movieflix.auth.repo.UserRepo;
import com.movieflix.auth.util.AuthResponse;
import com.movieflix.auth.util.LoginRequest;
import com.movieflix.auth.util.RegisterRequest;
import com.movieflix.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl {
    private final PasswordEncoder passwordEncoder;
    private final UserRepo userRepository;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;
    private final UserRepo userRepo;

    public AuthResponse register(RegisterRequest registerRequest) {
        User user = User.builder()
                .name(registerRequest.getName())
                .email(registerRequest.getEmail())
                .userName(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(UserRole.USER)
                .build();
        User saved = userRepository.save(user);
        return AuthResponse.builder()
                .accessToken(jwtService.generateToken(saved))
                .refreshToken(refreshTokenService.generateRefreshToken(saved.getUsername()).getRefreshToken())
                .build();
    }

    public AuthResponse login(LoginRequest loginRequest) {

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );

        User user = userRepo.findByEmail(loginRequest.getEmail()).orElseThrow(() ->
                new UserNotFoundException("User not found")
        );

        String accessToken = jwtService.generateToken(user);
        String refreshToken = refreshTokenService.generateRefreshToken(user.getUsername()).getRefreshToken();
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

    }
}
