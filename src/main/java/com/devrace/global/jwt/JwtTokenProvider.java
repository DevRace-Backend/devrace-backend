package com.devrace.global.jwt;

import com.devrace.domain.user.enums.UserRole;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final Duration ACCESS_TOKEN_DURATION = Duration.ofHours(2);
    public static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(14);

    private static final String AUTHORIZATION_TYPE = "Bearer ";
    private static final String AUTHORITIES_KEY = "authorities";

    @Value("${spring.jwt.secret}")
    private String secret;
    private Key key;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secret);
        key = Keys.hmacShaKeyFor(bytes);
    }

    public String createAccessToken(Long userId, UserRole role) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + ACCESS_TOKEN_DURATION.toMillis());

        return AUTHORIZATION_TYPE + Jwts.builder()
                .setSubject(userId.toString())
                .claim(AUTHORITIES_KEY, role)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key)
                .compact();
    }

    public String createRefreshToken() {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + REFRESH_TOKEN_DURATION.toMillis());

        return Jwts.builder()
                .setSubject(UUID.randomUUID().toString())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key)
                .compact();
    }

    public boolean isValidToken(String jwtToken) {
        Jwts.parser().setSigningKey(secret).parseClaimsJws(jwtToken);
        return true;
    }

}
