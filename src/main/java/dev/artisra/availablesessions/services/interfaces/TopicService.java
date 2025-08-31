package dev.artisra.availablesessions.services.interfaces;

import dev.artisra.availablesessions.models.res.TopicResponse;
import dev.artisra.availablesessions.models.req.TopicRequest;

import java.util.List;

public interface TopicService {
    int addTopicToSubject(int subjectId, String topicName, String description);
    TopicResponse getTopicById(int topicId);
    List<TopicResponse> getAllTopicsForSubject(int subjectId);
    void updateTopic(int topicId, TopicRequest topicRequest);
    boolean topicExists(int topicId);
    boolean deleteTopic(int topicId);
}
