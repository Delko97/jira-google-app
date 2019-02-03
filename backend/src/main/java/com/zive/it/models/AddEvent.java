package com.zive.it.models;

public class AddEvent {

    private String calendarId;
    private InsertEvent event;


    public String getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(String calendarId) {
        this.calendarId = calendarId;
    }

    public InsertEvent getEvent() {
        return event;
    }

    public void setEvent(InsertEvent event) {
        this.event = event;
    }
}
