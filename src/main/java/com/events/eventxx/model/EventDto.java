package com.events.eventxx.model;

import java.util.Date;

public class EventDto {
	private Integer codEvent;
	private String name;
	private String email;
	private String category;
	private Date dateEvent;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	private String password;

	public Integer getCodEvent() {
		return codEvent;
	}

	public void setCodEvent(Integer codEvent) {
		this.codEvent = codEvent;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Date getDateEvent() {
		return dateEvent;
	}

	public void setDateEvent(Date dateEvent) {
		this.dateEvent = dateEvent;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
