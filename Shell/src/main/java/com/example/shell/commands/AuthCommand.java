package com.example.shell.commands;

import com.example.shell.config.Authentication;
import com.example.shell.dto.LoginRequest;
import com.example.shell.dto.LogoutRequest;
import com.example.shell.dto.SignUpRequest;
import com.example.shell.dto.JwtResponse;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.HttpEntity;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.web.client.RestTemplate;

@ShellComponent
@ShellCommandGroup("Authentication")
@RequiredArgsConstructor
public class AuthCommand {

    private final Authentication authentication;

    private final RestTemplate restTemplate;

    @ShellMethod("Login. Takes 2 args: username, password")
    @ShellMethodAvailability("loginAvailability")
    public void login(String username, String password) {
        val request = new HttpEntity<>(new LoginRequest(username, password));
        val response = restTemplate.postForEntity("http://localhost:8080/login", request, JwtResponse.class);
        authentication.set(response.getBody());
        System.out.println("Authentication success");
    }

    @ShellMethod("Logout. Takes no args")
    @ShellMethodAvailability("logoutAvailability")
    public void logout() {
        val request = new HttpEntity<>(new LogoutRequest(authentication.getRefreshToken()));
        val response = restTemplate.postForEntity("http://localhost:8080/logout", request, String.class);
        System.out.println(response.getBody());
        authentication.logout();
    }

    @ShellMethod("Registration. Takes 2 args: username, password")
    @ShellMethodAvailability("registrationAvailability")
    public void register(String username, String password){
        val request = new HttpEntity<>(new SignUpRequest(username, password));
        val response = restTemplate.postForEntity("http://localhost:8080/signUp", request, String.class);
        System.out.println(response.getBody());
    }

    public Availability registrationAvailability() {
        return !authentication.getStatus() ? Availability.available() : Availability.unavailable("Logout to attempt registration");
    }
    public Availability loginAvailability() {
        return !authentication.getStatus() ? Availability.available() : Availability.unavailable("You are already logged in.");
    }

    public Availability logoutAvailability() {
        return authentication.getStatus() ? Availability.available() : Availability.unavailable("You must be logged in to log out.");
    }
}
