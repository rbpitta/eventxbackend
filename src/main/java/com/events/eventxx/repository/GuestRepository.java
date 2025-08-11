package com.events.eventxx.repository;

import com.events.eventxx.model.Guest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GuestRepository extends JpaRepository<Guest, Integer> {
    List<Guest>findByEvent_CodEvent(String id);
    Optional<Guest> findBycodQrcode(String cod);

}
