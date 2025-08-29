package dev.artisra.availablesessions.controllers;

import dev.artisra.availablesessions.models.req.SubjectRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/available-sessions")
public class AvailableSubjectsController {

    @PostMapping("/users/{userId}/subject")
    public String createNewSubject(@PathVariable Integer userId, @RequestBody SubjectRequest subjectRequest) {
        // Logic to create a new subject for the user with userId using details from subjectRequest
        return "New subject created for user " + userId;
    }
}
