package com.events.eventxx.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Guest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nome;
    private String present;
    private String email;
    private String codQrcode;
    private String statusEnvio;
    private boolean statusAccess;

    public String getMsgError() {
        return msgError;
    }

    public void setMsgError(String msgError) {
        this.msgError = msgError;
    }

    private String msgError;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "guest_id",referencedColumnName ="id")
    private List<Companion> companionList = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "idEvent", referencedColumnName = "id")
    private Event event;
    @UpdateTimestamp
    private Date updatedAt;

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

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public List<Companion> getCompanionList() {
        return companionList;
    }

    public void setCompanionList(List<Companion> companionList) {
        this.companionList = companionList;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPresent() {
        return present;
    }

    public void setPresent(String present) {
        this.present = present;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCodQrcode() {
        return codQrcode;
    }

    public void setCodQrcode(String codQrcode) {
        this.codQrcode = codQrcode;
    }


    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }


}
