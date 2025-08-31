package dev.artisra.availablesessions.services.interfaces;

import dev.artisra.availablesessions.models.TopicDTO;
import dev.artisra.availablesessions.models.req.TopicRequest;

import java.util.List;

public interface TopicService {
    int addTopicToSubject(int subjectId, String topicName, String description);
    TopicDTO getTopicById(int topicId);
    List<TopicDTO> getAllTopicsForSubject(int subjectId);
    void updateTopic(int topicId, TopicRequest topicRequest);
    boolean topicExists(int topicId);
    boolean deleteTopic(int topicId);
}
