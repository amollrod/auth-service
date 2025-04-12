package com.tfg.auth.infrastructure.adapters.persistence.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Document(collection = "users")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDocument {
    @Id
    private String email;
    private String password;
    private boolean enabled;
    private Set<String> roleNames;
}
