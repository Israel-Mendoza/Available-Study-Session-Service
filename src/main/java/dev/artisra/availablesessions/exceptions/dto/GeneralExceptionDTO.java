package dev.artisra.availablesessions.exceptions.dto;

public class GeneralExceptionDTO {
    private final String message;
    private final String timestamp;
    private final String error;
    private final String path;
    private final int status;

    public GeneralExceptionDTO(String message, String timestamp, String error, String path, int status) {
        this.message = message;
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.path = path;
    }

    public String getMessage() {
        return message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getPath() {
        return path;
    }
}
