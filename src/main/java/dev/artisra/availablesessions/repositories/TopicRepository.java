package dev.artisra.availablesessions.repositories;

import dev.artisra.availablesessions.entities.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// TopicRepository.java
@Repository
public interface TopicRepository extends JpaRepository<Topic, Integer> {
    // Finds a topic by its ID.
    Optional<Topic> findTopicById(int id);

    // Finds all topics associated with a specific subject ID.
    List<Topic> findBySubjectId(Integer subjectId);

    // Deletes a topic by its ID.
    boolean deleteById(int id);

    // Deletes all topics associated with a specific subject ID.
    boolean deleteBySubjectId(int subjectId);
}
