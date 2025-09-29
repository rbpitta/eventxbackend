package com.events.eventxx.service;

import com.events.eventxx.Dto.CompanionDTO;
import com.events.eventxx.model.Companion;
import com.events.eventxx.model.Event;
import com.events.eventxx.model.Guest;
import com.events.eventxx.repository.EventRepository;
import com.events.eventxx.repository.GuestRepository;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class GuestService {

	@Autowired
	GuestRepository guestRepository;
	@Autowired
	EventService eventService;

	private final String status_guest = "ausente";

	public void saveGuest(Map<String, List<CompanionDTO>> mapGuest, Event event, Pageable page) throws Exception {
		for (Map.Entry<String, List<CompanionDTO>> entry : mapGuest.entrySet()) {
			String guestName = entry.getKey();
			List<CompanionDTO> list = entry.getValue();

			Guest guest = new Guest();
			guest.setNome(guestName);
			guest.setEmail(list.get(0).getEmail());
			guest.setEvent(event);
			guest.setPresent(status_guest);

			List<Companion> companions = list.stream()
				.skip(1)
				.map(dto -> {
					Companion companion = new Companion();
					companion.setNome(dto.getNome());
					companion.setEmail(dto.getEmail());
					companion.setPresent(status_guest);
					companion.setGuest(guest);
					return companion;
				}).collect(Collectors.toList());

			guest.setCompanionList(companions);
			guestRepository.save(guest);

		}
		eventService.processQrCodeAsync(event.getCodEvent(),page);


	}

}
