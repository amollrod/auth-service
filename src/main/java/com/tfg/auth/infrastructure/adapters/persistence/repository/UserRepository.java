package com.tfg.auth.infrastructure.adapters.persistence.repository;

import com.tfg.auth.infrastructure.adapters.persistence.document.UserDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<UserDocument, String> {
    Optional<UserDocument> findByEmail(String email);
    void deleteByEmail(String email);
}
