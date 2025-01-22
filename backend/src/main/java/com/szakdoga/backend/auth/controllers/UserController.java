package com.szakdoga.backend.auth.controllers;

import com.szakdoga.backend.auth.dtos.*;
import com.szakdoga.backend.auth.model.*;
import com.szakdoga.backend.auth.services.EmailService;
import com.szakdoga.backend.auth.services.JwtService;
import com.szakdoga.backend.auth.services.UserService;
import com.szakdoga.backend.exceptions.ErrorResponse;
import com.szakdoga.backend.exceptions.InvalidCredentialsException;
import com.szakdoga.backend.exceptions.UserNotFoundException;
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

import java.util.Date;
import java.util.List;
import java.util.Map;
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
    public ResponseEntity<?> authenticate(@RequestBody LoginUserDto loginUserDto) {
        try {
            User authenticatedUser = userService.authenticate(loginUserDto);

            String jwtToken = jwtService.generateToken(authenticatedUser.getId());
            long expiresIn = jwtService.getTokenValidityInSeconds();

            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setFirstlogin(authenticatedUser.isFirstLogIn());
            loginResponse.setToken(jwtToken);
            loginResponse.setExpiresIn(expiresIn);
            return ResponseEntity.ok(loginResponse);
        } catch (InvalidCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Invalid credentials. Please check your ID and password."));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("An unexpected error occurred. Please try again later."));
        }
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

        private boolean firstlogin;

        public boolean isFirstlogin() {
            return firstlogin;
        }

        public void setFirstlogin(boolean firstlogin) {
            this.firstlogin = firstlogin;
        }

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
    public ResponseEntity<Map<String, Object>> saveUser(@RequestBody RegisterUserDto registerUserDto) {
        try {
            User user = userService.createUser(registerUserDto);
            Map<String, Object> response = Map.of(
                    "message", "User data received successfully!",
                    "userId", user.getId()
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = Map.of(
                    "error", "Error occurred while saving the user",
                    "message", e.getMessage()
            );

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @GetMapping(path = "/me/details", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDetailsDTO> getCurrentUserDetails() {
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

        List<String> phoneNumbers = user.getPhone_numbers()
                .stream()
                .map(phoneEntity -> phoneEntity.getPhone_number())
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
                phoneNumbers,
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
        log.info("User details retrieved successfully!" + userDTO.getId() + userDTO.getEmails());
        return ResponseEntity.ok(userDTO);
    }
    @GetMapping(path = "/me/first-login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> getFirstLoginStatus() {
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

        Boolean firstLogin = user.isFirstLogIn(); // Assuming there is a getter for 'firstLogin'
        return ResponseEntity.ok(firstLogin);
    }


    @PostMapping(path = "/me/change-password", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // Return 401 if not authenticated
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof UserDetails)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // Return 403 if unauthorized
        }

        UserDetails userDetails = (UserDetails) principal;
        String userId = userDetails.getUsername();

        // Verify the user exists
        User user = userService.findUserById(userId);
        if (user == null) {
            return ResponseEntity.notFound().build(); // Return 404 if user not found
        }

        // Validate and update the password
        if (!changePasswordDTO.getNewPassword().equals(changePasswordDTO.getConfirmPassword())) {
            return ResponseEntity.badRequest().build(); // Return 400 if passwords don't match
        }

        userService.changePassword(userId, changePasswordDTO.getNewPassword());
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/me/change-username", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> changeUsername(@RequestBody ChangeUsernameDTO changeUsernameDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // Return 401 if not authenticated
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof UserDetails)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // Return 403 if unauthorized
        }

        UserDetails userDetails = (UserDetails) principal;
        String userId = userDetails.getUsername();

        // Fetch the user entity from the database
        User user = userService.findUserById(userId);
        if (user == null) {
            return ResponseEntity.notFound().build(); // Return 404 if user not found
        }

        // Update the username
        userService.changeUsername(userId, changeUsernameDTO.getNewUsername());
        return ResponseEntity.ok().build();
    }
}
