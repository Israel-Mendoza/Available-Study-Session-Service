package dev.artisra.availablesessions.services.impl;

import dev.artisra.availablesessions.entities.Subject;
import dev.artisra.availablesessions.entities.User;
import dev.artisra.availablesessions.models.SubjectDTO;
import dev.artisra.availablesessions.repositories.SubjectRepository;
import dev.artisra.availablesessions.repositories.TopicRepository;
import dev.artisra.availablesessions.repositories.UserRepository;
import dev.artisra.availablesessions.services.interfaces.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DbSubjectService implements SubjectService {

    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;

    public DbSubjectService(@Autowired UserRepository userRepository, @Autowired SubjectRepository subjectRepository, @Autowired TopicRepository topicRepository) {
        this.userRepository = userRepository;
        this.subjectRepository = subjectRepository;
    }

    @Override
    public int createSubject(int userId, String subjectName, String description) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return -1; // User does not exist
        }

        User user = userOpt.get();

        Subject newSubject = new Subject();
        newSubject.setName(subjectName);
        newSubject.setDescription(description);
        newSubject.setUser(user);
        return subjectRepository.save(newSubject).getId();
    }

    @Override
    public SubjectDTO getSubject(int subjectId) {
        Optional<Subject> subjectOpt = subjectRepository.findById(subjectId);
        if (subjectOpt.isPresent()) {
            Subject subject = subjectOpt.get();
            return new SubjectDTO(subject.getUser().getId(), subject.getId(), subject.getName(), subject.getDescription());
        }
        return null;
    }

    @Override
    public boolean archiveSubject(int subjectId) {
        Optional<Subject> subjectOpt = subjectRepository.findById(subjectId);
        if (subjectOpt.isPresent()) {
            Subject subject = subjectOpt.get();
            subject.setArchived(true);
            subjectRepository.save(subject);
            return true;
        }
        return false; // Subject does not exist
    }
}
