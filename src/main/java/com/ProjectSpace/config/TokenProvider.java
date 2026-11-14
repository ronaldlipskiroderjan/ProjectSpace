package com.ProjectSpace.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@Getter
public class TokenProvider {

    @Value("${jwt.expiration}")
    private long expirationTime;

    @Value("${jwt.key}")
    private String key;

    public String gerarToken(Authentication auth) {
        return null;
    }

    public boolean isTokenValid(String token) {
        return false;
    }

    public String getUsername(String token) {
        return null;
    }
}
