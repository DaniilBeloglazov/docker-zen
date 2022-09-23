package com.example.auth.service;

import com.example.auth.exception.UsernameTakenException;
import com.example.auth.repos.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        val userToLoad = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username " + username + " not found"));
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        return new User(userToLoad.getUsername(), userToLoad.getPassword(), authorities);
    }

    public void save(com.example.auth.model.User userToSave){
        if (userRepository.existsByUsername(userToSave.getUsername()))
            throw new UsernameTakenException("Username " + userToSave.getUsername() + " taken");
        val encodedPass = passwordEncoder.encode(userToSave.getPassword());
        userToSave.setPassword(encodedPass);
        userRepository.save(userToSave);
    }
}
