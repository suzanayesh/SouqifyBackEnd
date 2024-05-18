package com.code.auth.service;

import com.code.auth.entity.RefreshToken;
import com.code.auth.repo.RefreshTokenRepository;
import com.code.auth.repo.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class RefreshTokenService {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserInfoRepository userRepository;

    @Autowired
    private JwtService jwtService;
    public void save(RefreshToken refreshToken) {
        refreshTokenRepository.save(refreshToken);
    }


    public RefreshToken createRefreshToken(String username) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUserInfo(userRepository.findByName(username)
                .orElseThrow(() -> new IllegalStateException("User not found with username: " + username)));
        refreshToken.setToken(jwtService.generateRefreshToken(username));
        refreshToken.setExpiryDate(Instant.now().plusMillis(86400000)); // 24 hours

        return refreshTokenRepository.save(refreshToken);
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException(token.getToken() + " Refresh token is expired. Please make a new login..!");
        }
        return token;
    }
}
