package dev.artisra.availablesessions.repositories;

import dev.artisra.availablesessions.entities.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Integer> {

    // Finds a subject by its name.
    Optional<Subject> findSubjectByName(String name);

    // Finds a subject by its ID.
    Optional<Subject> findSubjectById(int id);

    // Finds all subjects associated with a specific user ID.
    List<Subject> findByUserId(Integer userId);

    // Deletes a subject by its ID.
    boolean existsById(int id);

    // Checks if a subject with the given name exists.
    boolean existsSubjectByName(String name);

    // Deletes a subject by its ID.
    boolean deleteById(int id);
}