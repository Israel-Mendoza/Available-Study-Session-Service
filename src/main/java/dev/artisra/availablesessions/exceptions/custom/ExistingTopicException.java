package dev.artisra.availablesessions.exceptions.custom;

public class ExistingTopicException extends RuntimeException {
    public ExistingTopicException(String message) {
        super(message);
    }
}
