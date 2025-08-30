package dev.artisra.availablesessions.models;

public class TopicDTO {
    private int topicId;
    private int subjectId;
    private String topicName;
    private String description;

    public TopicDTO(int topicId, int subjectId, String topicName, String description) {
        this.topicId = topicId;
        this.subjectId = subjectId;
        this.topicName = topicName;
        this.description = description;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }
}
