package com.events.eventxx.model;

public class Layout {


    public Layout(String nomeCompleto, String manuscrito) {
        NomeCompleto = nomeCompleto;
        Manuscrito = manuscrito;
    }

    public String getNomeCompleto() {
        return NomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        NomeCompleto = nomeCompleto;
    }

    public String getManuscrito() {
        return Manuscrito;
    }

    public void setManuscrito(String manuscrito) {
        Manuscrito = manuscrito;
    }

    private String NomeCompleto;
    private String Manuscrito;

}
