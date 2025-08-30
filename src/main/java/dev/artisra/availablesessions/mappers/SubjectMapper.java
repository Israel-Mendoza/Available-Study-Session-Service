package dev.artisra.availablesessions.mappers;

import dev.artisra.availablesessions.entities.Subject;
import dev.artisra.availablesessions.models.SubjectDTO;
import org.springframework.stereotype.Component;

@Component
public class SubjectMapper {
    public SubjectDTO subjectToSubjectDTO(Subject subject) {
        return new SubjectDTO(
                subject.getId(),
                subject.getUser().getId(),
                subject.getName(),
                subject.getDescription(),
                subject.isArchived()
        );
    }
}
