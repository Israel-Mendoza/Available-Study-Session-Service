package dev.artisra.availablesessions.models;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

public class SubjectDTO {
    private int userId;
    private int subjectId;
    private String sessionName;
    private String description;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<TopicDTO> topicDTOs;
    private boolean isArchived;

    public SubjectDTO(int userId, int subjectId, String sessionName, String description, boolean isArchived) {
        this.userId = userId;
        this.subjectId = subjectId;
        this.sessionName = sessionName;
        this.description = description;
        this.topicDTOs = null;
        this.isArchived = isArchived; // Default value
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
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

    public List<TopicDTO> getTopicDTOs() {
        return topicDTOs;
    }

    public void setTopicDTOs(List<TopicDTO> topicDTOs) {
        this.topicDTOs = topicDTOs;
    }

    public boolean isArchived() {
        return isArchived;
    }

    public void setArchived(boolean archived) {
        isArchived = archived;
    }
}
