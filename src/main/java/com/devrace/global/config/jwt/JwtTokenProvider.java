package com.devrace.global.config.jwt;

import io.jsonwebtoken.Claims;
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

    public String createAccessToken(Long userId, String authority) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + ACCESS_TOKEN_DURATION.toMillis());

        return AUTHORIZATION_TYPE + Jwts.builder()
                .setSubject(userId.toString())
                .claim(AUTHORITIES_KEY, authority)
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

    public String substringToken(String authorizationHeaderValue) {
        if (authorizationHeaderValue.startsWith(AUTHORIZATION_TYPE)) {
            return authorizationHeaderValue.replace(AUTHORIZATION_TYPE, "");
        }
        return null;
    }

    public boolean isValidToken(String jwtToken) {
        Jwts.parser().setSigningKey(secret).parseClaimsJws(jwtToken);
        return true;
    }

    public String getSubject(String token) {
        return getClaims(token).getSubject();
    }

    public String getAuthority(String token) {
        return getClaims(token).get(AUTHORITIES_KEY, String.class);
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody();
    }

}
