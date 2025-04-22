package com.tfg.auth.infrastructure.adapters.security;

import com.tfg.auth.domain.models.AppUser;
import com.tfg.auth.domain.models.Role;
import com.tfg.auth.domain.ports.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtClaimsSet.Builder;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import java.util.Set;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
public class TokenCustomizer {

    private final UserRepositoryPort userRepository;

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer() {
        return context -> {
            if (context.getPrincipal() == null) return;

            Authentication principal = context.getPrincipal();

            String username = principal.getName();
            AppUser user = userRepository.findByEmail(username)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            Set<String> roles = user.getRoles().stream()
                    .map(Role::getName)
                    .collect(Collectors.toSet());

            Set<String> capabilities = user.getRoles().stream()
                    .flatMap(role -> role.getCapabilities().stream())
                    .map(Enum::name)
                    .collect(Collectors.toSet());

            Builder claims = context.getClaims();
            claims.claim("roles", roles);
            claims.claim("capabilities", capabilities);
        };
    }
}
