package dev.artisra.availablesessions.services.interfaces;

import dev.artisra.availablesessions.models.Session;

public interface SessionsService {
    int createSession(int userId, String sessionName, String description);
    boolean addTopicToSession(int sessionId, String topicName, String description);
    Session getSession(int sessionId);
    boolean archiveSession(int sessionId);
}
