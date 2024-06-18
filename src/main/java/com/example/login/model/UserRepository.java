package com.example.login.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * UserRepository är ett gränssnitt för att hantera databasoperationer för användare.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
