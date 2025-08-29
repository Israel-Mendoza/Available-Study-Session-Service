package dev.artisra.availablesessions.services.impl;

import dev.artisra.availablesessions.entities.User;
import dev.artisra.availablesessions.repositories.UserRepository;
import dev.artisra.availablesessions.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;

public class DbUserService implements UserService {

    private final UserRepository userRepository;

    public DbUserService(@Autowired UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public int createUser(int externalId) {
        if (userRepository.existsById(externalId)) {
            return -1; // User already exists
        }
        User user = new User();
        user.setId(externalId);
        return userRepository.save(user).getId();
    }

    @Override
    public boolean userExists(int externalId) {
        return userRepository.existsById(externalId);
    }

    @Override
    public boolean deleteUser(int externalId) {
        return userRepository.deleteById(externalId);
    }
}
