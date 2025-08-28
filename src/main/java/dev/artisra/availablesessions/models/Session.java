package dev.artisra.availablesessions.models;

import java.util.List;

public class Session {
    private int userId;
    private int sessionId;
    private String sessionName;
    private String description;
    private List<Topics> topics;
    private boolean isArchived;

    public Session(int userId, int sessionId, String sessionName, String description) {
        this.userId = userId;
        this.sessionId = sessionId;
        this.sessionName = sessionName;
        this.description = description;
        this.topics = List.of();
        this.isArchived = false; // Default value
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Topics> getTopics() {
        return topics;
    }

    public boolean addTopicToSession(String topicName, String description) {
        Topics newTopic = new Topics(topicName, description);
        if (topics.contains(newTopic)) {
            return false; // Topic already exists
        }
        topics.add(newTopic);
        return true;
    }

    public boolean isArchived() {
        return isArchived;
    }

    public void setArchived(boolean archived) {
        isArchived = archived;
    }
}
