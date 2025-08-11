package com.events.eventxx.Dto;

import com.events.eventxx.model.Companion;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

public class GuestDto {

	public String getGuestName() {
		return guestName;
	}

	public void setGuestName(String guestName) {
		this.guestName = guestName;
	}

	public String getGuestEmail() {
		return guestEmail;
	}

	public void setGuestEmail(String guestEmail) {
		this.guestEmail = guestEmail;
	}

	private String guestName;
	private String present;
	private String guestEmail;
	private List<Companion> acompanhantes = new ArrayList<>();

	public String getPresent() {
		return present;
	}

	public void setPresent(String present) {
		this.present = present;
	}

	public List<Companion> getAcompanhantes() {
		return acompanhantes;
	}

	public void setAcompanhantes(List<Companion> acompanhantes) {
		this.acompanhantes = acompanhantes;
	}

}
