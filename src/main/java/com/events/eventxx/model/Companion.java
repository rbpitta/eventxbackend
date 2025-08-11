package com.events.eventxx.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
public class Companion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;
    public String nome;
    public String present;
    public String email;
    private String codQrcode;
    private String statusEnvio;
    private boolean statusAccess;
    @ManyToOne
    @JoinColumn(name = "guest_id", referencedColumnName = "id")
    @JsonIgnoreProperties("companionList")
    private Guest guest;

    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodQrcode() {
        return codQrcode;
    }

    public void setCodQrcode(String codQrcode) {
        this.codQrcode = codQrcode;
    }

    public String getStatusEnvio() {
        return statusEnvio;
    }

    public void setStatusEnvio(String statusEnvio) {
        this.statusEnvio = statusEnvio;
    }

    public boolean getStatusAccess() {
        return statusAccess;
    }

    public void setStatusAccess(boolean statusAccess) {
        this.statusAccess = statusAccess;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPresent() {
        return present;
    }

    public void setPresent(String present) {
        this.present = present;
    }

}
