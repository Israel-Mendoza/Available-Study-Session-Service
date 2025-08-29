package dev.artisra.availablesessions.entities;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

// User.java
@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "id")
    private int id;

    // A user can have multiple subjects. The 'mappedBy' attribute indicates
    // that the 'subjects' field is owned by the 'user' field in the Subject entity.
    // CascadeType.ALL means all operations (persist, merge, remove, etc.) will
    // cascade from the User entity to its associated Subject entities.
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Subject> subjects = new HashSet<>();

    public User() {
    }

    public User(int id) {
        this.id = id;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Set<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(Set<Subject> subjects) {
        this.subjects = subjects;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                '}';
    }
}