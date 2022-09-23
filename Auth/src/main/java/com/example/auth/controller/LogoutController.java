package com.example.auth.controller;

import com.example.auth.dto.LogoutRequest;
import com.example.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LogoutController {

    private final AuthService authService;

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody LogoutRequest request){
        val msg = authService.logout(request.getRefreshToken());
        return ResponseEntity.ok(msg);
    }
}
