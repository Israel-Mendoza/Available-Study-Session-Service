package dev.artisra.availablesessions.exceptions.custom;

public class TopicNotFoundException extends RuntimeException {
    public TopicNotFoundException(String message) {
        super(message);
    }
}
