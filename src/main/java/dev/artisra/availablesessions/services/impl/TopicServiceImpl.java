package dev.artisra.availablesessions.services.impl;

import dev.artisra.availablesessions.entities.Subject;
import dev.artisra.availablesessions.entities.Topic;
import dev.artisra.availablesessions.exceptions.custom.ExistingTopicException;
import dev.artisra.availablesessions.exceptions.custom.SubjectNotFoundException;
import dev.artisra.availablesessions.models.TopicDTO;
import dev.artisra.availablesessions.repositories.SubjectRepository;
import dev.artisra.availablesessions.repositories.TopicRepository;
import dev.artisra.availablesessions.services.interfaces.TopicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TopicServiceImpl implements TopicService {

    private static final Logger logger = LoggerFactory.getLogger(TopicServiceImpl.class);

    private final TopicRepository topicRepository;
    private final SubjectRepository subjectRepository;

    public TopicServiceImpl(@Autowired TopicRepository topicRepository, @Autowired SubjectRepository subjectRepository) {
        this.topicRepository = topicRepository;
        this.subjectRepository = subjectRepository;
    }


    @Override
    public int addTopicToSubject(int subjectId, String topicName, String description) {
        Optional<Subject> subjectOpt = subjectRepository.findById(subjectId);
        if (subjectOpt.isEmpty()) {
            logger.warn("Subject with ID {} not found.", subjectId);
            throw new SubjectNotFoundException("Subject with ID " + subjectId + " not found.");
        }

        // Check if topic with the same name already exists for the subject
        var topicsInSubject = topicRepository.findBySubjectId(subjectId);
        for (Topic topic : topicsInSubject) {
            if (topic.getName().equalsIgnoreCase(topicName)) {
                logger.warn("Topic '{}' already exists for subject ID {}", topicName, subjectId);
                throw new ExistingTopicException("Topic '" + topicName + "' already exists for subject ID " + subjectId);
            }
        }

        logger.info("Adding topic '{}' to subject ID {}", topicName, subjectId);

        Subject subject = subjectOpt.get();

        Topic newTopic = new Topic();
        newTopic.setName(topicName);
        newTopic.setDescription(description);
        newTopic.setSubject(subject);

        logger.info("Topic '{}' added to subject ID {} with topic ID {}", topicName, subjectId, newTopic.getId());
        return topicRepository.save(newTopic).getId();
    }

    @Override
    public List<TopicDTO> getAllTopicsForSubject(int subjectId) {
        return topicRepository.findBySubjectId(subjectId).stream()
                .map(topic -> new TopicDTO(topic.getId(), topic.getSubject().getId(), topic.getName(), topic.getDescription()))
                .toList();
    }

    @Override
    public boolean topicExists(int topicId) {
        return topicRepository.existsById(topicId);
    }

    @Override
    public boolean deleteTopic(int topicId) {
        return topicRepository.deleteById(topicId);
    }
}
