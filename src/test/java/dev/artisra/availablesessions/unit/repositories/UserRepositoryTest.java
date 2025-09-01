package dev.artisra.availablesessions.unit.repositories;

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
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindUserById_ExistingUser() {
        var user = userRepository.findUserById(1001);
        assert user.isPresent();
        assert user.get().getId() == 1001;
    }

    @Test
    public void testFindUserById_NonExistingUser() {
        var user = userRepository.findUserById(9999);
        assert user.isEmpty();
    }

    @Test
    public void testExistsById_ExistingUser() {
        boolean exists = userRepository.existsById(1001);
        assert exists;
    }

    @Test
    public void testExistsById_NonExistingUser() {
        boolean exists = userRepository.existsById(9999);
        assert !exists;
    }

    @Test
    public void testDeleteById_ExistingUser() {
        // Confirm user exists
        var user = userRepository.findUserById(1001);
        assert user.isPresent();

        // Delete the user
        userRepository.deleteById(1001);

        // Confirm user is deleted
        user = userRepository.findUserById(1001);
        assert user.isEmpty();
    }

    @Test
    public void testDeleteById_NonExistingUser() {
        // Attempt to delete a non-existing user
        userRepository.deleteById(9999);
        // No exception should be thrown
    }
}
