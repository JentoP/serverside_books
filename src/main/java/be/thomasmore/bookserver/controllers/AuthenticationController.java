package be.thomasmore.bookserver.controllers;

import be.thomasmore.bookserver.model.User;
import be.thomasmore.bookserver.model.dto.AuthenticationDTO;
import be.thomasmore.bookserver.model.dto.UserDTO;
import be.thomasmore.bookserver.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.Optional;

/**
 * Controller for handling user authentication and registration.
 * Provides endpoints for user signup, login, and authentication status.
 * All endpoints are prefixed with "/api".
 */
@RestController
@RequestMapping("/api")
@Slf4j
public class AuthenticationController {
    /**
     * Repository for accessing user data in the database.
     */
    @Autowired
    UserRepository userRepository;

    /**
     * Used for encoding passwords when creating new users.
     */
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Handles the authentication process.
     */
    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * Checks if the current user is authenticated and returns their username.
     *
     * @param principal The currently authenticated user
     * @return AuthenticationDTO containing the username or "anonymous" if not authenticated
     */
    @GetMapping("/authenticate")
    public AuthenticationDTO authenticate(Principal principal) {
        log.info("##### authenticate");
        return new AuthenticationDTO(principal != null ? principal.getName() : "anonymous");
    }

    /**
     * Registers a new user in the system.
     * Validates that the username is not already taken and creates a new user with hashed password.
     *
     * @param userDTO The user data for registration
     * @return AuthenticationDTO containing the newly created username
     * @throws ResponseStatusException if the username already exists
     */
    @PostMapping("/signup")
    public AuthenticationDTO signup(@RequestBody UserDTO userDTO) {
        log.info("##### signup " + userDTO.getUsername());
        Optional<User> optionalUser = userRepository.findByUsername(userDTO.getUsername());
        if (optionalUser.isPresent())
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    String.format("User with name %s already exists.", userDTO.getUsername()));

        User newUser = new User();
        newUser.setUsername(userDTO.getUsername());
        newUser.setRole("USER");
        String encode = passwordEncoder.encode(userDTO.getPassword());
        newUser.setPassword(encode);
        User newSavedUser = userRepository.save(newUser);

        autologin(userDTO.getUsername(), userDTO.getPassword());

        return new AuthenticationDTO(newSavedUser.getUsername());
    }

    private void autologin(String userName, String password) {
        UsernamePasswordAuthenticationToken token
                = new UsernamePasswordAuthenticationToken(userName, password);

        try {
            Authentication auth = authenticationManager.authenticate(token);
            log.info("authentication: " + auth.isAuthenticated());

            SecurityContext sc = SecurityContextHolder.getContext();
            sc.setAuthentication(auth);
        } catch (AuthenticationException e) {
            e.printStackTrace();
        }
    }
}
