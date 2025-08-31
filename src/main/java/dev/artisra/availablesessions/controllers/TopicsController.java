package dev.artisra.availablesessions.controllers;

import dev.artisra.availablesessions.models.res.TopicResponse;
import dev.artisra.availablesessions.models.req.TopicRequest;
import dev.artisra.availablesessions.services.interfaces.TopicService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/available-sessions")
public class TopicsController {

    private final TopicService topicService;

    public TopicsController(@Autowired TopicService topicService) {
        this.topicService = topicService;
    }

    @PostMapping("/subjects/{subjectId}/topics")
    public ResponseEntity<Integer> addTopicToSubject(
            @PathVariable Integer subjectId,
            @Valid @RequestBody TopicRequest topicRequest) {
        int newTopicId = topicService.addTopicToSubject(subjectId, topicRequest.getTopic(), topicRequest.getDescription());
        return ResponseEntity
                .created(URI.create("/api/v1/available-sessions/topics/" + newTopicId))
                .body(newTopicId);
    }

    @GetMapping("/subjects/{subjectId}/topics")
    public ResponseEntity<List<TopicResponse>> getAllTopicsForSubject(@PathVariable Integer subjectId) {
        return ResponseEntity.ok(topicService.getAllTopicsForSubject(subjectId));
    }

    @GetMapping("/topics/{topicId}")
    public ResponseEntity<TopicResponse> getTopicById(@PathVariable Integer topicId) {
        return ResponseEntity.ok(topicService.getTopicById(topicId));
    }

    @PatchMapping("/topics/{topicId}")
    public ResponseEntity<TopicResponse> updateTopic(
            @PathVariable Integer topicId,
            @RequestBody TopicRequest topicRequest
    ) {
        topicService.updateTopic(topicId, topicRequest);
        return ResponseEntity.noContent().build();
    }
}
