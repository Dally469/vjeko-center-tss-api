package com.dev.vjeko_api.controllers;

import com.dev.vjeko_api.custom.ApiResponse;
import com.dev.vjeko_api.entities.Event;
import com.dev.vjeko_api.entities.Team;
import com.dev.vjeko_api.services.AuthService;
import com.dev.vjeko_api.services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {
    @Autowired
    private EventService eventService;

    @Autowired
    private AuthService authService;

    @GetMapping
    public ResponseEntity<ApiResponse> getAllEvents() {
        List<Event> events = eventService.getAllEvents();
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), true, "Fetched all events", events));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getEventById(@PathVariable Long id) {
        try {
            Event event = eventService.getEventById(id);
            return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), true, "Event fetched successfully", event));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(HttpStatus.NOT_FOUND.value(), false, e.getMessage()));
        }
    }

     @PostMapping
    public ResponseEntity<ApiResponse> createEvent(@RequestBody Event event,  @RequestHeader("Authorization") String token) {

         try {
             authService.validateToken(token);  // Validate token without returning a boolean


             if (eventService.eventExistsByTitle(event.getTitle())) {
                 return ResponseEntity.status(HttpStatus.CONFLICT)
                         .body(new ApiResponse(HttpStatus.CONFLICT.value(), false, "Event with this title already exists"));
             }

             Event savedEvent = eventService.saveEvent(event);
             return ResponseEntity.status(HttpStatus.CREATED)
                     .body(new ApiResponse(HttpStatus.CREATED.value(), true, "Event created successfully", savedEvent));
         } catch (Exception e) {
             return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                     .body(new ApiResponse(HttpStatus.UNAUTHORIZED.value(), false, e.getMessage()));
         }
    }

     @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateEvent(@PathVariable Long id, @RequestBody Event event,   @RequestHeader("Authorization") String token) {
        try {
            authService.validateToken(token);  // Validate token without returning a boolean
            Event updatedEvent = eventService.updateEvent(id, event);
            if (updatedEvent != null) {
                return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), true, "Event updated successfully", updatedEvent));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(404, false, "Event not found"));


        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse(HttpStatus.UNAUTHORIZED.value(), false, e.getMessage()));
        }
    }

     @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteEvent(@PathVariable Long id,   @RequestHeader("Authorization") String token) {
        try {
            authService.validateToken(token);  // Validate token without returning a boolean

            eventService.deleteEvent(id);
            return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), true, "Event deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse(HttpStatus.UNAUTHORIZED.value(), false, e.getMessage()));
        }
    }
}