package dev.artisra.availablesessions.controllers;

import dev.artisra.availablesessions.exceptions.custom.SubjectNotFoundException;
import dev.artisra.availablesessions.models.SubjectDTO;
import dev.artisra.availablesessions.models.req.SubjectRequest;
import dev.artisra.availablesessions.services.interfaces.SubjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/available-sessions")
public class SubjectsController {

    private static final Logger logger = LoggerFactory.getLogger(SubjectsController.class);

    private final SubjectService subjectService;

    public SubjectsController(@Autowired SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @PostMapping("/users/{userId}/subjects")
    public ResponseEntity<Integer> createNewSubject(@PathVariable Integer userId, @RequestBody SubjectRequest subjectRequest) {
        logger.info("Creating new subject '{}' for user ID {}", subjectRequest.getSubject(), userId);
        var createdSubjectId = subjectService.createSubject(userId, subjectRequest.getSubject(), subjectRequest.getDescription());
        return new ResponseEntity<>(createdSubjectId, org.springframework.http.HttpStatus.CREATED);
    }

    @PostMapping("/subjects/{subjectId}/archive")
    public ResponseEntity<SubjectDTO> archiveSubject(@PathVariable Integer subjectId) {
        logger.info("Archiving subject with ID {}", subjectId);
        SubjectDTO subjectDTO = subjectService.archiveSubject(subjectId);
        return ResponseEntity.ok(subjectDTO);
    }

    @PostMapping("/subjects/{subjectId}/unarchive")
    public ResponseEntity<SubjectDTO> unarchiveSubject(@PathVariable Integer subjectId) {
        logger.info("Unarchiving subject with ID {}", subjectId);
        SubjectDTO subjectDTO = subjectService.unarchiveSubject(subjectId);
        return ResponseEntity.ok(subjectDTO);
    }

    @GetMapping("/subjects/{subjectId}")
    public ResponseEntity<SubjectDTO> getSubjectById(@PathVariable Integer subjectId) {
        var subject = subjectService.getSubjectById(subjectId);
        if (subject == null) {
            throw new SubjectNotFoundException("Subject with ID " + subjectId + " not found.");
        }
        return ResponseEntity.ok(subject);
    }

    @GetMapping("/users/{userId}/subjects")
    public ResponseEntity<?> getAllSubjectsForUser(@PathVariable Integer userId) {
        var subjects = subjectService.getAllSubjectsForUser(userId);
        logger.info("Retrieved {} subjects for user ID {}", subjects.size(), userId);
        return ResponseEntity.ok(subjects);
    }
}
