package com.zive.it.models;

public class EditEvent {

    private String calendarId;
    private String eventId;
    private InsertEvent event;

    public String getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(String calendarId) {
        this.calendarId = calendarId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public InsertEvent getEvent() {
        return event;
    }

    public void setEvent(InsertEvent event) {
        this.event = event;
    }
}
