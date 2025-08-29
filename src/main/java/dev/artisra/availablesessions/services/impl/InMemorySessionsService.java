package dev.artisra.availablesessions.services.impl;

import dev.artisra.availablesessions.models.Subject;
import dev.artisra.availablesessions.services.interfaces.SessionsService;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemorySessionsService implements SessionsService {
    private final ConcurrentHashMap<Integer, Subject> sessions = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Integer, Collection<Subject>> sessionsByUser = new ConcurrentHashMap<>();
    private final AtomicInteger sessionIdGenerator = new AtomicInteger(1);

    @Override
    public int createSession(int userId, String sessionName, String description) {
        int sessionId = sessionIdGenerator.getAndIncrement();
        Subject subject = new Subject(userId, sessionId, sessionName, description);
        sessions.put(sessionId, subject);
        sessionsByUser.computeIfAbsent(userId, k -> new CopyOnWriteArrayList<>()).add(subject);
        return sessionId;
    }

    @Override
    public boolean addTopicToSession(int sessionId, String topicName, String description) {
        Subject subject = sessions.get(sessionId);
        if (subject == null) return false;
        return subject.addTopicToSession(topicName, description);
    }

    @Override
    public Subject getSession(int sessionId) {
        return sessions.get(sessionId);
    }

    @Override
    public boolean archiveSession(int sessionId) {
        Subject subject = sessions.get(sessionId);
        if (subject == null) return false;
        subject.setArchived(true);
        return true;
    }
}