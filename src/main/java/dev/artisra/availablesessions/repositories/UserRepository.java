package dev.artisra.availablesessions.repositories;

import dev.artisra.availablesessions.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// UserRepository.java
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    // Finds a user by its ID.
    Optional<User> findUserById(int id);

    // Checks if a user with the given ID exists.
    boolean existsById(int id);

    // Deletes a user by its ID.
    void deleteById(int id);
}
