package com.rezeptbuch.service;


import com.rezeptbuch.model.Token;
import com.rezeptbuch.model.TokenRepository;
import com.rezeptbuch.model.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class TokenService {
    private final TokenRepository tokenRepository;

    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public Optional<Token> findByToken(String token) {
        return tokenRepository.findByToken(token);
    }

    public Optional<Token> findByTokenForUser(User user) {
        return tokenRepository.findByUser(user);
    }

    public void save(Token token) {
        tokenRepository.save(token);
    }

    public Token renewExistingToken(Token token) {
        token.setToken(UUID.randomUUID().toString());
        token.setCreatedAt(LocalDateTime.now());
        token.setExpiresAt(LocalDateTime.now().plusMinutes(15));
        tokenRepository.save(token);
        return token;
    }


}