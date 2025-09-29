package com.events.eventxx.controller;

import com.events.eventxx.Dto.NewEventDto;
import com.events.eventxx.model.Event;
import com.events.eventxx.model.EventDto;
import com.events.eventxx.repository.EventRepository;
import com.events.eventxx.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class EventController {
    @Autowired
    EventService eventService;

    @Autowired
    EventRepository eventRepository;

    @PostMapping("/event")
    public ResponseEntity newEvent(@RequestBody EventDto eventDto) throws Exception {
        if (!eventDto.getPassword().equals("2606")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        try {
            eventService.saveEvent(eventDto);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception ex) {
            throw new Exception("error", ex);
        }

    }

    @GetMapping("/event")
    public ResponseEntity<NewEventDto> getEvent(@RequestParam String id) {
        try {
            NewEventDto eventDto = new NewEventDto();
            Event event = eventRepository.findByCodEvent(id);
            if (event != null) {
                eventDto.setCodeEvento(event.getCodEvent());
                return ResponseEntity.ok(eventDto);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}


