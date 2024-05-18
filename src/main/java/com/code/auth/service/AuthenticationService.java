package com.code.auth.service;

import org.springframework.beans.factory.annotation.Autowired;

public class AuthenticationService {
        @Autowired
        private JwtService jwtService;

        public String getUsernameFromToken(String token) {
            return jwtService.extractUsername(token);
        }
    }

