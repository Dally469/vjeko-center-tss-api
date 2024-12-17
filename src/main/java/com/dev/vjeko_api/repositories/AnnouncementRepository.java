package com.dev.vjeko_api.repositories;

import com.dev.vjeko_api.entities.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
    Optional<Announcement> findByTitleAndDate(String title, LocalDateTime date);

    // This query fetches the latest ACTIVE announcement based on the date
    @Query("SELECT a FROM Announcement a WHERE a.status = :status ORDER BY a.date DESC LIMIT 1")
    Optional<Announcement> findLatestActiveAnnouncement(@Param("status") String status);
}