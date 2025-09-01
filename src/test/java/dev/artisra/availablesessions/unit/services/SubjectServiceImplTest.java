package dev.artisra.availablesessions.unit.services;

import dev.artisra.availablesessions.entities.Subject;
import dev.artisra.availablesessions.entities.Topic;
import dev.artisra.availablesessions.entities.User;
import dev.artisra.availablesessions.exceptions.custom.ExistingSubjectException;
import dev.artisra.availablesessions.exceptions.custom.SubjectNotFoundException;
import dev.artisra.availablesessions.mappers.SubjectMapper;
import dev.artisra.availablesessions.mappers.TopicMapper;
import dev.artisra.availablesessions.models.req.SubjectRequest;
import dev.artisra.availablesessions.models.res.SubjectResponse;
import dev.artisra.availablesessions.repositories.SubjectRepository;
import dev.artisra.availablesessions.repositories.UserRepository;
import dev.artisra.availablesessions.services.impl.SubjectServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@SpringBootTest
public class SubjectServiceImplTest {

    @MockitoBean
    private SubjectRepository subjectRepository;
    @MockitoBean
    private UserRepository userRepository;
    @MockitoBean
    private SubjectMapper subjectMapper;

    @Autowired
    private SubjectServiceImpl subjectService;
    @Autowired
    private TopicMapper topicMapper;

    @Test
    public void contextLoads() {
        assert subjectService != null;
        assert subjectRepository != null;
        assert userRepository != null;
    }

    @Test
    public void testCreateSubject_Success() {
        User user = new User();
        user.setId(1);

        // Mocking the repositories
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(subjectRepository.save(org.mockito.ArgumentMatchers.any())).thenAnswer(i -> {
            var subject = i.getArgument(0);
            // Simulate setting an ID upon saving
            try {
                var idField = subject.getClass().getDeclaredField("id");
                idField.setAccessible(true);
                idField.set(subject, 1); // Set a dummy ID
            } catch (Exception e) {
                e.printStackTrace();
            }
            return subject;
        });

        int subjectId = subjectService.createSubject(1, "Mathematics", "All about numbers");
        assert subjectId == 1; // Ensure a valid subject ID is returned
    }

    @Test
    public void testCreateSubject_NewUser() {
        // Mocking the repositories
        when(userRepository.findById(2)).thenReturn(Optional.empty());
        when(userRepository.save(org.mockito.ArgumentMatchers.any())).thenAnswer(i -> {
            var user = i.getArgument(0);
            // Simulate setting an ID upon saving
            try {
                var idField = user.getClass().getDeclaredField("id");
                idField.setAccessible(true);
                idField.set(user, 2); // Set a dummy ID
            } catch (Exception e) {
                e.printStackTrace();
            }
            return user;
        });
        when(subjectRepository.save(org.mockito.ArgumentMatchers.any())).thenAnswer(i -> {
            var subject = i.getArgument(0);
            // Simulate setting an ID upon saving
            try {
                var idField = subject.getClass().getDeclaredField("id");
                idField.setAccessible(true);
                idField.set(subject, 1); // Set a dummy ID
            } catch (Exception e) {
                e.printStackTrace();
            }
            return subject;
        });

        int subjectId = subjectService.createSubject(2, "Science", "All about science");
        assert subjectId == 1; // Ensure a valid subject ID is returned
    }

    @Test
    public void testCreateSubject_ExistingSubject() {
        // Setup
        int userId = 1;
        String subjectName = "Mathematics";
        String description = "All about numbers";

        User user = new User();
        user.setId(userId);

        Subject existingSubject = new Subject();
        existingSubject.setName(subjectName);
        existingSubject.setUser(user);

        // Mocking the behavior for the test
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(subjectRepository.findByUserId(userId)).thenReturn(List.of(existingSubject));

        // Act & Assert
        // Use assertThrows to check for the expected exception
        ExistingSubjectException thrown = assertThrows(
                ExistingSubjectException.class,
                () -> subjectService.createSubject(userId, subjectName, description)
        );

        // Verify the exception message
        assertTrue(thrown.getMessage().contains("Subject with name 'Mathematics' already exists for user ID 1"));

        // Verify that the save method was never called
        verify(subjectRepository, never()).save(any(Subject.class));
    }

