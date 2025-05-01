package com.backenddiploma.repositories.security;

import com.backenddiploma.models.security.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByUsername(String username);
    Boolean existsByEmail(String email);
    Boolean existsByUsername(String username);
}
