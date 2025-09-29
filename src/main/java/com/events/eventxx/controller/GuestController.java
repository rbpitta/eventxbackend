package com.events.eventxx.controller;

import com.events.eventxx.model.Companion;
import com.events.eventxx.model.Event;
import com.events.eventxx.model.Guest;
import com.events.eventxx.repository.CompanionRepository;
import com.events.eventxx.repository.EventRepository;
import com.events.eventxx.repository.GuestRepository;
import com.events.eventxx.service.EventService;
import com.events.eventxx.service.ReportGuestService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/guest")
public class GuestController {

    @Autowired
    GuestRepository guestRepository;
    @Autowired
    CompanionRepository companionRepository;
    @Autowired
    EventRepository eventRepository;
    @Autowired
    EventService eventService;
    @Autowired
    ReportGuestService reportGuestService;

    @GetMapping()
    public ResponseEntity<Page<Guest>> getGuestsByEventId(@RequestParam String id, Pageable pageable) {
        Event event = eventRepository.findByCodEvent(id);
        if (event == null) {
            return ResponseEntity.notFound().build();
        }
        Page<Guest> pageGuest = guestRepository.findByEvent_CodEvent(id, pageable);
        return ResponseEntity.ok(pageGuest);
    }

    @PostMapping("/{idEvent}")
    public ResponseEntity saveGuest(@RequestBody Guest guest, @PathVariable String idEvent) {
        Event event = eventRepository.findByCodEvent(idEvent);
        Companion companion = new Companion();
        if (guest.getId() != null) {
            Optional<Guest> guest1 = guestRepository.findById(guest.getId());
            companion.setNome(guest.getNome());
            if (guest.getEmail() != null) {
                companion.setEmail(guest.getEmail());
            }
            companion.setPresent(guest.getPresent());
            companion.setGuest(guest1.get());
            guest1.get().getCompanionList().add(companion);
            try {
                Guest savedGuest = guestRepository.save(guest1.get());

                Companion companionSalvo =
                        savedGuest.getCompanionList()
                                .get(savedGuest.getCompanionList().size() - 1);
                if (companion.getEmail() != null) {
                    sendQrcodeCompanion(companionSalvo, event);
                }

                return ResponseEntity.ok().build();
            } catch (Exception ex) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        } else {
            try {
                if (event == null) {
                    return ResponseEntity.notFound().build();
                }
                guest.setEvent(event);
                guest = guestRepository.save(guest);
                if (guest.getEmail() != null) {
                    sendQrcode(guest, event);
                }
                return ResponseEntity.ok().build();
            } catch (Exception ex) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        }
    }

    @GetMapping("/present-count")
    public ResponseEntity<Integer> getPresentCount(@RequestParam String idEvent) {
        int presentCount = guestRepository.countGuestsByEventAndPresence(idEvent);
        int presentCompanion = companionRepository.countCompanionsByEventAndPresence(idEvent);
        int total = presentCount + presentCompanion;
        return ResponseEntity.ok(total);
    }

    @GetMapping("/download")
    public void report(@RequestParam String codEvent, HttpServletResponse httpServletResponse, Pageable pageable) throws IOException {
        reportGuestService.generateReport(codEvent, httpServletResponse, pageable);

    }

    @PutMapping()
    public ResponseEntity updateGuest(@RequestBody Guest guest) {
        Optional<Guest> getGuest = guestRepository.findById(guest.getId());
        getGuest.get().setNome(guest.getNome());
        getGuest.get().setEmail(guest.getEmail());
        getGuest.get().setPresent(guest.getPresent());

        if (guest.getEmail() != null && !Objects.equals(getGuest.get().getStatusEnvio(), "enviado")) {
            sendQrcode(getGuest.get(), getGuest.get().getEvent());
        }
        List<Companion> existingCompanions = getGuest.get().getCompanionList();

        guest.getCompanionList().forEach(newCompanion -> {
            existingCompanions.stream()
                    .filter(existingCompanion -> existingCompanion.getId() == newCompanion.getId())
                    .findFirst()
                    .ifPresent(existingCompanion -> {
                        existingCompanion.setNome(newCompanion.getNome());
                        existingCompanion.setEmail(newCompanion.getEmail());
                        existingCompanion.setPresent(newCompanion.getPresent());

                        if (!Objects.equals(existingCompanion.getStatusEnvio(), "enviado")) {
                            sendQrcodeCompanion(existingCompanion, existingCompanion.getGuest().getEvent());
                        }
                    });
        });

        guestRepository.save(getGuest.get());
        return ResponseEntity.ok().build();
    }

    public void sendQrcode(Guest guest, Event event) {
        eventService.sendQrCode(guest, event);
    }

    public void sendQrcodeCompanion(Companion companion, Event event) {
        eventService.sendQrCodeCompanion(companion, event);
    }
}