    @Test
    public void testGetNonArchivedSubjectsByUserId_UserNotFound() {
        // Mocking the repositories
        when(userRepository.existsById(999)).thenReturn(false);

        try {
            subjectService.getNonArchivedSubjectsByUserId(999, false);
            assert false; // Should not reach here
        } catch (Exception e) {
            assert e.getMessage().contains("User with ID 999 does not exist.");
            // Verify that the save method was never called
            verify(subjectRepository, never()).save(any(Subject.class));
        }
    }

    @Test
    public void testGetSubjectById_SubjectNotFound() {
        // Mocking the repositories
        when(subjectRepository.findById(999)).thenReturn(Optional.empty());

        try {
            subjectService.getSubjectById(999, false);
            assert false; // Should not reach here
        } catch (Exception e) {
            assert e.getMessage().contains("Subject with ID 999 not found.");
            // Verify that the save method was never called
            verify(subjectRepository, never()).save(any(Subject.class));
        }
    }

    @Test
    public void testGetSubjectById_Success() {
        User user = new User();
        user.setId(1);

        Subject subject = new Subject();
        subject.setId(1);
        subject.setName("Mathematics");
        subject.setUser(user);
        subject.setDescription("All about numbers");

        // Mocking the repositories
        when(subjectRepository.findById(1)).thenReturn(Optional.of(subject));

        // Mocking the subjectMapper to convert Subject entity to a valid SubjectResponse
        when(subjectMapper.subjectToSubjectDTO(any(Subject.class)))
                .thenReturn(new SubjectResponse(1, 1, "Mathematics", "All about numbers", false));

        var subjectResponse = subjectService.getSubjectById(1, false);
        assert subjectResponse != null;
        assert subjectResponse.getSubjectId() == 1;
        assert subjectResponse.getName().equals("Mathematics");
        assert subjectResponse.getDescription().equals("All about numbers");
    }

    @Test
    public void testGetNonArchivedSubjectsByUserId_Success() {
        User user = new User();
        user.setId(1);

        Subject subject1 = new Subject();
        subject1.setId(1);
        subject1.setName("Mathematics");
        subject1.setUser(user);
        subject1.setArchived(false);

        Subject subject2 = new Subject();
        subject2.setId(2);
        subject2.setName("Science");
        subject2.setUser(user);
        subject2.setArchived(false);

        // Mocking the repositories
        when(userRepository.existsById(1)).thenReturn(true);
        when(subjectRepository.findByUserIdAndIsArchivedFalse(1)).thenReturn(List.of(subject1, subject2));

        // Mock the subjectMapper to convert each Subject entity to a valid SubjectResponse
        when(subjectMapper.subjectToSubjectDTO(any(Subject.class))).thenAnswer(invocation -> {
            Subject subject = invocation.getArgument(0);
            // Create and return a new SubjectResponse based on the Subject object
            return new SubjectResponse(subject.getId(), subject.getUser().getId(), subject.getName(), subject.getDescription(), subject.isArchived());
        });

        var subjects = subjectService.getNonArchivedSubjectsByUserId(1, false);
        assert subjects != null;
        assert subjects.size() == 2;
        assert subjects.stream().anyMatch(s -> s.getName().equals("Mathematics"));
        assert subjects.stream().anyMatch(s -> s.getName().equals("Science"));
    }

    @Test
    public void testGetArchivedSubjectsByUserId_Success() {
        User user = new User();
        user.setId(1);

        Subject subject1 = new Subject();
        subject1.setId(1);
        subject1.setName("History");
        subject1.setUser(user);
        subject1.setArchived(true);

        Subject subject2 = new Subject();
        subject2.setId(2);
        subject2.setName("Geography");
        subject2.setUser(user);
        subject2.setArchived(true);

        // Mocking the repositories
        when(userRepository.existsById(1)).thenReturn(true);
        when(subjectRepository.findByUserIdAndIsArchivedTrue(1)).thenReturn(List.of(subject1, subject2));

        // Mock the subjectMapper to convert each Subject entity to a valid SubjectResponse
        when(subjectMapper.subjectToSubjectDTO(any(Subject.class))).thenAnswer(invocation -> {
            Subject subject = invocation.getArgument(0);
            // Create and return a new SubjectResponse based on the Subject object
            return new SubjectResponse(subject.getId(), subject.getUser().getId(), subject.getName(), subject.getDescription(), subject.isArchived());
        });

        var subjects = subjectService.getArchivedSubjectsByUserId(1, false);
        assert subjects != null;
        assert subjects.size() == 2;
        assert subjects.stream().anyMatch(s -> s.getName().equals("History"));
        assert subjects.stream().anyMatch(s -> s.getName().equals("Geography"));
    }

