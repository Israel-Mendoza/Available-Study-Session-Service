package dev.artisra.availablesessions.services.interfaces;

import dev.artisra.availablesessions.models.res.SubjectResponse;
import dev.artisra.availablesessions.models.req.SubjectRequest;

import java.util.List;

public interface SubjectService {
    int createSubject(int userId, String subjectName, String description);
    SubjectResponse getSubjectById(int subjectId, boolean includeTopics);
    List<SubjectResponse> getNonArchivedSubjectsByUserId(int userId, boolean includeTopics);
    List<SubjectResponse> getArchivedSubjectsByUserId(int userId, boolean includeTopics);
    List<SubjectResponse> getAllSubjectsByUserId(int userId, boolean includeTopics);
    void updateSubject(int subjectId, SubjectRequest subjectRequest);
    SubjectResponse archiveSubject(int subjectId);
    SubjectResponse unarchiveSubject(int subjectId);
}
