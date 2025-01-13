package com.szakdoga.backend.auth.services;
import com.szakdoga.backend.auth.dtos.LoginUserDto;
import com.szakdoga.backend.auth.dtos.RegisterUserDto;
import com.szakdoga.backend.auth.model.User;
import com.szakdoga.backend.auth.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;
    private PasswordEncoder passwordEncoder;

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }
    public User signup(RegisterUserDto input) {
        User user = new User();
        user.setName(input.getName());
        user.setFirstName(input.getFirstName());
        user.setPassword(passwordEncoder.encode(input.getPassword()));
        user.setLastName(input.getLastName());
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    public User authenticate(LoginUserDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getId(),
                        input.getPassword()
                )
        );

        return userRepository.findById(input.getId())
                .orElseThrow();
    }
    public List<User> findAllUsers() {
        log.info("Fetching all users...");
        return userRepository.findAll();
    }

    public User findUserById(String id) {
        log.info("Fetching user with ID: {}", id);
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found!"));
    }

    public User createUser(User user) {
        log.info("Creating a new user...");
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User updateUser(User user) {
        if (!userRepository.existsById(user.getId())) {
            return null;  // User not found
        }
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    public boolean deleteUserById(String id) {
        if (!userRepository.existsById(id)) {
            return false;
        }
        userRepository.deleteById(id);
        return true;
    }
    public boolean verifyPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
