package com.example.shell.config;

import com.example.shell.dto.JwtResponse;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
public class Authentication {
    @Setter
    private boolean status;
    @Getter
    @Setter
    private String accessToken;
    @Getter
    @Setter
    private String refreshToken;

    public boolean getStatus() {
        return status;
    }

    public void set(JwtResponse tokens) {
        accessToken = tokens.getAccessToken();
        refreshToken = tokens.getRefreshToken();
        status = true;
    }

    public void logout(){
        accessToken = null;
        refreshToken = null;
        status = false;
    }
}
