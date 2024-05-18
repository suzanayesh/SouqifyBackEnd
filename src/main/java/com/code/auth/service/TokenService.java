//package com.code.auth.service;
//
//import com.code.auth.entity.RefreshToken;
//import com.code.auth.repo.RefreshTokenRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.Date;
//import java.util.UUID;
//
//@Service
//public class TokenService {
//
//    @Autowired
//    private RefreshTokenRepository refreshTokenRepository;
//
//    public RefreshToken createRefreshToken(String username) {
//        RefreshToken refreshToken = new RefreshToken();
//        refreshToken.setName(username);
//        refreshToken.setToken(UUID.randomUUID().toString());
//        refreshToken.setExpiryDate(new Date(System.currentTimeMillis() + 86400000)); // 24 hours
//        return refreshTokenRepository.save(refreshToken);
//    }
//
//    public void deleteRefreshToken(String token) {
//        refreshTokenRepository.deleteByToken(token);
//    }
//}