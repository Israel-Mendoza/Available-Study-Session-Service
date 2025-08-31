package dev.artisra.availablesessions.models.req;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class SubjectRequest {
    @NotNull(message = "Subject cannot be null")
    @Size(min = 2, max = 100, message = "Subject must be between 2 and 100 characters")
    private String subject;
    @NotNull(message = "Description cannot be null")
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    public SubjectRequest() {
    }

    public SubjectRequest(String subject, String description) {
        this.subject = subject;
        this.description = description;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
