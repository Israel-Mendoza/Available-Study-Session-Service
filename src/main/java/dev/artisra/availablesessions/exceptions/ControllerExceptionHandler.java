package dev.artisra.availablesessions.exceptions;

import dev.artisra.availablesessions.exceptions.custom.*;
import dev.artisra.availablesessions.exceptions.dto.GeneralExceptionDTO;
import dev.artisra.availablesessions.exceptions.dto.ValidationExceptionDTO;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ControllerExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<GeneralExceptionDTO> handleUserNotFoundException(UserNotFoundException ex, WebRequest request) {
        var exceptionDTO = buildExceptionDTO(ex, request, "Not Found", 404);

        logger.warn("User not found: {} - URI: {}", ex.getMessage(), request.getDescription(false).replace("uri=", ""));

        return new ResponseEntity<>(exceptionDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SubjectNotFoundException.class)
    public ResponseEntity<GeneralExceptionDTO> handleSubjectNotFoundException(SubjectNotFoundException ex, WebRequest request) {
        var exceptionDTO = buildExceptionDTO(ex, request, "Not Found", 404);
        logger.warn("Subject not found: {} - URI: {}", ex.getMessage(), request.getDescription(false).replace("uri=", ""));
        return new ResponseEntity<>(exceptionDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TopicNotFoundException.class)
    public ResponseEntity<GeneralExceptionDTO> handleTopicNotFoundException(TopicNotFoundException ex, WebRequest request) {
        var exceptionDTO = buildExceptionDTO(ex, request, "Not Found", 404);
        logger.warn("Topic not found: {} - URI: {}", ex.getMessage(), request.getDescription(false).replace("uri=", ""));
        return new ResponseEntity<>(exceptionDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ExistingSubjectException.class)
    public ResponseEntity<GeneralExceptionDTO> handleExistingSubjectException(ExistingSubjectException ex, WebRequest request) {
        var exceptionDTO = buildExceptionDTO(ex, request, "Conflict", 409);

        logger.warn("Subject conflict occurred: {}", ex.getMessage());

        return new ResponseEntity<>(exceptionDTO, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ExistingTopicException.class)
    public ResponseEntity<GeneralExceptionDTO> handleExistingTopicException(ExistingTopicException ex, WebRequest request) {
        var exceptionDTO = buildExceptionDTO(ex, request, "Conflict", 409);

        logger.warn("Topic conflict occurred: {}", ex.getMessage());

        return new ResponseEntity<>(exceptionDTO, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationExceptionDTO> handleValidationExceptions(MethodArgumentNotValidException ex,  WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        logger.warn("Validation failed: {}", errors);

        ValidationExceptionDTO validationExceptionDTO = new ValidationExceptionDTO(
                "Validation failed",
                LocalDateTime.now().toString(),
                "Bad Request",
                request.getDescription(false).replace("uri=", ""),
                400,
                errors
        );

        return new ResponseEntity<>(validationExceptionDTO, HttpStatus.BAD_REQUEST);
    }

    private GeneralExceptionDTO buildExceptionDTO(Exception ex, WebRequest request, String status, int code) {
        return new GeneralExceptionDTO(
                ex.getMessage(),
                LocalDateTime.now().toString(),
                status,
                request.getDescription(false).replace("uri=", ""),
                code
        );
    }
}
