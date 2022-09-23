package com.example.shell.config;

import com.example.shell.dto.JwtResponse;
import com.example.shell.dto.RefreshJwtRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
@Component
@Slf4j
@RequiredArgsConstructor
public class RestTemplateInterceptor implements ClientHttpRequestInterceptor {

    private final Authentication authentication;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        val response = execution.execute(request, body);
        if (!response.getStatusCode().equals(HttpStatus.FORBIDDEN)) {
            return response;
        } else {
            log.info("Attempt refresh access token");
            RestTemplate restTemplate = new RestTemplate();
            val refreshRequest = new HttpEntity<>(new RefreshJwtRequest(request.getHeaders().getFirst("refreshToken")));
            val refreshResponse = restTemplate.postForEntity("http://localhost:8080/access/refresh", refreshRequest, JwtResponse.class);
            val tokens = refreshResponse.getBody();
            authentication.set(tokens);
            val reqHeaders = request.getHeaders();
            reqHeaders.set("accessToken", authentication.getAccessToken());
            reqHeaders.set("refreshToken", authentication.getRefreshToken());
            val newResponse = execution.execute(request, body);
            if (newResponse.getStatusCode().equals(HttpStatus.FORBIDDEN)) {
                System.out.println("Need to re-login");
                authentication.setStatus(false);
            }
            return newResponse;
        }
    }
}