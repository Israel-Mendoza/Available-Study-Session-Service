package dev.artisra.availablesessions.unit.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.artisra.availablesessions.controllers.TopicsController;
import dev.artisra.availablesessions.models.req.TopicRequest;
import dev.artisra.availablesessions.models.res.TopicResponse;
import dev.artisra.availablesessions.services.impl.TopicServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {TopicsController.class}) // Specify the controller class here
public class TopicsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TopicServiceImpl topicService;

    @Test
    public void contextLoads() {
        // This test will pass if the application context loads successfully
    }

    // Test for POST /api/v1/available-sessions/subjects/{subjectId}/topics
    @Test
    public void testAddTopicToSubject_Success() throws Exception {
        // Arrange
        int subjectId = 1;
        int newTopicId = 101;
        TopicRequest topicRequest = new TopicRequest();
        topicRequest.setTopic("Algebra");
        topicRequest.setDescription("Basic concepts");

        when(topicService.addTopicToSubject(anyInt(), any(), any())).thenReturn(newTopicId);

        // Act & Assert
        mockMvc.perform(post("/api/v1/available-sessions/subjects/{subjectId}/topics", subjectId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(topicRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/available-sessions/topics/" + newTopicId))
                .andExpect(content().string(String.valueOf(newTopicId)));
    }

    @Test
    public void testAddTopicToSubject_InvalidInput() throws Exception {
        // Arrange
        int subjectId = 1;
        TopicRequest topicRequest = new TopicRequest(); // Missing required fields

        // Act & Assert
        mockMvc.perform(post("/api/v1/available-sessions/subjects/{subjectId}/topics", subjectId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(topicRequest)))
                .andExpect(status().isBadRequest());
    }

    // Test for GET /api/v1/available-sessions/subjects/{subjectId}/topics
    @Test
    public void testGetAllTopicsForSubject_Success() throws Exception {
        // Arrange
        int subjectId = 1;
        List<TopicResponse> topics = List.of(new TopicResponse(1, subjectId, "Algebra", "Basic concepts"));
        when(topicService.getAllTopicsForSubject(subjectId)).thenReturn(topics);

        // Act & Assert
        mockMvc.perform(get("/api/v1/available-sessions/subjects/{subjectId}/topics", subjectId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].topicId").value(1))
                .andExpect(jsonPath("$[0].name").value("Algebra"));
    }

    // Test for GET /api/v1/topics/{topicId}
    @Test
    public void testGetTopicById_Success() throws Exception {
        // Arrange
        int topicId = 1;
        TopicResponse topicResponse = new TopicResponse(topicId, 1, "Mathematics", "Calculus");
        when(topicService.getTopicById(topicId)).thenReturn(topicResponse);

        // Act & Assert
        mockMvc.perform(get("/api/v1/available-sessions/topics/{topicId}", topicId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.topicId").value(topicId))
                .andExpect(jsonPath("$.name").value("Mathematics"));
    }

    // Test for PATCH /api/v1/topics/{topicId}
    @Test
    public void testUpdateTopic_Success() throws Exception {
        // Arrange
        int topicId = 1;
        TopicRequest topicRequest = new TopicRequest();
        topicRequest.setTopic("Trigonometry");

        // The service method returns void, so we just mock the call
        // The controller's return is noContent, which is a 204

        // Act & Assert
        mockMvc.perform(patch("/api/v1/available-sessions/topics/{topicId}", topicId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(topicRequest)))
                .andExpect(status().isNoContent());
    }
}
