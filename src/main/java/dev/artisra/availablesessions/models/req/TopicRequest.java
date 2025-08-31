package dev.artisra.availablesessions.models.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class TopicRequest {
    @NotNull(message = "Topic cannot be null")
    @Size(min = 2, max = 100, message = "Topic cannot exceed 100 characters")
    private String topic;
    @NotNull(message = "Description cannot be null")
    @Size(max = 500, message = "Description cannot exceed 500 characters")
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
