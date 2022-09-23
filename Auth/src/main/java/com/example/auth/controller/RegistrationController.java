package com.example.auth.controller;

import com.example.auth.dto.SignUpRequest;
import com.example.auth.model.User;
import com.example.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RegistrationController {

    private final UserService userService;

    @PostMapping("/signUp")
    public ResponseEntity<String> registerUser(@RequestBody SignUpRequest request){
        val userToSave = new User(request);
        userService.save(userToSave);
        return ResponseEntity.ok("User " + request.getUsername() + " was registered");
    }
}
