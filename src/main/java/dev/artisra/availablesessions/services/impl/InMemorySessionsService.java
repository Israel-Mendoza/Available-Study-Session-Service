package dev.artisra.availablesessions.services.impl;

import dev.artisra.availablesessions.models.Session;
import dev.artisra.availablesessions.services.interfaces.SessionsService;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemorySessionsService implements SessionsService {
    private final ConcurrentHashMap<Integer, Session> sessions = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Integer, Collection<Session>> sessionsByUser = new ConcurrentHashMap<>();
    private final AtomicInteger sessionIdGenerator = new AtomicInteger(1);

    @Override
    public int createSession(int userId, String sessionName, String description) {
        int sessionId = sessionIdGenerator.getAndIncrement();
        Session session = new Session(userId, sessionId, sessionName, description);
        sessions.put(sessionId, session);
        sessionsByUser.computeIfAbsent(userId, k -> new CopyOnWriteArrayList<>()).add(session);
        return sessionId;
    }

    @Override
    public boolean addTopicToSession(int sessionId, String topicName, String description) {
        Session session = sessions.get(sessionId);
        if (session == null) return false;
        return session.addTopicToSession(topicName, description);
    }

    @Override
    public Session getSession(int sessionId) {
        return sessions.get(sessionId);
    }

    @Override
    public boolean archiveSession(int sessionId) {
        Session session = sessions.get(sessionId);
        if (session == null) return false;
        session.setArchived(true);
        return true;
    }
}