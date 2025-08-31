package dev.artisra.availablesessions.models.res;

public class TopicResponse {
    private int topicId;
    private int subjectId;
    private String topicName;
    private String description;

    public TopicResponse(int topicId, int subjectId, String topicName, String description) {
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
