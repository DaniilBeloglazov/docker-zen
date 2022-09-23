package com.example.auth.controller;

import com.example.auth.dto.LoginRequest;
import com.example.auth.dto.JwtResponse;
import com.example.auth.dto.RefreshJwtRequest;
import com.example.auth.security.jwt.JwtProvider;
import com.example.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    private final JwtProvider jwtProvider;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest request) {
        log.info("Handle attempt login");
        val tokens = authService.performLogin(request.getUsername(), request.getPassword());
        return ResponseEntity.ok(tokens);
    }
    @PostMapping("/access/refresh")
    public ResponseEntity<JwtResponse> refreshAccessToken(@RequestBody RefreshJwtRequest request){
        log.info("Handle attempt refresh accessToken");
        val refreshToken = request.getRefreshToken();
        if (refreshToken != null && jwtProvider.validateRefreshToken(refreshToken)){
            val usernameFromToken = jwtProvider.getUsernameFromRefreshToken(refreshToken);
            val refreshedAccessToken = jwtProvider.generateAccessToken(usernameFromToken);
            return ResponseEntity.ok(new JwtResponse(refreshedAccessToken, refreshToken));
        }
        return null;
    }
}
