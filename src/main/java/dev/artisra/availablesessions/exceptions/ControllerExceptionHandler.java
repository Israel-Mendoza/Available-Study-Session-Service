package dev.artisra.availablesessions.exceptions;

import dev.artisra.availablesessions.exceptions.custom.ExistingSubjectException;
import dev.artisra.availablesessions.exceptions.custom.ExistingTopicException;
import dev.artisra.availablesessions.exceptions.custom.SubjectNotFoundException;
import dev.artisra.availablesessions.exceptions.custom.UserNotFoundException;
import dev.artisra.availablesessions.exceptions.dto.GeneralExceptionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

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
