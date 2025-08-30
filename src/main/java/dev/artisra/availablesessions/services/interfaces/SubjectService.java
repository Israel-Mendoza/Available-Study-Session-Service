package dev.artisra.availablesessions.services.interfaces;

import dev.artisra.availablesessions.models.SubjectDTO;

import java.util.List;

public interface SubjectService {
    int createSubject(int userId, String subjectName, String description);
    SubjectDTO getSubjectById(int subjectId, boolean includeTopics);
    List<SubjectDTO> getAllSubjectsForUser(int userId, boolean includeTopics);
    SubjectDTO archiveSubject(int subjectId);
    SubjectDTO unarchiveSubject(int subjectId);
}
