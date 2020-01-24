package com.team.szkielet.event;

public class Skarga {
    private String eventName;
    private String mail;

    public Skarga(String eventName, String mail) {
        this.eventName = eventName;
        this.mail = mail;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
