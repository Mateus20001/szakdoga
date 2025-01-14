package com.szakdoga.backend.auth.controllers;

import com.szakdoga.backend.auth.dtos.LoginUserDto;
import com.szakdoga.backend.auth.dtos.RegisterUserDto;
import com.szakdoga.backend.auth.dtos.UserDetailsDTO;
import com.szakdoga.backend.auth.dtos.UserNameAndRolesDTO;
import com.szakdoga.backend.auth.model.*;
import com.szakdoga.backend.auth.services.JwtService;
import com.szakdoga.backend.auth.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.stream.Collectors;



@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {
    private UserService userService;

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final JwtService jwtService;
    @Autowired
    public UserController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) {
        User authenticatedUser = userService.authenticate(loginUserDto);

        String jwtToken = jwtService.generateToken(authenticatedUser.getId());

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwtToken);

        return ResponseEntity.ok(loginResponse);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAllUsers();
        return ResponseEntity.ok(users);  // Return the list of users with HTTP status 200 OK
    }

    // Get a single user by ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        User user = userService.findUserById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();  // Return 404 if user not found
        }
        return ResponseEntity.ok(user);  // Return the user with HTTP status 200 OK
    }

    // Create a new user
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.status(201).body(createdUser);  // Return 201 Created status
    }

    // Update an existing user
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        User updatedUser = userService.updateUser(user);
        if (updatedUser == null) {
            return ResponseEntity.notFound().build();  // Return 404 if user not found
        }
        return ResponseEntity.ok(updatedUser);  // Return updated user with HTTP status 200 OK
    }

    // Delete a user by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        boolean deleted = userService.deleteUserById(id);
        if (!deleted) {
            return ResponseEntity.notFound().build();  // Return 404 if user not found
        }
        return ResponseEntity.noContent().build();  // Return 204 No Content status after successful deletion
    }

    public class LoginResponse {
        private String token;

        private long expiresIn;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public long getExpiresIn() {
            return expiresIn;
        }

        public void setExpiresIn(long expiresIn) {
            this.expiresIn = expiresIn;
        }
    }
    @GetMapping("/me")
    public UserNameAndRolesDTO getUserNameAndRoles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User is not authenticated");
        }

        Object principal = authentication.getPrincipal();

        String username;
        List<String> roles;

        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            username = userDetails.getUsername();
            roles = userDetails.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());
        } else {
            throw new RuntimeException("Unable to retrieve user details");
        }

        // Return the username and roles as a response
        return new UserNameAndRolesDTO(username, roles);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/save")
    public ResponseEntity<String> saveUser(@RequestBody RegisterUserDto registerUserDto) {

        User user = userService.createUser(registerUserDto);

        // Return a response
        return ResponseEntity.ok("User data received successfully!" + user);
    }

    @GetMapping(path = "/me/details", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDetailsDTO> getCurrentUserDetails() {
        // Retrieve the authentication object
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // Return 401 if not authenticated
        }

        // Extract principal (user details)
        Object principal = authentication.getPrincipal();

        if (!(principal instanceof UserDetails)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // Return 403 if principal is not a valid UserDetails instance
        }

        UserDetails userDetails = (UserDetails) principal;
        String userId = userDetails.getUsername(); // Assuming the username is the user ID

        // Fetch the user entity from the database
        User user = userService.findUserById(userId);
        if (user == null) {
            return ResponseEntity.notFound().build(); // Return 404 if user not found
        }
        List<String> majorNames = user.getMajors()
                .stream()
                .map(majorEntity -> majorEntity.getMajor().getName())
                .collect(Collectors.toList());

        List<String> facultyNames = user.getMajors()
                .stream()
                .map(majorEntity -> majorEntity.getMajor().getFaculty().getName())
                .distinct() // To avoid duplicates if majors belong to the same faculty
                .collect(Collectors.toList());

        // Map User to UserDTO
        UserDetailsDTO userDTO = new UserDetailsDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getName(),
                user.getRoles().stream().map(RoleEntity::getRoleName).collect(Collectors.toList()),
                user.getEmails().stream().map(EmailEntity::getEmail).collect(Collectors.toList()),
                majorNames,
                facultyNames,
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
        log.info("User details retrieved successfully!" + userDTO.getId() + userDTO.getEmails());
        return ResponseEntity.ok(userDTO);
    }


}
