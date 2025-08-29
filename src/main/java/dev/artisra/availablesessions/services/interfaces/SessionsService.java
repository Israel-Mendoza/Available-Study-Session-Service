package dev.artisra.availablesessions.services.interfaces;

import dev.artisra.availablesessions.models.Subject;

public interface SessionsService {
    int createSession(int userId, String sessionName, String description);
    boolean addTopicToSession(int sessionId, String topicName, String description);
    Subject getSession(int sessionId);
    boolean archiveSession(int sessionId);
}