    @Test
    public void testGetAllSubjectsByUserId_Success() {
        User user = new User();
        user.setId(1);

        Subject subject1 = new Subject();
        subject1.setId(1);
        subject1.setName("Mathematics");
        subject1.setUser(user);
        subject1.setArchived(false);

        Subject subject2 = new Subject();
        subject2.setId(2);
        subject2.setName("History");
        subject2.setUser(user);
        subject2.setArchived(true);

        // Mocking the repositories
        when(userRepository.existsById(1)).thenReturn(true);
        when(subjectRepository.findByUserId(1)).thenReturn(List.of(subject1, subject2));

        // Mock the subjectMapper to convert each Subject entity to a valid SubjectResponse
        when(subjectMapper.subjectToSubjectDTO(any(Subject.class))).thenAnswer(invocation -> {
            Subject subject = invocation.getArgument(0);
            // Create and return a new SubjectResponse based on the Subject object
            return new SubjectResponse(subject.getId(), subject.getUser().getId(), subject.getName(), subject.getDescription(), subject.isArchived());
        });

        var subjects = subjectService.getAllSubjectsByUserId(1, false);
        assert subjects != null;
        assert subjects.size() == 2;
        assert subjects.stream().anyMatch(s -> s.getName().equals("Mathematics"));
        assert subjects.stream().anyMatch(s -> s.getName().equals("History"));
    }

    @Test
    public void testUpdateSubject_SubjectNotFound() {
        // Setup
        int subjectId = 999;
        SubjectRequest subjectRequest = new SubjectRequest("NewName", "NewDescription");

        // Mocking the repositories
        when(subjectRepository.findById(subjectId)).thenReturn(Optional.empty());

        // Act & Assert
        // Use assertThrows to verify that SubjectNotFoundException is thrown
        SubjectNotFoundException thrown = assertThrows(
                SubjectNotFoundException.class,
                () -> subjectService.updateSubject(subjectId, subjectRequest)
        );

        // Verify the exception message
        assertTrue(thrown.getMessage().contains("Subject with ID " + subjectId + " not found."));

        // Verify that the save method was never called
        verify(subjectRepository, never()).save(any(Subject.class));
    }

