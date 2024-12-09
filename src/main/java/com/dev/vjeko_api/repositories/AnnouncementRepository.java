package com.dev.vjeko_api.repositories;

import com.dev.vjeko_api.entities.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.Optional;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
    Optional<Announcement> findByTitleAndDate(String title, Date date);
}