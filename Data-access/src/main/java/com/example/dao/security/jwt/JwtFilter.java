package com.example.dao.security.jwt;

import com.example.dao.repos.UserRepository;
import com.example.dao.service.UserService;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("Filter working");
        val accessToken = request.getHeader("accessToken");
        if (accessToken != null && jwtProvider.validateAccessToken(accessToken)) {
            val usernameFromToken = jwtProvider.getUsernameFromAccessToken(accessToken);
            val user = userService.loadUserByUsername(usernameFromToken);
            val authToken = new UsernamePasswordAuthenticationToken(
                    user.getUsername(),
                    user.getPassword(),
                    user.getAuthorities()
            );
            SecurityContextHolder.getContext()
                    .setAuthentication(authToken);
        }
        filterChain.doFilter(request, response);
    }
}
