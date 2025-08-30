package dev.artisra.availablesessions.models;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

public class SubjectDTO {
    private int userId;
    private int subjectId;
    private String name;
    private String description;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<TopicDTO> topicDTOs;
    private boolean isArchived;

    public SubjectDTO(int subjectId, int userId,  String sessionName, String description, boolean isArchived) {
        this.subjectId = subjectId;
        this.userId = userId;
        this.name = sessionName;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
