package dev.artisra.availablesessions.unit.repositories;

import dev.artisra.availablesessions.repositories.TopicRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@ActiveProfiles("unit")
@Sql(scripts = {"/sql/unit/create_schema.sql", "/sql/unit/insert_data.sql"})
@Sql(scripts = {"/sql/unit/cleanup.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class TopicRepositoryTest {

    @Autowired
    private TopicRepository topicRepository;

    @Test
    public void testFindTopicById_ExistingTopic() {
        var topic = topicRepository.findTopicById(1);
        assert topic.isPresent();
        assert topic.get().getName().equals("Algebra");
    }

    @Test
    public void testFindTopicById_NonExistingTopic() {
        var topic = topicRepository.findTopicById(999);
        assert topic.isEmpty();
    }

    @Test
    public void testFindBySubjectId_ExistingSubject() {
        var topics = topicRepository.findBySubjectId(1);
        assert topics.size() == 3; // Assuming subject with ID 1 has 2 topics
    }

    @Test
    public void testFindBySubjectId_NonExistingSubject() {
        var topics = topicRepository.findBySubjectId(999);
        assert topics.isEmpty();
    }

    @Test
    public void testDeleteById_ExistingTopic() {
        // Confirm topic exists
        var topic = topicRepository.findTopicById(1);
        assert topic.isPresent();

        // Delete the topic

        // Confirm topic is deleted
        topicRepository.deleteById(1);
        topic = topicRepository.findTopicById(1);
        assert topic.isEmpty();
    }

    @Test
    public void testDeleteById_NonExistingTopic() {
        // Attempt to delete a non-existing topic
        try {
            topicRepository.deleteById(999);
        } catch (Exception e) {
            assert false : "Exception should not be thrown when deleting a non-existing topic.";
        }
    }

    @Test
    public void testDeleteBySubjectId_ExistingSubject() {
        // Confirm topics exist for subject
        var topics = topicRepository.findBySubjectId(1);
        assert !topics.isEmpty();

        // Delete topics by subject ID
        topicRepository.deleteBySubjectId(1);

        // Confirm topics are deleted
        topics = topicRepository.findBySubjectId(1);
        assert topics.isEmpty();
    }

    @Test
    public void testDeleteBySubjectId_NonExistingSubject() {
        // Attempt to delete topics for a non-existing subject
        try {
            topicRepository.deleteBySubjectId(999);
        } catch (Exception e) {
            assert false : "Exception should not be thrown when deleting topics for a non-existing subject.";
        }
    }
}
