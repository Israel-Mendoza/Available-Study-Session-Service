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

    // Finds all subjects associated with a specific user ID that are not archived.
    List<Subject> findByUserIdAndIsArchivedFalse(Integer userId);

    // Finds all subjects associated with a specific user ID that are archived.
    List<Subject> findByUserIdAndIsArchivedTrue(Integer userId);

    // Checks if a subject with the given ID exists.
    boolean existsById(int id);

    // Checks if a subject with the given name exists.
    boolean existsSubjectByUserIdAndName(Integer userId, String name);

    // Deletes a subject by its ID and returns true if the subject was deleted.
    Subject deleteById(int id);
}