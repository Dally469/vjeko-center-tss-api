package com.dev.vjeko_api.repositories;

import com.dev.vjeko_api.entities.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {
    Optional<Team> findByNameAndRole(String name, String role);
    Optional<Team> findByEmail(String email);  // Finding by email example
    Optional<Team> findByPhone(String phone);  // Optional based on phone


}