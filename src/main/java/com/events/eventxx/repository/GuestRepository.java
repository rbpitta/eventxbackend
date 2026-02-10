package com.events.eventxx.repository;

import com.events.eventxx.model.Guest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GuestRepository extends JpaRepository<Guest, Integer> {
    Page<Guest> findByEvent_CodEvent(String codEvent, Pageable pageable);
    Page<Guest> findByEvent_CodEventAndNomeContainingIgnoreCase(String codEvent, Pageable pageable, String name);

    Optional<Guest> findBycodQrcode(String cod);
    int countByPresent(String present);
    @Query("SELECT COUNT(g) FROM Guest g WHERE g.event.codEvent = :codEvent AND g.present = 'presente'")
    int countGuestsByEventAndPresence(@Param("codEvent") String codEvent);




}
