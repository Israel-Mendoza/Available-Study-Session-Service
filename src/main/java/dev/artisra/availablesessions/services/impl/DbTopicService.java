package dev.artisra.availablesessions.services.impl;

import dev.artisra.availablesessions.entities.Subject;
import dev.artisra.availablesessions.entities.Topic;
import dev.artisra.availablesessions.repositories.SubjectRepository;
import dev.artisra.availablesessions.repositories.TopicRepository;
import dev.artisra.availablesessions.services.interfaces.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DbTopicService implements TopicService {

    private final TopicRepository topicRepository;
    private final SubjectRepository subjectRepository;

    public DbTopicService(@Autowired TopicRepository topicRepository, @Autowired SubjectRepository subjectRepository) {
        this.topicRepository = topicRepository;
        this.subjectRepository = subjectRepository;
    }


    @Override
    public int addTopicToSubject(int subjectId, String topicName, String description) {
        Optional<Subject> subjectOpt = subjectRepository.findById(subjectId);
        if (subjectOpt.isEmpty()) {
            return -1; // Subject does not exist
        }

        Subject subject = subjectOpt.get();

        Topic newTopic = new Topic();
        newTopic.setName(topicName);
        newTopic.setDescription(description);
        newTopic.setSubject(subject);

        return topicRepository.save(newTopic).getId();
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
