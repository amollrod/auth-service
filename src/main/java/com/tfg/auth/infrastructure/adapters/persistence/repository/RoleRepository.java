package com.tfg.auth.infrastructure.adapters.persistence.repository;

import com.tfg.auth.infrastructure.adapters.persistence.document.RoleDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoleRepository extends MongoRepository<RoleDocument, String> { }
