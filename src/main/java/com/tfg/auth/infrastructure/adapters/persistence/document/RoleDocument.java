package com.tfg.auth.infrastructure.adapters.persistence.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Document(collection = "roles")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleDocument {
    @Id
    private String name;
    private Set<String> capabilities;
}
