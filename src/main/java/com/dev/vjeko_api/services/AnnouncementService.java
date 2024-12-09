package com.dev.vjeko_api.services;

import com.dev.vjeko_api.entities.Announcement;
import com.dev.vjeko_api.repositories.AnnouncementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AnnouncementService {
    private  AnnouncementRepository announcementRepository;

    public List<Announcement> getAllAnnouncements() {
        return announcementRepository.findAll();
    }

    public Announcement getAnnouncementById(Long id) {
        return announcementRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Announcement not found with id: " + id));
    }

    @Transactional
    public Announcement saveAnnouncement(Announcement announcement) {
        checkIfAnnouncementExists(announcement);
        return announcementRepository.save(announcement);
    }

    @Transactional
    public Announcement updateAnnouncement(Long id, Announcement announcementDetails) {
        Announcement announcement = getAnnouncementById(id);
        checkIfAnnouncementExists(announcementDetails);

        // Conditionally update only provided fields
        if (announcementDetails.getTitle() != null && !announcementDetails.getTitle().isEmpty()) {
            announcement.setTitle(announcementDetails.getTitle());
        }
        if (announcementDetails.getContent() != null && !announcementDetails.getContent().isEmpty()) {
            announcement.setContent(announcementDetails.getContent());
        }
        if (announcementDetails.getDate() != null) {
            announcement.setDate(announcementDetails.getDate());
        }

        return announcementRepository.save(announcement);
    }

    @Transactional
    public void deleteAnnouncement(Long id) {
        Announcement announcement = getAnnouncementById(id);
        announcementRepository.delete(announcement);
    }

    private void checkIfAnnouncementExists(Announcement announcement) {
        Optional<Announcement> existingAnnouncement = announcementRepository.findByTitleAndDate(announcement.getTitle(), announcement.getDate());
        if (existingAnnouncement.isPresent() && !existingAnnouncement.get().getId().equals(announcement.getId())) {
            throw new IllegalArgumentException("An announcement with the same title and date already exists.");
        }
    }
}