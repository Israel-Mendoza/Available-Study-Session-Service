package dev.artisra.availablesessions.models.req;

public class SubjectRequest {
    private String subject;
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
