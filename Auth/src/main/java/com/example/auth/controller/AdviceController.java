package com.example.auth.controller;

import com.example.auth.exception.UsernameTakenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class AdviceController {
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> authFailed() {
        return ResponseEntity.status(403).body("Authentication failed");
    }

    @ExceptionHandler(UsernameTakenException.class)
    public ResponseEntity<String> handleUsernameNotFound(UsernameTakenException exception) {
        log.info("Advice controller working");
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
}
