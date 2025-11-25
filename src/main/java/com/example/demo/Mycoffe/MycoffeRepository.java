package com.example.demo.Mycoffe;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Repository interface for performing CRUD operations on the Mycoffe entity.
 *
 * <p>This interface extends JpaRepository, which provides built-in methods such as:
 * save(), findAll(), findById(), deleteById(), etc.
 *
 * <p>The generic types specify:
 * - Mycoffe: the entity class mapped to the database table
 * - Integer: the type of the primary key (ID)
 */
public interface MycoffeRepository extends JpaRepository<Mycoffe, Integer> {

    /**
     * Custom query method used to retrieve a Mycoffe entity by its email.
     *
     * <p>Spring Data JPA automatically generates the SQL query for this method
     * based on its name, equivalent to:
     * <pre>SELECT * FROM mycoffe WHERE email = ?</pre>
     *
     * @param email the email to search for
     * @return an Optional containing the Mycoffe entity if found,
     *         or an empty Optional if no matching record exists
     */
    Optional<Mycoffe> findByEmail(String email); // find user by email
}