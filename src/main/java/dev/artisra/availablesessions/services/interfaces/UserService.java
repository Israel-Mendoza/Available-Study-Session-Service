package dev.artisra.availablesessions.services.interfaces;


public interface UserService {
    int createUser(int externalId);
    boolean userExists(int externalId);
    boolean deleteUser(int externalId);
}
