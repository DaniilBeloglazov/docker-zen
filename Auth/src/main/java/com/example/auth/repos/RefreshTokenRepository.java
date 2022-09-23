package com.example.auth.repos;

import com.example.auth.model.RefreshToken;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RefreshTokenRepository extends MongoRepository<RefreshToken, String> {
    void deleteAllByOwner(String username);
}
