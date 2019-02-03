import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {ConfigService} from './config.service';
import {IRooms, mapResponseToRooms} from '../entities/IRooms';
import {Observable} from 'rxjs';
import {Issue, mapResponseToIssue} from '../entities/issue';
import {map} from 'rxjs/operators';
import {format} from 'date-fns';
import {CalendarEvent, CalendarEventAction} from 'angular-calendar';
import {ICalendars, mapResponseToCalendars} from '../entities/ICalendars';
import {CalendarComponent} from '../components/calendar/calendar.component';


@Injectable({
  providedIn: 'root'
})
export class CalendarService {

  public isUpdated = false;

  constructor(private http: HttpClient, private configService: ConfigService) {
  }

  public addEvent(event: IRooms) {
    return this.http.post(this.configService.getCalendarUrl() + '/create', event);
  }

  public editEvent(calendarId, eventId, event: IRooms) {
    console.log('calendar, eventId, event', calendarId, eventId, event);
    this.isUpdated = true;
    return this.http.post(this.configService.getCalendarUrl() + '/updateEvent', {
      calendarId: calendarId,
      eventId: eventId,
      event: event
    });
  }

  public getEventById(params: { calendarId: string, id: string }) {
    const parameters = new HttpParams()
      .set(
        'calendarId',
        'info@officeassistant.sk'
      )
      .set(
        'eventId',
        params.id
      );
    return this.http.get<IRooms>(this.configService.getCalendarUrl() + '/event', {params: parameters}).pipe(
      map((response: IRooms[]) => mapResponseToRooms(response)));
  }

  public getEvents(params: { startDate: string, endDate: string, calendarId: string, actions}): Observable<CalendarEvent<IRooms>[]> {
    const parameters = new HttpParams()
      .set(
        'startDate',
        format(params.startDate, 'YYYY-MM-DD')
      )
      .set(
        'endDate',
        format(params.endDate, 'YYYY-MM-DD')
      )
      .set(
        'calendarId',
        params.calendarId
      )
    ;
    return this.http.get<IRooms>(this.configService.getCalendarUrl() + '/events', {params: parameters}).pipe(
      map((response: IRooms[]) => mapResponseToRooms(response)),
      map(irooms => irooms.map(room => {
        return {
          id: room.eventId,
          title: `${room.title} - ${room.location}`,
          start: room.startDate,
          end: room.endDate,
          allDay: false,
          actions: params.actions
        } as CalendarEvent<IRooms>;
      }))
    );
  }

  public getCalendars(): Observable<Record<keyof ICalendars, any>> {
    return this.http.get<any>(this.configService.getCalendarUrl() + '/calendars').pipe(
      map(response => mapResponseToCalendars(response))
    );
  }

  public getEventsForList(params: { startDate: string, endDate: string, calendarId: string }): Observable<Record<keyof IRooms, any>> {
    const parameters = new HttpParams()
      .set(
        'startDate',
        format(params.startDate, 'YYYY-MM-DD')
      )
      .set(
        'endDate',
        format(params.endDate, 'YYYY-MM-DD')

      )
      .set(
        'calendarId',
        params.calendarId
      );
    return this.http.get<any>(this.configService.getCalendarUrl() + '/events', {params: parameters}).pipe(
      map(response => mapResponseToRooms(response))
    );
  }

  public deleteEvent(params: { calendarId: string, eventId: string | number }) {
      const parameters = new HttpParams()
        .set(
          'calendarId',
          'info@officeassistant.sk'
        )
        .set(
          'eventId',
          params.eventId.toString()
        );
    return this.http.post(this.configService.getCalendarUrl() + '/delete', {params: parameters});
  }
}
