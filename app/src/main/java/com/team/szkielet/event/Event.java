package com.team.szkielet.event;

public class Event {

    private String eventName;
    private String description;
    private String linkToEvent;
    private int image;

    public Event(String eventName, String description, String linkToEvent, int image) {
        this.eventName = eventName;
        this.description = description;
        this.linkToEvent = linkToEvent;
        this.image = image;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLinkToEvent() {
        return linkToEvent;
    }

    public void setLinkToEvent(String linkToEvent) {
        this.linkToEvent = linkToEvent;
    }
}
