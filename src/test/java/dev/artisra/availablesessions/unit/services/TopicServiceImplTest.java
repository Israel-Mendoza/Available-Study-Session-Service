package dev.artisra.availablesessions.unit.services;

import dev.artisra.availablesessions.entities.Subject;
import dev.artisra.availablesessions.entities.Topic;
import dev.artisra.availablesessions.repositories.SubjectRepository;
import dev.artisra.availablesessions.repositories.TopicRepository;
import dev.artisra.availablesessions.services.impl.TopicServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@SpringBootTest
public class TopicServiceImplTest {

    @MockitoBean
    private TopicRepository topicRepository;
    @MockitoBean
    private SubjectRepository subjectRepository;

    @Autowired
    private TopicServiceImpl topicService;

    @Test
    public void contextLoads() {
        assert topicService != null;
        assert topicRepository != null;
    }

    @Test
    public void testAddTopicToSubject_Success() {

        Subject subject = new Subject();
        subject.setId(1);
        subject.setName("Mathematics");

        // Mocking the repositories
        when(subjectRepository.findById(1)).thenReturn(Optional.of(subject));
        when(topicRepository.findBySubjectId(1)).thenReturn(List.of());
        when(topicRepository.save(org.mockito.ArgumentMatchers.any())).thenAnswer(i -> {
            var topic = i.getArgument(0);
            // Simulate setting an ID upon saving
            try {
                var idField = topic.getClass().getDeclaredField("id");
                idField.setAccessible(true);
                idField.set(topic, 1); // Set a dummy ID
            } catch (Exception e) {
                e.printStackTrace();
            }
            return topic;
        });

        int topicId = topicService.addTopicToSubject(1, "Algebra", "Basic Algebra concepts");
        assert topicId == 1; // Ensure a valid topic ID is returned
    }

    @Test
    public void testAddTopicToSubject_SubjectNotFound() {
        // Mocking the repositories
        when(subjectRepository.findById(999)).thenReturn(Optional.empty());

        try {
            topicService.addTopicToSubject(999, "Algebra", "Basic Algebra concepts");
            assert false; // Should not reach here
        } catch (Exception e) {
            assert e.getMessage().contains("Subject with ID 999 not found.");
        }
    }

    @Test
    public void testAddTopicToSubject_ExistingTopic() {

        Subject subject = new Subject();
        subject.setId(1);
        subject.setName("Mathematics");

        var existingTopic = new Topic();
        existingTopic.setId(1);
        existingTopic.setName("Algebra");
        existingTopic.setDescription("Basic Algebra concepts");
        existingTopic.setSubject(subject);

        // Mocking the repositories
        when(subjectRepository.findById(1)).thenReturn(Optional.of(subject));
        when(topicRepository.findBySubjectId(1)).thenReturn(List.of(existingTopic));

        try {
            topicService.addTopicToSubject(1, "Algebra", "Another description");
            assert false; // Should not reach here
        } catch (Exception e) {
            assert e.getMessage().contains("Topic 'Algebra' already exists for subject ID 1");
        }
    }

    @Test
    public void testGetTopicById_Success() {
        Subject subject = new Subject();
        subject.setId(1);
        subject.setName("Mathematics");

        Topic topic = new Topic();
        topic.setId(1);
        topic.setName("Algebra");
        topic.setDescription("Basic Algebra concepts");
        topic.setSubject(subject);

        // Mocking the repository
        when(topicRepository.findById(1)).thenReturn(Optional.of(topic));

        var topicResponse = topicService.getTopicById(1);
        assert topicResponse != null;
        assert topicResponse.getTopicId() == 1;
        assert topicResponse.getName().equals("Algebra");
    }

    @Test
    public void testGetTopicById_NotFound() {
        // Mocking the repository
        when(topicRepository.findById(999)).thenReturn(Optional.empty());

        try {
            topicService.getTopicById(999);
            assert false; // Should not reach here
        } catch (Exception e) {
            assert e.getMessage().contains("Topic with ID 999 not found.");
        }
    }

    @Test
    public void testGetAllTopicsForSubject_Success() {
        Subject subject = new Subject();
        subject.setId(1);
        subject.setName("Mathematics");

        Topic topic1 = new Topic();
        topic1.setId(1);
        topic1.setName("Algebra");
        topic1.setDescription("Basic Algebra concepts");
        topic1.setSubject(subject);

        Topic topic2 = new Topic();
        topic2.setId(2);
        topic2.setName("Geometry");
        topic2.setDescription("Basic Geometry concepts");
        topic2.setSubject(subject);

        // Mocking the repositories
        when(subjectRepository.findById(1)).thenReturn(Optional.of(subject));
        when(topicRepository.findBySubjectId(1)).thenReturn(List.of(topic1, topic2));

        var topics = topicService.getAllTopicsForSubject(1);
        assert topics.size() == 2;
        assert topics.stream().anyMatch(t -> t.getName().equals("Algebra"));
        assert topics.stream().anyMatch(t -> t.getName().equals("Geometry"));
    }

    @Test
    public void testGetAllTopicsForSubject_SubjectNotFound() {
        // Mocking the repository
        when(subjectRepository.findById(999)).thenReturn(Optional.empty());

        try {
            topicService.getAllTopicsForSubject(999);
            assert false; // Should not reach here
        } catch (Exception e) {
            assert e.getMessage().contains("Subject with ID 999 not found.");
        }
    }

    @Test
    public void testTopicExists() {
        // Mocking the repository
        when(topicRepository.existsById(1)).thenReturn(true);
        when(topicRepository.existsById(999)).thenReturn(false);

        assert topicService.topicExists(1);
        assert !topicService.topicExists(999);
    }

    @Test
    public void testUpdateTopic_Success() {
        Subject subject = new Subject();
        subject.setId(1);
        subject.setName("Mathematics");

        Topic topic = new Topic();
        topic.setId(1);
        topic.setName("Algebra");
        topic.setDescription("Basic Algebra concepts");
        topic.setSubject(subject);

        // Mocking the repository
        when(topicRepository.findById(1)).thenReturn(Optional.of(topic));
        when(topicRepository.save(org.mockito.ArgumentMatchers.any())).thenAnswer(i -> i.getArgument(0));

        var topicRequest = new dev.artisra.availablesessions.models.req.TopicRequest();
        topicRequest.setTopic("Advanced Algebra");
        topicRequest.setDescription("Advanced Algebra concepts");

        topicService.updateTopic(1, topicRequest);

        assert topic.getName().equals("Advanced Algebra");
        assert topic.getDescription().equals("Advanced Algebra concepts");
    }

    @Test
    public void testDeleteTopic() {
        // Just ensure no exceptions are thrown
        topicService.deleteTopic(1);
    }
}
