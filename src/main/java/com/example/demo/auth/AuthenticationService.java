package com.example.demo.auth;

import com.example.demo.Mycoffe.Mycoffe;
import com.example.demo.Mycoffe.MycoffeRepository;
import com.example.demo.Mycoffe.Role;
import com.example.demo.Mycoffe.config.jwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * AuthenticationService handles both user registration and user login.
 *
 * Responsibilities:
 * - Create new users and save them to the database
 * - Encode user passwords before storing
 * - Authenticate existing users
 * - Generate JWT tokens during registration and login
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    /** Repository for interacting with the Mycoffe user table */
    private final MycoffeRepository repository;

    /** Encodes user passwords before saving to the database */
    private final PasswordEncoder passwordEncoder;

    /** Custom service for generating and validating JWT tokens */
    private final jwtService jwtService;

    /** Manager that performs Spring Security authentication */
    private final AuthenticationManager authenticationManager;

    /**
     * Register a new user into the system.
     *
     * Steps:
     * 1. Build a new Mycoffe user object from the request
     * 2. Encode the password
     * 3. Save the user into the database
     * 4. Generate a JWT token for the new user
     *
     * @param request the user's registration details
     * @return a response containing the generated JWT token
     */
    public AuthenticationResponse register(RegisterRequest request){
        // Build the user object using Lombok's builder
        var user = Mycoffe.builder()
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        // Save new user to the database
        repository.save(user);

        // Generate JWT token for authentication
        var jwtToken = jwtService.generateToken(user);

        // Return token as part of the response
        return AuthenticationResponse
                .builder()
                .token(jwtToken)
                .build();
    }

    /**
     * Authenticate a user using email and password.
     *
     * Steps:
     * 1. Spring Security checks username/password with AuthenticationManager
     * 2. Fetch user from database
     * 3. Generate a JWT token for the logged-in user
     *
     * @param request login credentials (email + password)
     * @return a response containing a valid JWT token
     */
    public AuthenticationResponse authenticate(AuthenticationRequest request){
        // Authenticate using Spring Security framework
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // Retrieve authenticated user
        var user = repository.findByEmail(request.getEmail()).orElseThrow();

        // Generate JWT token for the user
        var jwtToken = jwtService.generateToken(user);

        // Return token as part of the response
        return AuthenticationResponse
                .builder()
                .token(jwtToken)
                .build();
    }
}
