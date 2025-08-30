package dev.artisra.availablesessions.services.impl;

import dev.artisra.availablesessions.entities.Subject;
import dev.artisra.availablesessions.entities.User;
import dev.artisra.availablesessions.exceptions.custom.ExistingSubjectException;
import dev.artisra.availablesessions.exceptions.custom.SubjectNotFoundException;
import dev.artisra.availablesessions.exceptions.custom.UserNotFoundException;
import dev.artisra.availablesessions.mappers.SubjectMapper;
import dev.artisra.availablesessions.mappers.TopicMapper;
import dev.artisra.availablesessions.models.SubjectDTO;
import dev.artisra.availablesessions.models.TopicDTO;
import dev.artisra.availablesessions.repositories.SubjectRepository;
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
    private final TopicMapper topicMapper;
    private final SubjectMapper subjectMapper;

    public SubjectServiceImpl(
            @Autowired UserRepository userRepository,
            @Autowired SubjectRepository subjectRepository,
            @Autowired TopicMapper topicMapper,
            @Autowired SubjectMapper subjectMapper
    ) {
        this.userRepository = userRepository;
        this.subjectRepository = subjectRepository;
        this.topicMapper = topicMapper;
        this.subjectMapper = subjectMapper;
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
    public SubjectDTO getSubjectById(int subjectId, boolean includeTopics) {
        Optional<Subject> subjectOpt = subjectRepository.findById(subjectId);
        if (subjectOpt.isEmpty()) {
            logger.warn("Subject with ID {} not found", subjectId);
            throw new SubjectNotFoundException("Subject with ID " + subjectId + " not found.");
        }
        Subject subject = subjectOpt.get();
        logger.info("Subject with ID {} found: '{}'", subjectId, subject.getName());

        SubjectDTO subjectDTO = subjectMapper.subjectToSubjectDTO(subject);

        if (includeTopics) {
            logger.info("Including topics for subject ID {}", subjectId);
            populateTopicsForSubjectDTO(subject, subjectDTO);
        }
        return subjectDTO;
    }

    @Override
    public List<SubjectDTO> getAllSubjectsForUser(int userId, boolean includeTopics) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User with ID " + userId + " does not exist.");
        }

        // Using the repository to fetch subjects directly
        var subjects = getSubjectDTOsForUser(userId);
        logger.info("Retrieved {} subjects for user ID {}", subjects.size(), userId);

        if (!includeTopics) {
            return subjects;
        }

        for (var subjectDTO : subjects) {
            Optional<Subject> subjectOpt = subjectRepository.findById(subjectDTO.getSubjectId());
            subjectOpt.ifPresent(subject -> populateTopicsForSubjectDTO(subject, subjectDTO));
        }

        return subjects;
    }

    @Override
    public SubjectDTO archiveSubject(int subjectId) {
        Optional<Subject> subjectOpt = subjectRepository.findById(subjectId);
        if (subjectOpt.isPresent()) {
            logger.info("Subject with ID {} found. Archiving...", subjectId);
            Subject subject = subjectOpt.get();
            subject.setArchived(true);
            subjectRepository.save(subject);

            return subjectMapper.subjectToSubjectDTO(subject);
        }
        logger.warn("Subject with ID {} not found. Cannot archive.", subjectId);
        throw new SubjectNotFoundException("Subject with ID " + subjectId + " not found.");
    }

    @Override
    public SubjectDTO unarchiveSubject(int subjectId) {
        Optional<Subject> subjectOpt = subjectRepository.findById(subjectId);
        if (subjectOpt.isPresent()) {
            logger.info("Subject with ID {} found. Unarchiving...", subjectId);
            Subject subject = subjectOpt.get();
            subject.setArchived(false);
            subjectRepository.save(subject);
            return subjectMapper.subjectToSubjectDTO(subject);
        }
        logger.warn("Subject with ID {} not found. Cannot unarchive.", subjectId);
        throw new SubjectNotFoundException("Subject with ID " + subjectId + " not found.");
    }

    private List<TopicDTO> getTopicDTOsForSubject(Subject subject) {
        return subject.getTopics()
                .stream()
                .map(topicMapper::topicToTopicDTO)
                .toList();
    }

    private List<SubjectDTO> getSubjectDTOsForUser(int userId) {
        return subjectRepository.findByUserId(userId)
                .stream()
                .map(subjectMapper::subjectToSubjectDTO)
                .toList();
    }

    private void populateTopicsForSubjectDTO(Subject subject, SubjectDTO subjectDTO) {
        List<TopicDTO> topicDTOs = getTopicDTOsForSubject(subject);
        subjectDTO.setTopicDTOs(topicDTOs);
    }

    private Optional<Subject> getSubjectByName(int userId, String subjectName) {
        return subjectRepository.findByUserId(userId).stream().filter(s -> s.getName().equals(subjectName)).findFirst();
    }
}
