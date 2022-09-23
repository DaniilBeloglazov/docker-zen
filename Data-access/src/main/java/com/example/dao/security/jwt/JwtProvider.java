package com.example.dao.security.jwt;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Slf4j
@Component
public class JwtProvider {

    @Value("${jwt.access.secret}")
    private String jwtAccessSecret;

    @Value("${jwt.refresh.secret}")
    private String jwtRefreshSecret;

    @Value("${jwt.access.exp}")
    private Long jwtAccessExp;

    @Value("${jwt.refresh.exp}")
    private Long jwtRefreshExp;

    public String generateAccessToken(String username) {
        val now = LocalDateTime.now();
        val accessExpirationInstant = now.plusMinutes(jwtAccessExp).atZone(ZoneId.systemDefault()).toInstant();
        val accessExpiration = Date.from(accessExpirationInstant);
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(accessExpiration)
                .signWith(SignatureAlgorithm.HS256, jwtAccessSecret)
                .compact();
    }

    public String generateRefreshToken(String username) {
        val now = LocalDateTime.now();
        val refreshExpirationInstant = now.plusMinutes(jwtRefreshExp).atZone(ZoneId.systemDefault()).toInstant();
        val refreshExpiration = Date.from(refreshExpirationInstant);
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(refreshExpiration)
                .signWith(SignatureAlgorithm.HS256, jwtAccessSecret)
                .compact();
    }

    public boolean validateAccessToken(String accessToken) {
        return validateToken(accessToken, jwtAccessSecret);
    }

    public boolean validateRefreshToken(String refreshToken) {
        return validateToken(refreshToken, jwtRefreshSecret);
    }

    private boolean validateToken(String token, String secret) {
        try {
            Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException expEx) {
            log.error("Token expired", expEx);
        } catch (UnsupportedJwtException unsEx) {
            log.error("Unsupported jwt", unsEx);
        } catch (MalformedJwtException mjEx) {
            log.error("Malformed jwt", mjEx);
        } catch (SignatureException sEx) {
            log.error("Invalid signature", sEx);
        } catch (Exception e) {
            log.error("invalid token", e);
        }
        return false;
    }

    public String getUsernameFromAccessToken(String token) {
        return Jwts.parser().setSigningKey(jwtAccessSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public String getUsernameFromRefreshToken(String token) {
        return Jwts.parser().setSigningKey(jwtRefreshSecret).parseClaimsJws(token).getBody().getSubject();
    }
}