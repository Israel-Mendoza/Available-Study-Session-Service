package dev.artisra.availablesessions.models.req;

public class SubjectRequest {
    private String sessionName;
    private String description;

    public SubjectRequest() {
    }

    public SubjectRequest(String sessionName, String description) {
        this.sessionName = sessionName;
        this.description = description;
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
}
