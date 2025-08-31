package dev.artisra.availablesessions.models.res;

public class TopicResponse {
    private int topicId;
    private int subjectId;
    private String name;
    private String description;

    public TopicResponse(int topicId, int subjectId, String topicName, String description) {
        this.topicId = topicId;
        this.subjectId = subjectId;
        this.name = topicName;
        this.description = description;
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
