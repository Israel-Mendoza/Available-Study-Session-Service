package dev.artisra.availablesessions.exceptions.custom;

public class ExistingSubjectException extends RuntimeException {
    public ExistingSubjectException(String message) {
        super(message);
    }
}
