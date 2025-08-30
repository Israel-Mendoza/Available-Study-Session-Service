package dev.artisra.availablesessions.services.impl;

import dev.artisra.availablesessions.entities.Subject;
import dev.artisra.availablesessions.entities.User;
import dev.artisra.availablesessions.exceptions.custom.ExistingSubjectException;
import dev.artisra.availablesessions.exceptions.custom.SubjectNotFoundException;
import dev.artisra.availablesessions.exceptions.custom.UserNotFoundException;
import dev.artisra.availablesessions.models.SubjectDTO;
import dev.artisra.availablesessions.repositories.SubjectRepository;
import dev.artisra.availablesessions.repositories.TopicRepository;
import dev.artisra.availablesessions.repositories.UserRepository;
import dev.artisra.availablesessions.services.interfaces.SubjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubjectServiceImpl implements SubjectService {

    private static final Logger logger = LoggerFactory.getLogger(SubjectServiceImpl.class);

    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;

    public SubjectServiceImpl(@Autowired UserRepository userRepository, @Autowired SubjectRepository subjectRepository, @Autowired TopicRepository topicRepository) {
        this.userRepository = userRepository;
        this.subjectRepository = subjectRepository;
    }

    @Override
    public int createSubject(int userId, String subjectName, String description) {
        logger.info("Creating subject '{}' for user ID {}", subjectName, userId);
        if (getSubjectByName(userId, subjectName).isPresent()) {
            logger.warn("Subject '{}' already exists for user ID {}", subjectName, userId);
            throw new ExistingSubjectException("Subject with name '" + subjectName + "' already exists for user ID " + userId + ".");
        }

        Optional<User> userOpt = userRepository.findById(userId);
        User user;
        // Creating a new user if not exists
        if (userOpt.isEmpty()) {
            user = new User();
            user.setId(userId);
            user = userRepository.save(user);
        } else {
            user = userOpt.get();
        }

        Subject newSubject = new Subject();
        newSubject.setName(subjectName);
        newSubject.setDescription(description);
        newSubject.setUser(user);

        logger.info("Subject '{}' created with ID {} for user ID {}", subjectName, newSubject.getId(), userId);
        return subjectRepository.save(newSubject).getId();
    }

    @Override
    public SubjectDTO getSubjectById(int subjectId) {
        logger.info("Fetching subject with ID {}", subjectId);
        Optional<Subject> subjectOpt = subjectRepository.findById(subjectId);
        if (subjectOpt.isPresent()) {
            Subject subject = subjectOpt.get();
            logger.info("Subject with ID {} found: '{}'", subjectId, subject.getName());
            return new SubjectDTO(subject.getUser().getId(), subject.getId(), subject.getName(), subject.getDescription(), subject.isArchived());
        }
        return null;
    }

    @Override
    public List<SubjectDTO> getAllSubjectsForUser(int userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User with ID " + userId + " does not exist.");
        }

        // Using the repository to fetch subjects directly
        return subjectRepository.findByUserId(userId)
                .stream()
                .map(subject -> new SubjectDTO(subject.getUser().getId(), subject.getId(), subject.getName(), subject.getDescription(), subject.isArchived()))
                .toList();
    }

    @Override
    public SubjectDTO archiveSubject(int subjectId) {
        logger.info("Archiving subject with ID {}", subjectId);
        Optional<Subject> subjectOpt = subjectRepository.findById(subjectId);
        if (subjectOpt.isPresent()) {
            logger.info("Subject with ID {} found. Archiving...", subjectId);
            Subject subject = subjectOpt.get();
            subject.setArchived(true);
            subjectRepository.save(subject);

            return new SubjectDTO(subject.getUser().getId(), subject.getId(), subject.getName(), subject.getDescription(), subject.isArchived());
        }
        logger.warn("Subject with ID {} not found. Cannot archive.", subjectId);
        throw new SubjectNotFoundException("Subject with ID " + subjectId + " not found.");
    }

    @Override
    public SubjectDTO unarchiveSubject(int subjectId) {
        logger.info("Unarchiving subject with ID {}", subjectId);
        Optional<Subject> subjectOpt = subjectRepository.findById(subjectId);
        if (subjectOpt.isPresent()) {
            logger.info("Subject with ID {} found. Unarchiving...", subjectId);
            Subject subject = subjectOpt.get();
            subject.setArchived(false);
            subjectRepository.save(subject);
            return new SubjectDTO(subject.getUser().getId(), subject.getId(), subject.getName(), subject.getDescription(), subject.isArchived());
        }
        logger.warn("Subject with ID {} not found. Cannot unarchive.", subjectId);
        throw new SubjectNotFoundException("Subject with ID " + subjectId + " not found.");
    }

    private Optional<Subject> getSubjectByName(int userId, String subjectName) {
        return subjectRepository.findByUserId(userId).stream().filter(s -> s.getName().equals(subjectName)).findFirst();
    }
}
