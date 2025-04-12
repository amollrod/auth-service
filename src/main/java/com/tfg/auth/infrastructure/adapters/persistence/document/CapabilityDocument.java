package com.tfg.auth.infrastructure.adapters.persistence.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "capabilities")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CapabilityDocument {
    @Id
    private String id;
}
