package com.movieflix.auth.service;

import com.movieflix.auth.entity.RefreshToken;
import com.movieflix.auth.entity.User;
import com.movieflix.auth.repo.RefreshTokenRepo;
import com.movieflix.auth.repo.UserRepo;
import com.movieflix.exception.RefreshTokenException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor
@Service
public class RefreshTokenService {
    private final UserRepo userRepo ;
    private final RefreshTokenRepo refreshTokenRepo ;

    public RefreshToken generateRefreshToken(String userName){
        User user = userRepo.findByEmail(userName).orElseThrow(() -> new UsernameNotFoundException("UserName Not Found Exception"));
        RefreshToken refreshToken = user.getRefreshToken();
        if(refreshToken == null){
            long refreshTokenValidity = 15 * 60 * 60 * 1000 ;
            refreshToken = RefreshToken.builder()
                    .refreshToken(UUID.randomUUID().toString())
                    .expirationTime(Instant.now().plusMillis(refreshTokenValidity))
                    .user(user)
                    .build();
            refreshTokenRepo.save(refreshToken);
        }
        return refreshToken;
    }

    public RefreshToken validateRefreshToken(String refreshToken){
        RefreshToken token = refreshTokenRepo.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new RefreshTokenException("Refresh Token Not Found Exception"));

      if(token.getExpirationTime().compareTo(Instant.now()) < 0){
          refreshTokenRepo.delete(token);
          throw new RefreshTokenException("Refresh Token Expired Exception");
      }
      return token;
    }

}
