
package com.example.demo.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for handling authentication-related operations.
 * Provides endpoints for user registration and authentication.
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    /**
     * Service responsible for authentication and registration logic.
     */
    private final AuthenticationService service;

    /**
     * Registers a new user account.
     *
     * @param request the registration request containing user details.
     * @return a {@link ResponseEntity} containing the {@link AuthenticationResponse}.
     */
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(service.register(request));
    }

    /**
     * Authenticates an existing user.
     *
     * @param request the authentication request containing login credentials.
     * @return a {@link ResponseEntity} containing the {@link AuthenticationResponse}.
     */
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }
}
