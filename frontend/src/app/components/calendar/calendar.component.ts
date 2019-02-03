import {Component, ChangeDetectionStrategy, OnInit} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {map, shareReplay} from 'rxjs/operators';
import {CalendarDateFormatter, CalendarEvent, CalendarEventAction, DateFormatterParams} from 'angular-calendar';
import {
  isSameMonth,
  isSameDay,
  startOfMonth,
  endOfMonth,
  startOfWeek,
  endOfWeek,
  startOfDay,
  endOfDay,
  format, startOfHour, startOfMinute, startOfSecond, endOfHour, endOfMinute, endOfSecond
} from 'date-fns';
import {Observable, of} from 'rxjs';
import {colors} from '../../entities/colors';
import {getTimezoneOffsetString} from '../../helpers/helper';
import {IRooms, mapResponseToRooms} from '../../entities/IRooms';
import {CalendarService} from '../../services/calendar.service';
import {Issue} from '../../entities/issue';
import {ActivatedRoute, Router} from '@angular/router';
import {ICalendars} from '../../entities/ICalendars';
import {formatDate} from '@angular/common';
import {CustomDateFormatter} from '../../helpers/customDateFormatter';


@Component({
  selector: 'app-calendar',
  templateUrl: './calendar.component.html',
  styleUrls: ['./calendar.component.scss'],
  providers: [{
    provide: CalendarDateFormatter,
    useClass: CustomDateFormatter
  }]
})
export class CalendarComponent implements OnInit {

  view: string = 'month';

  viewDate: Date = new Date();

  events: Record<keyof IRooms, any>[];
  events$: Observable<Array<CalendarEvent<IRooms>>>;
  calendars: Record<keyof ICalendars, any>;

  activeDayIsOpen: boolean = false;
  calendarID: string;
  isUpdated = false;

  constructor(private http: HttpClient,
              private calendarService: CalendarService,
              private router: Router,
              private activatedR: ActivatedRoute) {
  }

  actions: CalendarEventAction[] = [
    {
      label: '<i class="fa fa-fw fa-times"></i>',
      onClick: ({event}: { event: CalendarEvent }): void => {
        // this.events$ = this.events$.filter((iEvent) => iEvent !== event);
        // this.handleEvent('Deleted', event);
        console.log('this should be deleted');
        this.calendarService.deleteEvent({calendarId: this.calendarID, eventId: event.id}).subscribe(
          data => console.log(data)
        );
      }
    }
  ];

  ngOnInit() {
    // this.fetchEvents();
    this.calendarService.getCalendars().subscribe(response => {
      this.calendars = response;
      console.log('calendars', this.calendars);
    });
  }

  onChanges(): void {
    if (this.calendarID !== undefined) {
      this.fetchEvents();
    } else {
      this.calendarID = 'info@officeassistant.sk';
      this.fetchEvents();
    }
  }

  fetchEvents(): void {
    const getStart: any = {
      month: startOfMonth,
      week: startOfWeek,
      day: startOfDay
    }[this.view];

    const getEnd: any = {
      month: endOfMonth,
      week: endOfWeek,
      day: endOfDay
    }[this.view];

    this.calendarService.getEvents({
      startDate: getStart(this.viewDate),
      endDate: getEnd(this.viewDate),
      calendarId: this.calendarID,
      actions: this.actions
    })
      .subscribe(response => {
        this.events$ = of(response);
        console.log('calendar', this.calendarID);
      });
  }

  dayClicked({
               date,
               events
             }: {
    date: Date;
    events: Array<CalendarEvent<{ rooms: IRooms }>>;
  }): void {
    if (isSameMonth(date, this.viewDate)) {
      if (
        (isSameDay(this.viewDate, date) && this.activeDayIsOpen === true) ||
        events.length === 0
      ) {
        this.activeDayIsOpen = false;
      } else {
        this.activeDayIsOpen = true;
        this.viewDate = date;
      }
    }
  }

  eventClicked(event: CalendarEvent<IRooms>, calendarId, eventId: string | number): void {
    console.log('calendar, eventId', calendarId, eventId);
    this.router.navigate(['edit', calendarId, eventId], {relativeTo: this.activatedR});
  }

  public getIsUpdated() {
    return this.calendarService.isUpdated;
  }
}
