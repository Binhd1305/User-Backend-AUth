
package com.example.demo.Mycoffe.config;

import com.example.demo.Mycoffe.MycoffeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Application security configuration class.
 * Defines beans for authentication and password encoding.
 */
@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    /**
     * Repository for accessing user data.
     */
    private final MycoffeRepository repository;

    /**
     * Provides a custom {@link UserDetailsService} implementation.
     * Loads user details by email from the database.
     *
     * @return a {@link UserDetailsService} instance.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> repository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    /**
     * Configures the {@link AuthenticationProvider} using {@link DaoAuthenticationProvider}.
     * Sets the custom {@link UserDetailsService} and {@link PasswordEncoder}.
     *
     * @return an {@link AuthenticationProvider} instance.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Provides the {@link AuthenticationManager} bean.
     *
     * @param config the {@link AuthenticationConfiguration} provided by Spring.
     * @return an {@link AuthenticationManager} instance.
     * @throws Exception if authentication manager cannot be created.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Defines a {@link PasswordEncoder} bean using {@link BCryptPasswordEncoder}.
     *
     * @return a {@link PasswordEncoder} instance.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
