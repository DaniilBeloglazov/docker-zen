package com.example.auth.service;

import com.example.auth.dto.JwtResponse;
import com.example.auth.model.RefreshToken;
import com.example.auth.repos.RefreshTokenRepository;
import com.example.auth.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;

    private final RefreshTokenRepository refreshTokenRepository;

    private final JwtProvider jwtProvider;

    public JwtResponse performLogin(String username, String password) {
        val authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        authenticationManager.authenticate(authenticationToken); // using UserDetailsService impl and throw Authentication Exception if auth failed
        val accessToken = jwtProvider.generateAccessToken(username);
        val refreshToken = jwtProvider.generateRefreshToken(username);
        refreshTokenRepository.deleteAllByOwner(username);
        refreshTokenRepository.save(new RefreshToken(refreshToken, username));
        return new JwtResponse(accessToken, refreshToken);
    }

    public String logout(String refreshToken) {
        val usernameFromToken = jwtProvider.getUsernameFromRefreshToken(refreshToken);
        refreshTokenRepository.deleteAllByOwner(usernameFromToken);
        return "Logout success";
    }
}
