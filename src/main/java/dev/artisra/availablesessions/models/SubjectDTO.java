package dev.artisra.availablesessions.models;

import java.util.List;

public class SubjectDTO {
    private int userId;
    private int sessionId;
    private String sessionName;
    private String description;
    private List<TopicDTO> topicDTOS;
    private boolean isArchived;

    public SubjectDTO(int userId, int sessionId, String sessionName, String description, boolean isArchived) {
        this.userId = userId;
        this.sessionId = sessionId;
        this.sessionName = sessionName;
        this.description = description;
        this.topicDTOS = List.of();
        this.isArchived = isArchived; // Default value
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

    public List<TopicDTO> getTopics() {
        return topicDTOS;
    }

    public boolean addTopicToSession(String topicName, String description) {
        TopicDTO newTopicDTO = new TopicDTO(topicName, description);
        if (topicDTOS.contains(newTopicDTO)) {
            return false; // Topic already exists
        }
        topicDTOS.add(newTopicDTO);
        return true;
    }

    public boolean isArchived() {
        return isArchived;
    }

    public void setArchived(boolean archived) {
        isArchived = archived;
    }
}
