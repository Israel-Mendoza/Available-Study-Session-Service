package dev.artisra.availablesessions.models.req;

public class TopicRequest {
    private String topic;
    private String description;

    public TopicRequest() {
    }

    public TopicRequest(String subject, String description) {
        this.topic = subject;
        this.description = description;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
