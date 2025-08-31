package dev.artisra.availablesessions.controllers;

import dev.artisra.availablesessions.models.res.SubjectResponse;
import dev.artisra.availablesessions.models.req.SubjectRequest;
import dev.artisra.availablesessions.services.interfaces.SubjectService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/available-sessions")
public class SubjectsController {

    private static final Logger logger = LoggerFactory.getLogger(SubjectsController.class);

    private final SubjectService subjectService;

    public SubjectsController(@Autowired SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @PostMapping("/users/{userId}/subjects")
    public ResponseEntity<Integer> createNewSubject(
            @PathVariable Integer userId,
            @Valid @RequestBody SubjectRequest subjectRequest
    ) {
        logger.info("Creating new subject '{}' for user ID {}", subjectRequest.getSubject(), userId);
        var createdSubjectId = subjectService.createSubject(userId, subjectRequest.getSubject(), subjectRequest.getDescription());
        return ResponseEntity
                .created(URI.create("/api/v1/available-sessions/subjects/" + createdSubjectId))
                .body(createdSubjectId);
    }

    @PostMapping("/subjects/{subjectId}/archive")
    public ResponseEntity<SubjectResponse> archiveSubject(@PathVariable Integer subjectId) {
        logger.info("Archiving subject with ID {}", subjectId);
        SubjectResponse subjectResponse = subjectService.archiveSubject(subjectId);
        return ResponseEntity.ok(subjectResponse);
    }

    @PostMapping("/subjects/{subjectId}/unarchive")
    public ResponseEntity<SubjectResponse> unarchiveSubject(@PathVariable Integer subjectId) {
        logger.info("Unarchiving subject with ID {}", subjectId);
        SubjectResponse subjectResponse = subjectService.unarchiveSubject(subjectId);
        return ResponseEntity.ok(subjectResponse);
    }

    @GetMapping("/subjects/{subjectId}")
    public ResponseEntity<SubjectResponse> getSubjectById(
            @PathVariable Integer subjectId,
            @RequestParam (value = "includeTopics", defaultValue = "false") boolean includeTopics
    ) {
        var subject = subjectService.getSubjectById(subjectId, includeTopics);
        return ResponseEntity.ok(subject);
    }

    @GetMapping("/users/{userId}/subjects")
    public ResponseEntity<?> getSubjectsForUser(
            @PathVariable Integer userId,
            @RequestParam(value = "includeTopics", defaultValue = "false") boolean includeTopics,
            @RequestParam(value = "archived", defaultValue = "false") boolean archived
    ) {
        List<SubjectResponse> subjects;

        if (archived) {
            logger.info("Fetching archived subjects for user ID {}", userId);
            subjects = subjectService.getArchivedSubjectsByUserId(userId, includeTopics);
        } else {
            logger.info("Fetching non-archived subjects for user ID {}", userId);
            subjects = subjectService.getNonArchivedSubjectsByUserId(userId, includeTopics);
        }
        logger.info("Retrieved {} subjects for user ID {}", subjects.size(), userId);
        return ResponseEntity.ok(subjects);
    }

    @PatchMapping("/subjects/{subjectId}")
    public ResponseEntity<SubjectResponse> updateSubject(
            @PathVariable Integer subjectId,
            @RequestBody SubjectRequest subjectRequest
    ) {
        logger.info("Updating subject with ID {}", subjectId);
        subjectService.updateSubject(subjectId, subjectRequest);
        return ResponseEntity.noContent().build();
    }
}