package com.events.eventxx.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

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
    private String msgError;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String title;
    @ManyToOne
    @JoinColumn(name = "guest_id", referencedColumnName = "id")
    @JsonIgnoreProperties("companionList")
    private Guest guest;

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @UpdateTimestamp
    private Date updatedAt;

    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    public Integer getId() {
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
    public String getMsgError() {
        return msgError;
    }

    public void setMsgError(String msgError) {
        this.msgError = msgError;
    }

}
