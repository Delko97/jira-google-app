package com.zive.it.controllers;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import com.zive.it.models.*;
import com.zive.it.services.CalendarService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets.Details;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;

@RestController
@CrossOrigin
public class GoogleCalController {

    @Autowired
    private CalendarService calendarService;

    private final static Log logger = LogFactory.getLog(GoogleCalController.class);
    private static final String APPLICATION_NAME = "office assistant";
    private static HttpTransport httpTransport;
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static com.google.api.services.calendar.Calendar client;

    GoogleClientSecrets clientSecrets;
    GoogleAuthorizationCodeFlow flow;
    Credential credential;



    @Value("${google.client.client-id}")
    private String clientId;
    @Value("${google.client.client-secret}")
    private String clientSecret;
    @Value("${google.client.redirectUri}")
    private String redirectURI;

    private Set<Event> events = new HashSet<>();

    public void setEvents(Set<Event> events) {
        this.events = events;
    }

    @GetMapping(value = "/login/google")
    public RedirectView googleConnectionStatus(HttpServletRequest request) throws Exception {
        return new RedirectView(authorize());
    }

    @GetMapping(value = "/login/google", params = "code")
    public ResponseEntity<String> oauth2Callback(@RequestParam(value = "code") String code) {
        String message = null;
        try {
            TokenResponse response = flow.newTokenRequest(code).setRedirectUri(redirectURI).execute();
            credential = flow.createAndStoreCredential(response, "userID");
            client = new com.google.api.services.calendar.Calendar.Builder(httpTransport, JSON_FACTORY, credential)
                    .setApplicationName(APPLICATION_NAME).build();
        } catch (Exception e) {
            logger.warn("Exception while handling OAuth2 callback (" + e.getMessage() + ")."
                    + " Redirecting to google connection status page.");
            message = "Exception while handling OAuth2 callback (" + e.getMessage() + ")."
                    + " Redirecting to google connection status page.";
        }

        System.out.println("cal message:" + message);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping("/rest/calendar/events")
    public ResponseEntity<List<Event>> getEvents(@RequestParam String calendarId,
                                 @RequestParam String startDate,
                                 @RequestParam String endDate) throws IOException, GeneralSecurityException, ParseException {
        return new ResponseEntity(calendarService.getEvents(calendarId,startDate,endDate,client),HttpStatus.OK);
    }

    @GetMapping("/rest/calendar/event")
    public ResponseEntity<Event> getEvent(@RequestParam String calendarId,
                                          @RequestParam String eventId) throws IOException {
        return new ResponseEntity(calendarService.getEvent(calendarId,eventId,client),HttpStatus.OK);
    }

    @PostMapping("/rest/calendar/delete")
    public ResponseEntity deleteEvent(@RequestBody DeleteEvent event) throws GeneralSecurityException, IOException {
        calendarService.deleteEvent(event.getCalendarId(),event.getEventId(),client);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/rest/calendar/create")
    public ResponseEntity createEvent(@RequestBody AddEvent event) throws GeneralSecurityException, IOException {
        calendarService.createEvent(event.getCalendarId(),event.getEvent(),client);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/rest/calendar/updateEvent")
    public ResponseEntity updateEvent(@RequestBody EditEvent event) throws IOException, GeneralSecurityException {
        calendarService.updateEvent(event.getCalendarId(),event.getEventId(),event.getEvent(),client);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/rest/calendar/calendars")
    public ResponseEntity<List<Calendars>> getCalendars() throws IOException {
        return new ResponseEntity(calendarService.getCalendars(client),HttpStatus.OK);
    }


    private String authorize() throws Exception {
        AuthorizationCodeRequestUrl authorizationUrl;
        if (flow == null) {
            Details web = new Details();
            web.setClientId(clientId);
            web.setClientSecret(clientSecret);
            clientSecrets = new GoogleClientSecrets().setWeb(web);
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets,
                    Collections.singleton(CalendarScopes.CALENDAR)).build();
        }
        authorizationUrl = flow.newAuthorizationUrl().setRedirectUri(redirectURI);
        System.out.println("cal authorizationUrl->" + authorizationUrl);
        return authorizationUrl.build();
    }
}