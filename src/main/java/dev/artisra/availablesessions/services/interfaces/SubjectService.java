package dev.artisra.availablesessions.services.interfaces;

import dev.artisra.availablesessions.models.SubjectDTO;

public interface SubjectService {
    int createSubject(int userId, String subjectName, String description);
    SubjectDTO getSubject(int subjectId);
    boolean archiveSubject(int subjectId);
}
