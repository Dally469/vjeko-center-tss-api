package com.dev.vjeko_api.services;

import com.dev.vjeko_api.entities.Event;
import com.dev.vjeko_api.repositories.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventService {
    @Autowired
    private EventRepository eventRepository;

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Event getEventById(Long id) {
        return eventRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Event not found with id: " + id));
    }

    @Transactional
    public Event saveEvent(Event event) {
        return eventRepository.save(event);
    }

    @Transactional
    public Event updateEvent(Long id, Event eventDetails) {
        Event event = getEventById(id);

        // Only update fields that are not null or empty in the incoming request
        if (eventDetails.getImg() != null && !eventDetails.getImg().isEmpty()) {
            event.setImg(eventDetails.getImg());
        }
        if (eventDetails.getBadge() != null && !eventDetails.getBadge().isEmpty()) {
            event.setBadge(eventDetails.getBadge());
        }
        if (eventDetails.getDate() != null && !eventDetails.getDate().isEmpty()) {
            event.setDate(eventDetails.getDate());
        }
        if (eventDetails.getView() != null && !eventDetails.getView().isEmpty()) {
            event.setView(eventDetails.getView());
        }
        if (eventDetails.getTitle() != null && !eventDetails.getTitle().isEmpty()) {
            event.setTitle(eventDetails.getTitle());
        }
        if (eventDetails.getDescription() != null && !eventDetails.getDescription().isEmpty()) {
            event.setDescription(eventDetails.getDescription());
        }
        if (eventDetails.getPostedBy() != null && !eventDetails.getPostedBy().isEmpty()) {
            event.setPostedBy(eventDetails.getPostedBy());
        }

        return eventRepository.save(event);
    }

    @Transactional
    public void deleteEvent(Long id) {
        Event event = getEventById(id);
        eventRepository.delete(event);
    }

    public boolean eventExistsByTitle(String title) {
        Optional<Event> event = eventRepository.findByTitle(title);
        return event.isPresent();
    }
}