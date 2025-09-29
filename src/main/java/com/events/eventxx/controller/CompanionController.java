package com.events.eventxx.controller;

import com.events.eventxx.model.Companion;
import com.events.eventxx.repository.CompanionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController("/api")
public class CompanionController {
    @Autowired
    CompanionRepository companionRepository;

    @PostMapping("/companion")
    public ResponseEntity saveCompanion(@RequestBody Companion companion) {
        try {
            companionRepository.save(companion);
            return ResponseEntity.ok("Companion saved successfully");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/companion")
    public ResponseEntity deleteCompanion(@RequestBody Integer id) {
        Optional<Companion> companion = companionRepository.findById(id);
        if (companion.isPresent()) {
            companionRepository.delete(companion.get());
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
