package dev.artisra.availablesessions.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integration")
@Sql(scripts = {"/sql/integration/create_schema.sql", "/sql/integration/insert_data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"/sql/integration/cleanup.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class IntegrationApplicationTest {

    private static final String BASE_URL = "/api/v1/available-sessions";

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void contextLoads() {
    }

    @Test
    void testCreateNewSubjectForUser_Expect201() throws Exception {
        var requestBody = """
                {
                    "subject": "Biology",
                    "description": "Study of living organisms"
                }
                """;

        var request = MockMvcRequestBuilders
                .post(BASE_URL + "/users/1001/subjects")
                .contentType("application/json").content(requestBody);

        mockMvc.perform(request).andExpectAll(
                MockMvcResultMatchers
                        .status()
                        .isCreated(),
                MockMvcResultMatchers
                        .jsonPath("$")
                        .value(8),
                MockMvcResultMatchers
                        .header()
                        .string("Location",
                                org.hamcrest.Matchers.containsString(BASE_URL + "/subjects/8"))
        );
    }

    @Test
    public void testCreateNewSubject_WithoutDescription_Expect201() throws Exception {
        String newSubjectJson = """
                {
                    "subject": "Italian",
                    "description": ""
                }
                """;

        var request = MockMvcRequestBuilders
                .post(BASE_URL + "/users/1002/subjects")
                .contentType("application/json")
                .content(newSubjectJson);

        mockMvc.perform(request)
                .andExpectAll(
                        MockMvcResultMatchers
                                .status()
                                .isCreated(),
                        MockMvcResultMatchers
                                .jsonPath("$")
                                .value(8),
                        MockMvcResultMatchers
                                .header()
                                .string("Location",
                                        org.hamcrest.Matchers.containsString(BASE_URL + "/subjects/8"))
                );

    }

    @Test
    void testCreateNewSubjectForUser_MissingSubjectField_Expect400() throws Exception {
        var requestBody = """
                {
                    "description": "Study of living organisms"
                }
                """;

        String path = BASE_URL + "/users/1001/subjects";

        var request = MockMvcRequestBuilders
                .post(path)
                .contentType("application/json").content(requestBody);

        mockMvc.perform(request).andExpectAll(
                MockMvcResultMatchers
                        .status()
                        .isBadRequest(),
                MockMvcResultMatchers
                        .jsonPath("$.fieldErrors.subject")
                        .value("Subject cannot be null"),
                MockMvcResultMatchers
                        .jsonPath("$.message")
                        .value("Validation failed"),
                MockMvcResultMatchers
                        .jsonPath("$.status")
                        .value(400),
                MockMvcResultMatchers
                        .jsonPath("$.error")
                        .value("Bad Request"),
                MockMvcResultMatchers
                        .jsonPath("$.path")
                        .value(path)
        );
    }

    @Test
    void testCreateNewSubjectForUser_SubjectFieldIsNull_Expect400() throws Exception {
        var requestBody = """
                {
                    "subject": null,
                    "description": "Study of living organisms"
                }
                """;

        String path = BASE_URL + "/users/1001/subjects";

        var request = MockMvcRequestBuilders
                .post(path)
                .contentType("application/json").content(requestBody);

        mockMvc.perform(request).andExpectAll(
                MockMvcResultMatchers
                        .status()
                        .isBadRequest(),
                MockMvcResultMatchers
                        .jsonPath("$.fieldErrors.subject")
                        .value("Subject cannot be null")
        );
    }

    @Test
    void testCreateNewSubjectForUser_SubjectTooShort_Expect400() throws Exception {
        var requestBody = """
                {
                    "subject": "A",
                    "description": "Study of living organisms"
                }
                """;

        String path = BASE_URL + "/users/1001/subjects";

        var request = MockMvcRequestBuilders
                .post(path)
                .contentType("application/json").content(requestBody);

        mockMvc.perform(request).andExpectAll(
                MockMvcResultMatchers
                        .status()
                        .isBadRequest(),
                MockMvcResultMatchers
                        .jsonPath("$.fieldErrors.subject")
                        .value("Subject must be between 2 and 100 characters")
        );
    }

    @Test
    void testCreateNewSubjectForUser_SubjectTooLong_Expect400() throws Exception {
        var requestBody = """
                {
                     "subject": "This subject line must have more that one hundred characters in order for the application to refuse it...",
                     "description": "The German language"
                }
                """;

        String path = BASE_URL + "/users/1001/subjects";

        var request = MockMvcRequestBuilders
                .post(path)
                .contentType("application/json").content(requestBody);

        mockMvc.perform(request)
                .andExpectAll(
                        MockMvcResultMatchers.status().isBadRequest(),
                        MockMvcResultMatchers
                                .jsonPath("$.fieldErrors.subject")
                                .value("Subject must be between 2 and 100 characters"),
                        MockMvcResultMatchers
                                .jsonPath("$.fieldErrors.description").doesNotExist(),
                        MockMvcResultMatchers
                                .jsonPath("$.message").value("Validation failed")
                );
    }

    @Test
    void testCreateNewSubjectForUser_DescriptionTooLong_Expect400() throws Exception {
        String requestBody = """
                {
                    "subject": "German",
                    "description": "This description must have more than five hundred characters.Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum. This description must have more than five hundred characters. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
                }
               """;

        String path = BASE_URL + "/users/1001/subjects";

        var request = MockMvcRequestBuilders
                .post(path)
                .contentType("application/json").content(requestBody);

        mockMvc.perform(request)
                .andExpectAll(
                        MockMvcResultMatchers.status().isBadRequest(),
                        MockMvcResultMatchers
                                .jsonPath("$.fieldErrors.description")
                                .value("Description cannot exceed 500 characters"),
                        MockMvcResultMatchers
                                .jsonPath("$.fieldErrors.subject").doesNotExist(),
                        MockMvcResultMatchers
                                .jsonPath("$.message").value("Validation failed")
                );
    }

    @Test
    void testCreateNewSubjectForUser_DescriptionIsNull_Expect400() throws Exception {
        var requestBody = """
                {
                    "subject": "Biology",
                    "description": null
                }
                """;

        String path = BASE_URL + "/users/1001/subjects";

        var request = MockMvcRequestBuilders
                .post(path)
                .contentType("application/json").content(requestBody);

        mockMvc.perform(request).andExpectAll(
                MockMvcResultMatchers
                        .status()
                        .isBadRequest(),
                MockMvcResultMatchers
                        .jsonPath("$.fieldErrors.description")
                        .value("Description cannot be null")
        );
    }

    @Test
    void testCreateExistingSubjectForUser_Expect409() throws Exception {
        var requestBody = """
                {
                    "subject": "Mathematics",
                    "description": "Study of numbers, shapes, and patterns"
                }
                """;

        String path = BASE_URL + "/users/1001/subjects";

        var request = MockMvcRequestBuilders
                .post(path)
                .contentType("application/json").content(requestBody);

        mockMvc.perform(request).andExpectAll(
                MockMvcResultMatchers
                        .status()
                        .isConflict(),
                MockMvcResultMatchers
                        .jsonPath("$.message")
                        .value("Subject with name 'Mathematics' already exists for user ID 1001."),
                MockMvcResultMatchers
                        .jsonPath("$.status")
                        .value(409),
                MockMvcResultMatchers
                        .jsonPath("$.error")
                        .value("Conflict"),
                MockMvcResultMatchers
                        .jsonPath("$.path")
                        .value(path)
        );
    }

    @Test
    void testRetrieveSubjectById_Expect200() throws Exception {
        var request = MockMvcRequestBuilders
                .get(BASE_URL + "/subjects/1")
                .contentType("application/json");
        mockMvc.perform(request).andExpectAll(
                MockMvcResultMatchers.status().isOk(),
                MockMvcResultMatchers.jsonPath("$.subjectId").value(1),
                MockMvcResultMatchers.jsonPath("$.name").value("Mathematics"),
                MockMvcResultMatchers.jsonPath("$.archived").value(false),
                MockMvcResultMatchers.jsonPath("$.description").value("All about numbers and equations"),
                MockMvcResultMatchers.jsonPath("$.userId").value(1001)
        );
    }

    @Test
    void testRetrieveUnarchivedSubjectsForUser_Expect200() throws Exception {
        var request = MockMvcRequestBuilders
                .get(BASE_URL + "/users/1002/subjects")
                .contentType("application/json");
        mockMvc.perform(request).andExpectAll(
                MockMvcResultMatchers.status().isOk(),
                MockMvcResultMatchers.jsonPath("$.length()").value(2),
                MockMvcResultMatchers.jsonPath("$[0].subjectId").value(3),
                MockMvcResultMatchers.jsonPath("$[0].name").value("Spanish"),
                MockMvcResultMatchers.jsonPath("$[0].archived").value(false),
                MockMvcResultMatchers.jsonPath("$[1].subjectId").value(4),
                MockMvcResultMatchers.jsonPath("$[1].name").value("French"),
                MockMvcResultMatchers.jsonPath("$[1].archived").value(false)
        );
    }

    @Test
    void testRetrieveArchivedSubjectsForUser_Expect200() throws Exception {
        var request = MockMvcRequestBuilders
                .get(BASE_URL + "/users/1002/subjects?archived=true")
                .contentType("application/json");
        mockMvc.perform(request).andExpectAll(
                MockMvcResultMatchers.status().isOk(),
                MockMvcResultMatchers.jsonPath("$.length()").value(1),
                MockMvcResultMatchers.jsonPath("$[0].subjectId").value(5),
                MockMvcResultMatchers.jsonPath("$[0].name").value("Sanskrit"),
                MockMvcResultMatchers.jsonPath("$[0].archived").value(true)
        );
    }

    @Test
    void testRetrieveArchivedSubjectsForUserWithNoArchivedSubjects_Expect200() throws Exception {
        var request = MockMvcRequestBuilders.get(BASE_URL + "/users/1001/subjects?archived=true").contentType("application/json");
        mockMvc.perform(request).andExpectAll(
                MockMvcResultMatchers.status().isOk(),
                MockMvcResultMatchers.jsonPath("$.length()").value(0)
        );
    }

    @Test
    void testRetrieveSubjectsForNonExistingUser_Expect404() throws Exception {
        String path = BASE_URL + "/users/9999/subjects";
        var request = MockMvcRequestBuilders.get(path).contentType("application/json");
        mockMvc.perform(request).andExpectAll(
                MockMvcResultMatchers.status().isNotFound(),
                MockMvcResultMatchers.jsonPath("$.message").value("User with ID 9999 does not exist."),
                MockMvcResultMatchers.jsonPath("$.status").value(404),
                MockMvcResultMatchers.jsonPath("$.error").value("Not Found"),
                MockMvcResultMatchers.jsonPath("$.path").value(path)
        );
    }

    @Test
    void testCreateNewTopicForSubject_Expect201() throws Exception {
        var requestBody = """
                {
                    "topic": "Listening",
                    "description": "Passive listening - Podcasts, audio books, etc."
                }
                """;

        var request = MockMvcRequestBuilders
                .post(BASE_URL + "/subjects/3/topics")
                .contentType("application/json").content(requestBody);

        mockMvc.perform(request).andExpectAll(
                MockMvcResultMatchers
                        .status()
                        .isCreated(),
                MockMvcResultMatchers
                        .jsonPath("$")
                        .value(19),
                MockMvcResultMatchers
                        .header()
                        .string("Location",
                                org.hamcrest.Matchers.containsString(BASE_URL + "/topics/19"))
        );
    }

    @Test
    void testCreateNewTopic_WithoutDescription_Expect201() throws Exception {
        String newTopicJson = """
                {
                    "topic": "Listening",
                    "description": ""
                }
                """;

        var request = MockMvcRequestBuilders
                .post(BASE_URL + "/subjects/3/topics")
                .contentType("application/json")
                .content(newTopicJson);

        mockMvc.perform(request)
                .andExpectAll(
                        MockMvcResultMatchers
                                .status()
                                .isCreated(),
                        MockMvcResultMatchers
                                .jsonPath("$")
                                .value(19),
                        MockMvcResultMatchers
                                .header()
                                .string("Location",
                                        org.hamcrest.Matchers.containsString(BASE_URL + "/topics/19"))
                );

    }

    @Test
    void testCreateNewTopicForSubject_MissingTopicField_Expect400() throws Exception {
        var requestBody = """
                {
                    "description": "Passive listening - Podcasts, audio books, etc."
                }
                """;

        String path = BASE_URL + "/subjects/3/topics";

        var request = MockMvcRequestBuilders
                .post(path)
                .contentType("application/json").content(requestBody);

        mockMvc.perform(request).andExpectAll(
                MockMvcResultMatchers
                        .status()
                        .isBadRequest(),
                MockMvcResultMatchers
                        .jsonPath("$.fieldErrors.topic")
                        .value("Topic cannot be null"),
                MockMvcResultMatchers
                        .jsonPath("$.message")
                        .value("Validation failed"),
                MockMvcResultMatchers
                        .jsonPath("$.status")
                        .value(400),
                MockMvcResultMatchers
                        .jsonPath("$.error")
                        .value("Bad Request"),
                MockMvcResultMatchers
                        .jsonPath("$.path")
                        .value(path)
        );
    }

    @Test
    void testCreateNewTopicForSubject_TopicFieldIsNull_Expect400() throws Exception {
        var requestBody = """
                {
                    "topic": null,
                    "description": "Passive listening - Podcasts, audio books, etc."
                }
                """;

        String path = BASE_URL + "/subjects/3/topics";

        var request = MockMvcRequestBuilders
                .post(path)
                .contentType("application/json").content(requestBody);

        mockMvc.perform(request).andExpectAll(
                MockMvcResultMatchers
                        .status()
                        .isBadRequest(),
                MockMvcResultMatchers
                        .jsonPath("$.fieldErrors.topic")
                        .value("Topic cannot be null")
        );
    }

    @Test
    void testCreateNewTopicForSubject_TopicTooShort_Expect400() throws Exception {
        var requestBody = """
                {
                    "topic": "A",
                    "description": "Passive listening - Podcasts, audio books, etc."
                }
                """;

        String path = BASE_URL + "/subjects/3/topics";

        var request = MockMvcRequestBuilders
                .post(path)
                .contentType("application/json").content(requestBody);

        mockMvc.perform(request).andExpectAll(
                MockMvcResultMatchers
                        .status()
                        .isBadRequest(),
                MockMvcResultMatchers
                        .jsonPath("$.fieldErrors.topic")
                        .value("Topic cannot exceed 100 characters")
        );
    }

    @Test
    void testCreateNewTopicForSubject_TopicTooLong_Expect400() throws Exception {
        var requestBody = """
                {
                     "topic": "This topic line must have more that one hundred characters in order for the application to refuse it...",
                     "description": "Passive listening - Podcasts, audio books, etc."
                }
                """;

        String path = BASE_URL + "/subjects/3/topics";

        var request = MockMvcRequestBuilders
                .post(path)
                .contentType("application/json").content(requestBody);

        mockMvc.perform(request)
                .andExpectAll(
                        MockMvcResultMatchers.status().isBadRequest(),
                        MockMvcResultMatchers
                                .jsonPath("$.fieldErrors.topic")
                                .value("Topic cannot exceed 100 characters"),
                        MockMvcResultMatchers
                                .jsonPath("$.fieldErrors.description").doesNotExist(),
                        MockMvcResultMatchers
                                .jsonPath("$.message").value("Validation failed")
                );
    }

    @Test
    void testCreateNewTopicForSubject_DescriptionTooLong_Expect400() throws Exception {
        String requestBody = """
                {
                    "topic": "Listening",
                    "description": "This description must have more than five hundred characters.Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum. This description must have more than five hundred characters. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
                }
               """;

        String path = BASE_URL + "/subjects/3/topics";

        var request = MockMvcRequestBuilders
                .post(path)
                .contentType("application/json").content(requestBody);

        mockMvc.perform(request)
                .andExpectAll(
                        MockMvcResultMatchers.status().isBadRequest(),
                        MockMvcResultMatchers
                                .jsonPath("$.fieldErrors.description")
                                .value("Description cannot exceed 500 characters"),
                        MockMvcResultMatchers
                                .jsonPath("$.fieldErrors.topic").doesNotExist(),
                        MockMvcResultMatchers
                                .jsonPath("$.message").value("Validation failed")
                );
    }

    @Test
    void testCreateNewTopicForSubject_DescriptionIsNull_Expect400() throws Exception {
        var requestBody = """
                {
                    "topic": "Listening",
                    "description": null
                }
                """;

        String path = BASE_URL + "/subjects/3/topics";

        var request = MockMvcRequestBuilders
                .post(path)
                .contentType("application/json").content(requestBody);

        mockMvc.perform(request).andExpectAll(
                MockMvcResultMatchers
                        .status()
                        .isBadRequest(),
                MockMvcResultMatchers
                        .jsonPath("$.fieldErrors.description")
                        .value("Description cannot be null")
        );
    }

    @Test
    void testCreateExistingTopicForSubject_Expect409() throws Exception {
        var requestBody = """
                {
                    "topic": "Grammar",
                    "description": "Study of the structure and rules of a language"
                }
                """;

        String path = BASE_URL + "/subjects/3/topics";

        var request = MockMvcRequestBuilders
                .post(path)
                .contentType("application/json").content(requestBody);

        mockMvc.perform(request).andExpectAll(
                MockMvcResultMatchers
                        .status()
                        .isConflict(),
                MockMvcResultMatchers
                        .jsonPath("$.message")
                        .value("Topic 'Grammar' already exists for subject ID 3"),
                MockMvcResultMatchers
                        .jsonPath("$.status")
                        .value(409),
                MockMvcResultMatchers
                        .jsonPath("$.error")
                        .value("Conflict"),
                MockMvcResultMatchers
                        .jsonPath("$.path")
                        .value(path)
        );
    }

    @Test
    void testRetrieveTopicById_Expect200() throws Exception {
        var request = MockMvcRequestBuilders
                .get(BASE_URL + "/topics/1")
                .contentType("application/json");
        mockMvc.perform(request).andExpectAll(
                MockMvcResultMatchers.status().isOk(),
                MockMvcResultMatchers.jsonPath("$.topicId").value(1),
                MockMvcResultMatchers.jsonPath("$.name").value("Algebra"),
                MockMvcResultMatchers.jsonPath("$.description").value("Study of mathematical symbols and rules"),
                MockMvcResultMatchers.jsonPath("$.subjectId").value(1)
        );
    }

    @Test
    void testRetrieveNonExistingTopicById_Expect404() throws Exception {
        String path = BASE_URL + "/topics/9999";
        var request = MockMvcRequestBuilders.get(path).contentType("application/json");
        mockMvc.perform(request).andExpectAll(
                MockMvcResultMatchers.status().isNotFound(),
                MockMvcResultMatchers.jsonPath("$.message").value("Topic with ID 9999 not found."),
                MockMvcResultMatchers.jsonPath("$.status").value(404),
                MockMvcResultMatchers.jsonPath("$.error").value("Not Found"),
                MockMvcResultMatchers.jsonPath("$.path").value(path)
        );
    }

    @Test
    void testRetrieveNonExistingSubjectById_Expect404() throws Exception {
        String path = BASE_URL + "/subjects/9999";
        var request = MockMvcRequestBuilders.get(path).contentType("application/json");
        mockMvc.perform(request).andExpectAll(
                MockMvcResultMatchers.status().isNotFound(),
                MockMvcResultMatchers.jsonPath("$.message").value("Subject with ID 9999 not found."),
                MockMvcResultMatchers.jsonPath("$.status").value(404),
                MockMvcResultMatchers.jsonPath("$.error").value("Not Found"),
                MockMvcResultMatchers.jsonPath("$.path").value(path)
        );
    }

    @Test
    void testRetrieveAllTopicsForSubject_Expect200() throws Exception {
        var request = MockMvcRequestBuilders.get(BASE_URL + "/subjects/1/topics").contentType("application/json");
        mockMvc.perform(request).andExpectAll(
                MockMvcResultMatchers.status().isOk(),
                MockMvcResultMatchers.jsonPath("$.length()").value(3),
                MockMvcResultMatchers.jsonPath("$[0].topicId").value(1),
                MockMvcResultMatchers.jsonPath("$[0].name").value("Algebra"),
                MockMvcResultMatchers.jsonPath("$[1].topicId").value(2),
                MockMvcResultMatchers.jsonPath("$[1].name").value("Geometry")
        );
    }

    // TODO: Add more tests (e.g., patching subjects and topics, retrieving subjects w/topics, deleting subjects and topics, etc.)
}
