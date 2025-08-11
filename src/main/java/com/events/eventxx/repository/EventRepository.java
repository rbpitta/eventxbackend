package com.events.eventxx.repository;

import com.events.eventxx.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Integer > {
    Event findByCodEvent(String id);


}
