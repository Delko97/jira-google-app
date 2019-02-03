package com.zive.it.services;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.*;


import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.zive.it.models.Calendars;
import com.zive.it.models.InsertEvent;
import org.springframework.stereotype.Service;

@Service
public class CalendarService {

    private static final String APPLICATION_NAME = "zive it";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR);


    public List<Event> getEvents(String calendarId, String startDate,String endDate,Calendar service) throws IOException, GeneralSecurityException, ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date startDateFormatted = formatter.parse(startDate);
        Date endDateFormatted = formatter.parse(endDate);
        Events events = service.events().list(calendarId)
                .setTimeMin(new DateTime(startDateFormatted))
                .setTimeMax(new DateTime(endDateFormatted))
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();
        List<Event> eventsItems = events.getItems();
        return eventsItems;
    }

    public Event getEvent(String calendarId,String eventId,Calendar service) throws IOException {
        return service.events().get(calendarId,eventId).execute();
    }

    public void deleteEvent(String calendarId,String eventId,Calendar service) throws GeneralSecurityException, IOException {
        service.events().delete(calendarId,eventId).execute();
    }

    public void createEvent(String calendarId,InsertEvent eventData,Calendar service) throws GeneralSecurityException, IOException {
          Event event = new Event()
                .setSummary(eventData.getTitle())
                .setLocation(eventData.getLocation())
                .setDescription(eventData.getDescription());

        DateTime startDateTime = new DateTime(eventData.getStartDate());
        EventDateTime start = new EventDateTime()
                .setDateTime(startDateTime)
                .setTimeZone("Europe/Prague");
        event.setStart(start);

        DateTime endDateTime = new DateTime(eventData.getEndDate());
        EventDateTime end = new EventDateTime()
                .setDateTime(endDateTime)
                .setTimeZone("Europe/Prague");
        event.setEnd(end);

        service.events().insert(calendarId,event).execute();
    }

    public void updateEvent(String calendarId,String eventId,InsertEvent eventData,Calendar service) throws IOException, GeneralSecurityException {
        Event event = service.events().get(calendarId, eventId).execute();

        event.setDescription(eventData.getDescription());
        event.setSummary(eventData.getTitle());
        event.setLocation(eventData.getLocation());

        DateTime startDateTime = new DateTime(eventData.getStartDate());
        EventDateTime start = new EventDateTime()
                .setDateTime(startDateTime)
                .setTimeZone("Europe/Prague");
        event.setStart(start);

        DateTime endDateTime = new DateTime(eventData.getEndDate());
        EventDateTime end = new EventDateTime()
                .setDateTime(endDateTime)
                .setTimeZone("Europe/Prague");
        event.setEnd(end);

        service.events().update(calendarId, event.getId(), event).execute();
    }

    public List<Calendars> getCalendars(Calendar service) throws IOException {
        List<Calendars> calendars = new ArrayList<>();
        String pageToken = null;
        do {
            CalendarList calendarList = service.calendarList().list().setPageToken(pageToken).execute();
            List<CalendarListEntry> items = calendarList.getItems();

            for (CalendarListEntry calendarListEntry : items) {
                System.out.println(calendarListEntry.getSummary());
                calendars.add(new Calendars(calendarListEntry.getId(),calendarListEntry.getSummary()));
            }
            pageToken = calendarList.getNextPageToken();
        } while (pageToken != null);
        return calendars;
    }
}
