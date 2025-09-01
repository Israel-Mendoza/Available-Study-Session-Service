package dev.artisra.availablesessions.unit.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.artisra.availablesessions.controllers.SubjectsController;
import dev.artisra.availablesessions.exceptions.custom.SubjectNotFoundException;
import dev.artisra.availablesessions.models.req.SubjectRequest;
import dev.artisra.availablesessions.models.res.SubjectResponse;
import dev.artisra.availablesessions.services.interfaces.SubjectService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SubjectsController.class)
public class SubjectsControllerTest {

    private final String BASE_URL = "/api/v1/available-sessions";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private SubjectService subjectService;

    // Test for POST /users/{userId}/subjects
    @Test
    void createNewSubject_Success() throws Exception {
        // Arrange
        int userId = 1;
        int createdSubjectId = 101;
        SubjectRequest request = new SubjectRequest("Math", "Study of numbers");

        when(subjectService.createSubject(anyInt(), anyString(), anyString())).thenReturn(createdSubjectId);

        // Act & Assert
        mockMvc.perform(post(BASE_URL + "/users/{userId}/subjects", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/available-sessions/subjects/" + createdSubjectId))
                .andExpect(content().string(String.valueOf(createdSubjectId)));
    }

    // Test for POST /subjects/{subjectId}/archive
    @Test
    void archiveSubject_Success() throws Exception {
        // Arrange
        int subjectId = 1;
        SubjectResponse response = new SubjectResponse(subjectId, 1, "Math", "Study of numbers", true);

        when(subjectService.archiveSubject(subjectId)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post(BASE_URL + "/subjects/{subjectId}/archive", subjectId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.archived").value(true));
    }

    // Test for POST /subjects/{subjectId}/archive - Subject Not Found
    @Test
    void archiveSubject_SubjectNotFound() throws Exception {
        // Arrange
        int subjectId = 999;
        when(subjectService.archiveSubject(subjectId)).thenThrow(new SubjectNotFoundException("Subject not found"));

        // Act & Assert
        mockMvc.perform(post(BASE_URL + "/subjects/{subjectId}/archive", subjectId))
                .andExpect(status().isNotFound());
    }

    // Test for GET /subjects/{subjectId}
    @Test
    void getSubjectById_Success() throws Exception {
        // Arrange
        int subjectId = 1;
        SubjectResponse response = new SubjectResponse(subjectId, 1, "Physics", "Study of matter and energy", false);

        when(subjectService.getSubjectById(subjectId, false)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get(BASE_URL + "/subjects/{subjectId}", subjectId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.subjectId").value(subjectId))
                .andExpect(jsonPath("$.archived").value(false));
    }

    // Test for GET /users/{userId}/subjects - non-archived
    @Test
    void getNonArchivedSubjectsForUser_Success() throws Exception {
        // Arrange
        int userId = 1;
        List<SubjectResponse> subjects = List.of(
                new SubjectResponse(1, userId, "Math", "...", false),
                new SubjectResponse(2, userId, "English", "...", false)
        );
        when(subjectService.getNonArchivedSubjectsByUserId(userId, false)).thenReturn(subjects);

        // Act & Assert
        mockMvc.perform(get(BASE_URL + "/users/{userId}/subjects", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].archived").value(false));
    }

    // Test for GET /users/{userId}/subjects - archived
    @Test
    void getArchivedSubjectsForUser_Success() throws Exception {
        // Arrange
        int userId = 1;
        List<SubjectResponse> subjects = List.of(
                new SubjectResponse(1, userId, "History", "...", true)
        );
        when(subjectService.getArchivedSubjectsByUserId(userId, false)).thenReturn(subjects);

        // Act & Assert
        mockMvc.perform(get(BASE_URL + "/users/{userId}/subjects", userId)
                        .param("archived", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].archived").value(true));
    }

    // Test for PATCH /subjects/{subjectId}
    @Test
    void updateSubject_Success() throws Exception {
        // Arrange
        int subjectId = 1;
        SubjectRequest request = new SubjectRequest("New Subject", null);

        doNothing().when(subjectService).updateSubject(anyInt(), any(SubjectRequest.class));

        // Act & Assert
        mockMvc.perform(patch(BASE_URL + "/subjects/{subjectId}", subjectId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }
}