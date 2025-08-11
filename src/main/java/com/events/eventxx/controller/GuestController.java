package com.events.eventxx.controller;

import com.events.eventxx.model.Companion;
import com.events.eventxx.model.Event;
import com.events.eventxx.model.Guest;
import com.events.eventxx.repository.CompanionRepository;
import com.events.eventxx.repository.EventRepository;
import com.events.eventxx.repository.GuestRepository;
import com.events.eventxx.service.EmailService;
import com.events.eventxx.service.EventService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class GuestController {

	@Autowired
	GuestRepository guestRepository;
	@Autowired
	EventRepository eventRepository;
	@Autowired
	EventService eventService;

	@GetMapping("/guest")
	public ResponseEntity<List<Guest>> getGuestById(@RequestParam String id) throws Exception {
		Event event = eventRepository.findByCodEvent(id);
		if (event == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
		List<Guest> listGuest = guestRepository.findByEvent_CodEvent(id);
		return ResponseEntity.status(HttpStatus.OK).body(listGuest);

	}
	@PostMapping("/guest/{idEvent}")
	public ResponseEntity saveGuest(@RequestBody Guest guest, @PathVariable String idEvent) {
		Event event = eventRepository.findByCodEvent(idEvent);
		Companion companion = new Companion();
		if (guest.getId() != null) {
			Optional<Guest> guest1 = guestRepository.findById(guest.getId());
			companion.setNome(guest.getNome());
			companion.setEmail(guest.getEmail());
			companion.setPresent(guest.getPresent());
			companion.setGuest(guest1.get());
			guest1.get().getCompanionList().add(companion);
			try {
			Guest savedGuest = guestRepository.save(guest1.get());
				Companion companionSalvo = savedGuest.getCompanionList()
						.stream()
						.filter(c -> c.getEmail().equalsIgnoreCase(companion.getEmail()))
						.findFirst()
						.orElse(null);

				sendQrcodeCompanion(companionSalvo, event);

				return ResponseEntity.ok().build();
			} catch (Exception ex) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
			}
		} else {
			try {
				if(event == null) {
					return ResponseEntity.notFound().build();
				}
					guest.setEvent(event);
					guest = guestRepository.save(guest);
					sendQrcode(guest, event);

				return ResponseEntity.ok().build();
			} catch (Exception ex) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
			}
		}
	}

	//AJUSTAR COD EVENTO por email
	//tirar 1234 hashcode
	//AO CADASTRAR NOVO ACOMP OU CONVIDADO GERAR QR CODE SALVAR NO BANCO E MANDAR EMAIL

	//filtro por nome e paginação
	//validação qr code tela
	//erros email como controlar

	/**Limpa tels **/
	//front end bonito
	//aparecer nome do evento grande na tela
	//formartar email com data e nome do evento
	//apagar console.log e alerts


	@PutMapping("/guest")
	public ResponseEntity updateGuest(@RequestBody Guest guest) {
		Optional<Guest> getGuest = guestRepository.findById(guest.getId());
		getGuest.get().setNome(guest.getNome());
		getGuest.get().setEmail(guest.getEmail());
		getGuest.get().setPresent(guest.getPresent());

		getGuest.get().getCompanionList().clear();
		getGuest.get().getCompanionList().addAll(guest.getCompanionList());

		guestRepository.save(getGuest.get());
		return ResponseEntity.ok().build();

	}
	public void sendQrcode(Guest guest, Event event){
		eventService.sendQrCode(guest, event);
	}

	public void sendQrcodeCompanion(Companion companion, Event event){
		eventService.sendQrCodeCompanion(companion, event);
	}
}

