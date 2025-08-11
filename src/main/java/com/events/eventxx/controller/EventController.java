package com.events.eventxx.controller;

import com.events.eventxx.model.Event;
import com.events.eventxx.model.EventDto;
import com.events.eventxx.repository.EventRepository;
import com.events.eventxx.service.EmailService;
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
    public void newEvent(@RequestBody EventDto eventDto) throws Exception {
        try {
            eventService.saveEvent(eventDto);
        } catch (Exception ex) {
           throw new Exception("error", ex);
        }
    }

    @GetMapping("/event")
    public ResponseEntity<String> getEvent(@RequestParam String id) {
        try {
            Event event = eventRepository.findByCodEvent(id);

            if (event != null) {
                return ResponseEntity.ok(event.getCodEvent());
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

//TODO EMAIL, RANDON COD EVENTO, 
    //salvar convidado---ok
    //novo evento--- falta o email
    //novo acompanhante--ok
    //coluna status na tabela acompanhante e convidado--ok
    //metodo salvar presença convidado e acompanhante--ok
    //arrumar as exeception
    //validar quando o email não for encontrado
//ajustar select na tabela de evento para trazer só acompanhante sem repetir o nome --- tira o nome repitido
//no nome e manuscrito //ficará só o qr code


