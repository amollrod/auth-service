package com.tfg.auth.infrastructure.adapters.persistence.mapper;

import com.tfg.auth.domain.models.AppUser;
import com.tfg.auth.domain.models.Role;
import com.tfg.auth.infrastructure.adapters.persistence.document.UserDocument;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UserMapper {

    public static AppUser toDomain(UserDocument doc, List<Role> roles) {
        return AppUser.builder()
                .email(doc.getEmail())
                .password(doc.getPassword())
                .enabled(doc.isEnabled())
                .roles(Set.copyOf(roles))
                .build();
    }

    public static UserDocument toDocument(AppUser appUser) {
        return UserDocument.builder()
                .email(appUser.getEmail())
                .password(appUser.getPassword())
                .enabled(appUser.isEnabled())
                .roleNames(appUser.getRoles().stream()
                        .map(Role::getName)
                        .collect(Collectors.toSet()))
                .build();
    }
}
