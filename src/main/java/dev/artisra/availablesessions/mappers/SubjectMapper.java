package dev.artisra.availablesessions.mappers;

import dev.artisra.availablesessions.entities.Subject;
import dev.artisra.availablesessions.models.res.SubjectResponse;
import org.springframework.stereotype.Component;

@Component
public class SubjectMapper {
    public SubjectResponse subjectToSubjectDTO(Subject subject) {
        return new SubjectResponse(
                subject.getId(),
                subject.getUser().getId(),
                subject.getName(),
                subject.getDescription(),
                subject.isArchived()
        );
    }
}
