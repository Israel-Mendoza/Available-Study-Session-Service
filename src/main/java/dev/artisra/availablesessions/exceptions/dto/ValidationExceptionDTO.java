package dev.artisra.availablesessions.exceptions.dto;

import java.util.Map;

public class ValidationExceptionDTO extends GeneralExceptionDTO {
    private final Map<String, String> fieldErrors;

    public ValidationExceptionDTO(String message, String timestamp, String error, String path, int status, Map<String, String> fieldErrors) {
        super(message, timestamp, error, path, status);
        this.fieldErrors = fieldErrors;
    }

    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }
}
