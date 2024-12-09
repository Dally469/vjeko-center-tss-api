package com.dev.vjeko_api.repositories;

import com.dev.vjeko_api.entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findByTitle(String title);

}