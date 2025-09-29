package com.events.eventxx.repository;

import com.events.eventxx.model.Companion;
import com.events.eventxx.model.Guest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CompanionRepository extends JpaRepository<Companion, Integer> {
    List<Companion> findByGuest_Event_CodEvent(String cod);

    Optional<Companion> findBycodQrcode(String cod);
//
//    int countByPresentAndCodEvent(String present, String codEvent);
//
//    @Query("SELECT COUNT(c) FROM Companion c WHERE c.guest.event.codEvent = :codEvent and c.present = :'presente'" )
//    int countByGuest_Event_CodEvent(@Param("codEvent") String codEvent);
//
//    @Query(value = "SELECT COUNT(*) as total FROM ( " +
//            "SELECT c.id FROM Companion c WHERE c.guest.event.codEvent = :codEvent AND c.present = 'presente' " +
//            "UNION ALL " +
//            "SELECT g.id FROM Guest g WHERE g.event.codEvent = :codEvent AND g.present = 'presente' " +
//            ") AS combined", nativeQuery = true)
//    int countCompanionsAndGuestsByEvent(@Param("codEvent") String codEvent);

    @Query("SELECT COUNT(c) FROM Companion c WHERE c.guest.event.codEvent = :codEvent AND c.present = 'presente'")
    int countCompanionsByEventAndPresence(@Param("codEvent") String codEvent);


}


