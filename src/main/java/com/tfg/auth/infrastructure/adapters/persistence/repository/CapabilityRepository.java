package com.tfg.auth.infrastructure.adapters.persistence.repository;

import com.tfg.auth.infrastructure.adapters.persistence.document.CapabilityDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CapabilityRepository extends MongoRepository<CapabilityDocument, String> { }
