package com.team.szkielet.event;

public class Event implements Comparable<Event> {

    private String eventName;
    private String description;
    private String linkToEvent;
    private String image;
    private int day;
    private int month;
    private int year;

    public Event(String eventName, String description, String linkToEvent, String image, int day, int month, int year) {
        this.eventName = eventName;
        this.description = description;
        this.linkToEvent = linkToEvent;
        this.image = image;
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
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


    @Override
    public int compareTo(Event event) {

        int checkEvent = (event.getYear() * 365) + (event.getMonth() * 30) + event.getDay();
        int checkThis = (this.year * 365) + (this.month * 30) + this.day;

        return checkThis - checkEvent;
    }
}
