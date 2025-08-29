package dev.artisra.availablesessions.services.interfaces;

public interface TopicService {
    int addTopicToSubject(int subjectId, String topicName, String description);
    boolean topicExists(int topicId);
    boolean deleteTopic(int topicId);
}
