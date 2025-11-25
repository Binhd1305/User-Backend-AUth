
package com.example.demo.Mycoffe;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * Represents a customer entity in the system.
 * Implements {@link UserDetails} for Spring Security authentication and authorization.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "my_coffe")
public class Mycoffe implements UserDetails {

    /**
     * Primary key for the customer entity.
     * Auto-generated using {@link GenerationType#AUTO}.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "customer_id")
    private Integer customerId;

    /** Customer's first name. */
    @Column(name = "first_name")
    private String firstName;

    /** Customer's last name. */
    @Column(name = "last_name")
    private String lastName;

    /** Customer's email address, used as username for authentication. */
    private String email;

    /** Customer's phone number. */
    private Integer phone;

    /** Encrypted password for authentication. */
    private String password;

    /**
     * Timestamp when the record was created.
     * Automatically set when the entity is persisted.
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Role of the customer (e.g., ADMIN, USER).
     * Stored as a string in the database.
     */
    @Enumerated(EnumType.STRING)
    private Role role;

    /**
     * Returns the authorities granted to the user based on their role.
     *
     * @return a collection of {@link GrantedAuthority} objects.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    /**
     * Returns the user's password.
     *
     * @return the password.
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Returns the user's email as the username.
     *
     * @return the email.
     */
    @Override
    public String getUsername() {
        return email;
    }

    /**
     * Indicates whether the user's account has expired.
     *
     * @return true (account never expires).
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user's account is locked.
     *
     * @return true (account never locked).
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indicates whether the user's credentials have expired.
     *
     * @return true (credentials never expire).
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is enabled.
     *
     * @return true (account always enabled).
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
