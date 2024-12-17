package com.dev.vjeko_api.controllers;

import com.dev.vjeko_api.custom.ApiResponse;
import com.dev.vjeko_api.entities.Announcement;
import com.dev.vjeko_api.services.AuthService;
import com.dev.vjeko_api.services.AnnouncementService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/announcements")
@RequiredArgsConstructor
public class AnnouncementController {

    @Autowired
    private  AnnouncementService announcementService;

    @Autowired
    private  AuthService authService;



    @GetMapping("/latest")
    public ResponseEntity<ApiResponse> getLatestActiveAnnouncement() {
        try {
            Announcement latestAnnouncement = announcementService.getLatestActiveAnnouncement();
            return ResponseEntity.ok(
                    new ApiResponse(HttpStatus.OK.value(), true, "Fetched latest ACTIVE announcement", latestAnnouncement)
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(HttpStatus.NOT_FOUND.value(), false, e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllAnnouncements() {
        List<Announcement> announcements = announcementService.getAllAnnouncements();
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), true, "Fetched all announcements", announcements));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getAnnouncementById(@PathVariable Long id) {
        try {
            Announcement announcement = announcementService.getAnnouncementById(id);
            return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), true, "Announcement fetched successfully", announcement));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(HttpStatus.NOT_FOUND.value(), false, e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createAnnouncement(@RequestBody Announcement announcement, @RequestHeader("Authorization") String token) {
        try {
            authService.validateToken(token);
            Announcement savedAnnouncement = announcementService.saveAnnouncement(announcement);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse(HttpStatus.CREATED.value(), true, "Announcement created successfully", savedAnnouncement));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse(HttpStatus.UNAUTHORIZED.value(), false, e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateAnnouncement(@PathVariable Long id, @RequestBody Announcement announcement, @RequestHeader("Authorization") String token) {
        try {
            authService.validateToken(token);
            Announcement updatedAnnouncement = announcementService.updateAnnouncement(id, announcement);
            if (updatedAnnouncement != null) {
                return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), true, "Announcement updated successfully", updatedAnnouncement));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(HttpStatus.NOT_FOUND.value(), false, "Announcement not found"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse(HttpStatus.UNAUTHORIZED.value(), false, e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteAnnouncement(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        try {
            authService.validateToken(token);
            announcementService.deleteAnnouncement(id);
            return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), true, "Announcement deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse(HttpStatus.UNAUTHORIZED.value(), false, e.getMessage()));
        }
    }
}