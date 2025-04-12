package com.tfg.auth.infrastructure.adapters.persistence.mapper;

import com.tfg.auth.domain.models.Role;
import com.tfg.auth.domain.models.User;
import com.tfg.auth.infrastructure.adapters.persistence.document.UserDocument;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UserMapper {

    public static User toDomain(UserDocument doc, List<Role> roles) {
        return User.builder()
                .email(doc.getEmail())
                .password(doc.getPassword())
                .enabled(doc.isEnabled())
                .roles(Set.copyOf(roles))
                .build();
    }

    public static UserDocument toDocument(User user) {
        return UserDocument.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .enabled(user.isEnabled())
                .roleNames(user.getRoles().stream()
                        .map(Role::getName)
                        .collect(Collectors.toSet()))
                .build();
    }
}
