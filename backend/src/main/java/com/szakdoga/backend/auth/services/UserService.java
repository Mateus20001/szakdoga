package com.szakdoga.backend.auth.services;
import com.szakdoga.backend.auth.PasswordGenerator;
import com.szakdoga.backend.auth.dtos.LoginUserDto;
import com.szakdoga.backend.auth.dtos.RegisterUserDto;
import com.szakdoga.backend.auth.model.*;
import com.szakdoga.backend.auth.repositories.*;
import com.szakdoga.backend.exceptions.InvalidCredentialsException;
import com.szakdoga.backend.exceptions.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final EmailRepository emailRepository;
    private final MajorRepository majorRepository;
    private final MajorDetailsRepository majorDetailsRepository;
    private final PhoneRepository phoneRepository;
    private final AuthenticationManager authenticationManager;
    private PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    @Autowired
    private EmailService emailService;
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository, EmailRepository emailRepository, MajorRepository majorRepository, MajorDetailsRepository majorDetailsRepository, PhoneRepository phoneRepository, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.emailRepository = emailRepository;
        this.majorRepository = majorRepository;
        this.majorDetailsRepository = majorDetailsRepository;
        this.phoneRepository = phoneRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }
    @PreAuthorize("hasRole('ADMIN')")
    public User createUser(RegisterUserDto input) {
        User user = new User();
        user.setFirstName(input.getFirstName());
        String randomPassword = PasswordGenerator.generateRandomPassword(12);
        user.setPassword(passwordEncoder.encode(randomPassword));
        user.setLastName(input.getLastName());
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        if (input.getEmails().isEmpty()) {
            throw new RuntimeException("No email provided!");
        }
        if (input.getEmails().size() > 4) {
            throw new RuntimeException("Too many emails provided!");
        }
        List<EmailEntity> emailEntities = input.getEmails().stream()
                .map(email -> new EmailEntity(user, email)) // Create EmailEntity instances
                .collect(Collectors.toList());
        Optional<EmailEntity> existingEmail = emailRepository.findByEmail(emailEntities.getFirst().getEmail());
        if (existingEmail.isPresent()) {
            throw new RuntimeException("Email already exists in the database!");
        }
        List<MajorEntity> majorEntities = input.getMajors().stream()
                .map(major -> new MajorEntity(user, Integer.parseInt(major), majorDetailsRepository)) // Create EmailEntity instances
                .collect(Collectors.toList());
        List<RoleEntity> roleEntities = input.getRoles().stream()
                .map(roleEntity -> new RoleEntity(user, roleEntity)) // Create EmailEntity instances
                .collect(Collectors.toList());
        List<PhoneEntity> phone_numbers = input.getPhone_numbers().stream()
                .map(phoneEntity -> new PhoneEntity(user, phoneEntity)) // Create EmailEntity instances
                .collect(Collectors.toList());
        user.setPhone_numbers(phone_numbers);
        user.setMajors(majorEntities);
        user.setEmails(emailEntities);
        user.setRoles(roleEntities);
        userRepository.save(user);
        if (!input.getPhone_numbers().isEmpty()) {
            phoneRepository.saveAll(phone_numbers);
        }
        if (!roleEntities.isEmpty()) {
            roleRepository.saveAll(roleEntities);
        }
        if (!majorEntities.isEmpty()) {
            majorRepository.saveAll(majorEntities);
        }
        emailRepository.saveAll(emailEntities);
        EmailEntity firstEmail = emailEntities.getFirst();
        emailService.sendEmail(firstEmail.getEmail(), user.getId(), randomPassword);
        return user;
    }

    public User authenticate(LoginUserDto input) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            input.getId(),
                            input.getPassword()
                    )
            );

            return userRepository.findById(input.getId())
                    .orElseThrow(() -> new UserNotFoundException("User with ID " + input.getId() + " not found."));
        } catch (AuthenticationException e) {
            throw new InvalidCredentialsException("Invalid ID or password.", e);
        }
    }
    public List<User> findAllUsers() {
        log.info("Fetching all users...");
        return userRepository.findAll();
    }

    public User findUserById(String id) {
        log.info("Fetching user with ID: {}", id);
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found!"));
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
