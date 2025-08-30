package dev.artisra.availablesessions.services.interfaces;

import dev.artisra.availablesessions.models.TopicDTO;

import java.util.List;

public interface TopicService {
    int addTopicToSubject(int subjectId, String topicName, String description);
    List<TopicDTO> getAllTopicsForSubject(int subjectId);
    boolean topicExists(int topicId);
    boolean deleteTopic(int topicId);
}
