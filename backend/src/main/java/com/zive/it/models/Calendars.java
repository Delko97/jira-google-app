package com.zive.it.models;

public class Calendars {

    private String calendarId;
    private String calendarName;

    public Calendars(String calendarId, String calendarName) {
        this.calendarId = calendarId;
        this.calendarName = calendarName;
    }

    public String getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(String calendarId) {
        this.calendarId = calendarId;
    }

    public String getCalendarName() {
        return calendarName;
    }

    public void setCalendarName(String calendarName) {
        this.calendarName = calendarName;
    }
}
