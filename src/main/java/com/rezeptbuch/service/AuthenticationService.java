package com.rezeptbuch.service;

import com.rezeptbuch.model.Token;
import com.rezeptbuch.model.User;
import com.rezeptbuch.model.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(UserRepository userRepository, TokenService tokenService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean validatePassword(String username, String rawPassword) {
        Optional<User> userOpt = userRepository.findByUsername(username);

        if (userOpt.isEmpty()) {
            return false;
        }

        User user = userOpt.get();
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }


    public boolean isTokenConfirmed(String token) {
        Optional<Token> tokenOpt = tokenService.findByToken(token);

        if (tokenOpt.isEmpty()) {
            return false;
        }

        Token confirmationToken = tokenOpt.get();
        return confirmationToken.getConfirmedAt() != null;
    }

    public Optional<Token> findUserToken(String username) {
        Optional<User> userOpt = userRepository.findByUsername(username);

        return userOpt.flatMap(tokenService::findByTokenForUser);
    }
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

}