    @Test
    public void testUpdateSubject_Success() {
        // Setup
        int subjectId = 1;
        SubjectRequest subjectRequest = new SubjectRequest("NewName", "NewDescription");

        Subject existingSubject = new Subject();
        existingSubject.setId(subjectId);
        existingSubject.setName("OldName");
        existingSubject.setDescription("OldDescription");

        // Mocking the repositories
        when(subjectRepository.findById(subjectId)).thenReturn(Optional.of(existingSubject));
        when(subjectRepository.save(any(Subject.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        subjectService.updateSubject(subjectId, subjectRequest);

        // Assert
        verify(subjectRepository, times(1)).save(existingSubject);
        assert existingSubject.getName().equals("NewName");
        assert existingSubject.getDescription().equals("NewDescription");
    }

    @Test
    public void testArchiveSubject_SubjectNotFound() {
        // Mocking the repositories
        when(subjectRepository.findById(999)).thenReturn(Optional.empty());

        try {
            subjectService.archiveSubject(999);
            assert false; // Should not reach here
        } catch (Exception e) {
            assert e.getMessage().contains("Subject with ID 999 not found.");
            // Verify that the save method was never called
            verify(subjectRepository, never()).save(any(Subject.class));
        }
    }

    @Test
    public void testArchiveSubject_Success() {
        Subject subject = new Subject();
        subject.setId(1);
        subject.setName("Mathematics");
        subject.setArchived(false);

        // Mocking the repositories
        when(subjectRepository.findById(1)).thenReturn(Optional.of(subject));
        when(subjectRepository.save(any(Subject.class))).thenAnswer(i -> i.getArgument(0));
        when(subjectMapper.subjectToSubjectDTO(any(Subject.class)))
                .thenReturn(new SubjectResponse(1, 1, "Mathematics", "", true));

        var subjectResponse = subjectService.archiveSubject(1);
        assert subjectResponse != null;
        assert subjectResponse.getSubjectId() == 1;
        assert subjectResponse.isArchived();
        verify(subjectRepository, times(1)).save(subject);
    }

    @Test
    public void testUnarchiveSubject_SubjectNotFound() {
        // Mocking the repositories
        when(subjectRepository.findById(999)).thenReturn(Optional.empty());

        try {
            subjectService.unarchiveSubject(999);
            assert false; // Should not reach here
        } catch (Exception e) {
            assert e.getMessage().contains("Subject with ID 999 not found.");
            // Verify that the save method was never called
            verify(subjectRepository, never()).save(any(Subject.class));
        }
    }

    @Test
    public void testUnarchiveSubject_Success() {
        Subject subject = new Subject();
        subject.setId(1);
        subject.setName("Mathematics");
        subject.setArchived(true);

        // Mocking the repositories
        when(subjectRepository.findById(1)).thenReturn(Optional.of(subject));
        when(subjectRepository.save(any(Subject.class))).thenAnswer(i -> i.getArgument(0));
        when(subjectMapper.subjectToSubjectDTO(any(Subject.class)))
                .thenReturn(new SubjectResponse(1, 1, "Mathematics", "", false));

        var subjectResponse = subjectService.unarchiveSubject(1);
        assert subjectResponse != null;
        assert subjectResponse.getSubjectId() == 1;
        assert !subjectResponse.isArchived();
        verify(subjectRepository, times(1)).save(subject);
    }

    @Test
    public void testSubjectContainsNoTopicsByDefault() {
        User user = new User();
        user.setId(1);

        Topic topic1 = new Topic();
        Subject subject = new Subject();

        topic1.setId(1);
        topic1.setName("Algebra");
        topic1.setSubject(subject);

        subject.setId(1);
        subject.setName("Mathematics");
        subject.setUser(user);
        subject.setTopics(Set.of(topic1));

        // Mocking the repositories
        when(subjectRepository.findById(1)).thenReturn(Optional.of(subject));
        when(subjectMapper.subjectToSubjectDTO(any(Subject.class)))
                .thenReturn(new SubjectResponse(1, 1, "Mathematics", "", false));

        var subjectResponse = subjectService.getSubjectById(1, false);
        assert subjectResponse != null;
        assert subjectResponse.getSubjectId() == 1;
        assert subjectResponse.getName().equals("Mathematics");
        assert subjectResponse.getTopics() == null;
    }

    @Test
    public void testSubjectContainsTopicsWhenRequested() {
        User user = new User();
        user.setId(1);

        Topic topic1 = new Topic();
        Subject subject = new Subject();

        topic1.setId(1);
        topic1.setName("Algebra");
        topic1.setSubject(subject);

        subject.setId(1);
        subject.setName("Mathematics");
        subject.setUser(user);
        subject.setTopics(Set.of(topic1));

        // Mocking the SubjectResponse to include topics
        SubjectResponse subjectResponseWithTopics = new SubjectResponse(1, 1, "Mathematics", "", false);
        subjectResponseWithTopics.setTopics(List.of(topicMapper.topicToTopicDTO(topic1))); // Add topic responses

        // Mocking the repositories
        when(subjectRepository.findById(1)).thenReturn(Optional.of(subject));
        when(subjectMapper.subjectToSubjectDTO(any(Subject.class)))
                .thenReturn(subjectResponseWithTopics);

        var subjectResponse = subjectService.getSubjectById(1, true);
        assert subjectResponse != null;
        assert subjectResponse.getSubjectId() == 1;
        assert subjectResponse.getName().equals("Mathematics");
        assert subjectResponse.getTopics() != null;
        assert !subjectResponse.getTopics().isEmpty();
    }
}
