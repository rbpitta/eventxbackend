package com.events.eventxx.Dto;

public class ManuscriptDto {
    public ManuscriptDto(String manuscript, Long codEvent) {
        this.manuscript = manuscript;
        this.codEvent = codEvent;
    }

    public String getManuscript() {
        return manuscript;
    }

    public void setManuscript(String manuscript) {
        this.manuscript = manuscript;
    }

    public Long getCodEvent() {
        return codEvent;
    }

    public void setCodEvent(Long codEvent) {
        this.codEvent = codEvent;
    }

    private String manuscript;
    private Long codEvent;

}
