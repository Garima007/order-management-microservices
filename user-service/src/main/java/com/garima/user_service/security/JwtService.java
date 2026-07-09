package com.garima.user_service.security;

import com.garima.user_service.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8)
        );
    }

    public String generateToken(User user) {
        return Jwts.builder()
                .subject(user.getEmail())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    public Claims getAllClaims(String token){
        return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
    }

    public String extractUsername(String token){
        return getAllClaims(token).getSubject();
    }

    public Date extractExpirationDate(String token){
        return getAllClaims(token).getExpiration();
    }

    private boolean isTokenExpired(String token){
        return extractExpirationDate(token).before(new Date());
    }

    public boolean isTokenValid(String token, User user) {
        return extractUsername(token).equals(user.getEmail())
                && !isTokenExpired(token);
    }

}