package com.events.eventxx.Dto;

public class CompanionDTO {
	private String nome;
	private String email;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	private String title;

	public CompanionDTO(String nome, String email, String title) {
		this.nome = nome;
		this.email = email;
		this.title = title;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


}
