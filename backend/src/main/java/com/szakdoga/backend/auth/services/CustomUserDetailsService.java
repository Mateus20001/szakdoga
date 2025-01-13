package com.szakdoga.backend.auth.services;

import com.szakdoga.backend.auth.model.User;
import com.szakdoga.backend.auth.repositories.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        // Fetch user by identifier (e.g., email, username, etc.)
        User user = userRepository.findById(identifier)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with identifier: " + identifier));

        // Convert roles (strings) to GrantedAuthority objects
        Collection<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName())) // Convert each role to SimpleGrantedAuthority
                .collect(Collectors.toList());

        // Return a Spring Security UserDetails object
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getId()) // Ensure this is non-empty
                .password(user.getPassword())       // Password (hashed)
                .authorities(authorities)           // Roles/authorities
                .build();
    }
}