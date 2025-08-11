package com.events.eventxx.repository;

import com.events.eventxx.model.Companion;
import com.events.eventxx.model.Guest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CompanionRepository extends JpaRepository<Companion, Integer> {
    List<Companion> findByGuest_Event_CodEvent(String cod);
   Optional<Companion> findBycodQrcode(String cod);
}
