package dev.artisra.availablesessions.unit.repositories;

import dev.artisra.availablesessions.entities.Subject;
import dev.artisra.availablesessions.repositories.SubjectRepository;
import dev.artisra.availablesessions.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@ActiveProfiles("unit")
@Sql(scripts = {"/sql/unit/create_schema.sql", "/sql/unit/insert_data.sql"})
@Sql(scripts = {"/sql/unit/cleanup.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class SubjectRepositoryTest {
    @Autowired
    private SubjectRepository subjectRepository;

    @Test
    public void testFindSubjectById_ExistingSubject() {
        var subject = subjectRepository.findSubjectById(1);
        assert subject.isPresent();
        assert subject.get().getName().equals("Mathematics");
    }

    @Test
    public void testFindSubjectById_NonExistingSubject() {
        var subject = subjectRepository.findSubjectById(999);
        assert subject.isEmpty();
    }

    @Test
    public void testFindSubjectByName_ExistingSubject() {
        var subject = subjectRepository.findSubjectByName("Mathematics");
        assert subject.isPresent();
        assert subject.get().getId() == 1;
    }

    @Test
    public void testFindSubjectByName_NonExistingSubject() {
        var subject = subjectRepository.findSubjectByName("NonExistingSubject");
        assert subject.isEmpty();
    }

    @Test
    public void testFindByUserId_ExistingUser() {
        var subjects = subjectRepository.findByUserId(1001);
        assert subjects.size() == 2;
    }

    @Test
    public void testFindByUserId_NonExistingUser() {
        var subjects = subjectRepository.findByUserId(999);
        assert subjects.isEmpty();
    }

    @Test
    public void testFindByUserIdAndIsArchivedFalse_ExistingUser() {
        var subjects = subjectRepository.findByUserIdAndIsArchivedFalse(1002);
        assert subjects.size() == 2;
    }

    @Test
    public void testFindByUserIdAndIsArchivedTrue_ExistingUser() {
        var subjects = subjectRepository.findByUserIdAndIsArchivedTrue(1002);
        assert subjects.size() == 1;
        assert subjects.get(0).getName().equals("Sanskrit");
    }

    @Test
    public void testExistsById_ExistingSubject() {
        boolean exists = subjectRepository.existsById(1);
        assert exists;
    }

    @Test
    public void testExistsById_NonExistingSubject() {
        boolean exists = subjectRepository.existsById(999);
        assert !exists;
    }

    @Test
    public void testExistsSubjectByName_ExistingSubject() {
        boolean exists1 = subjectRepository.existsSubjectByUserIdAndName(1001, "Mathematics");
        boolean exists2 = subjectRepository.existsSubjectByUserIdAndName(1001, "English");
        boolean exists3 = subjectRepository.existsSubjectByUserIdAndName(1002, "French");
        assert exists1;
        assert exists2;
        assert exists3;
    }

    @Test
    public void testExistsSubjectByName_NonExistingSubject() {
        boolean exists1 = subjectRepository.existsSubjectByUserIdAndName(1001, "NonExistingSubject");
        boolean exists2 = subjectRepository.existsSubjectByUserIdAndName(999, "Mathematics");
        assert !exists1;
        assert !exists2;
    }

    @Test
    public void testDeleteById_ExistingSubject() {
        boolean existsBefore = subjectRepository.existsById(1);
        assert existsBefore;

        subjectRepository.deleteById(1);

        boolean existsAfter = subjectRepository.existsById(1);
        assert !existsAfter;
    }
}
