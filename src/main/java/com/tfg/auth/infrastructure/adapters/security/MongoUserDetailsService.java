package com.tfg.auth.infrastructure.adapters.security;

import com.tfg.auth.infrastructure.adapters.persistence.document.UserDocument;
import com.tfg.auth.infrastructure.adapters.persistence.repository.RoleRepository;
import com.tfg.auth.infrastructure.adapters.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MongoUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDocument user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Set<String> capabilities = user.getRoleNames().stream()
                .map(roleRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .flatMap(role -> role.getCapabilities().stream())
                .collect(Collectors.toSet());

        Set<GrantedAuthority> authorities = capabilities.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());

        return User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(authorities)
                .accountLocked(false)
                .accountExpired(false)
                .credentialsExpired(false)
                .disabled(!user.isEnabled())
                .build();
    }
}
