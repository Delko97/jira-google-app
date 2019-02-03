import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, ActivatedRouteSnapshot, Router} from '@angular/router';
import {CalendarService} from '../../services/calendar.service';
import {IRooms} from '../../entities/IRooms';
import {ICalendars} from '../../entities/ICalendars';

@Component({
  selector: 'app-edit-event-calendar',
  templateUrl: './edit-event-calendar.component.html',
  styleUrls: ['./edit-event-calendar.component.scss']
})
export class EditEventCalendarComponent implements OnInit {

  editForm: FormGroup;
  iroom: IRooms;
  calendars: Record<keyof ICalendars, any>;
  // startDate = new FormControl(new Date());
  // endDate = new FormControl(new Date());
  calendarId = 'info@officeassistant.sk';
  eId: string;

  constructor(private formBuilder: FormBuilder,
              private router: Router, private calendarService: CalendarService, private activatedRoute: ActivatedRoute) {
  }

  ngOnInit() {

    const eventId = this.activatedRoute.snapshot.params['eventId'];
    const calendarId = 'info@officeassistant.sk';

    this.calendarService.getCalendars().subscribe(response => {
      this.calendars = response;
      console.log('calendars', this.calendars);
    });
    console.log(eventId);


    this.editForm = this.formBuilder.group({
      id: [''],
      title: ['', Validators.required],
      description: ['', Validators.required],
      location: [''],
      name: ['', Validators.required],
      startDate: ['', Validators.required],
      endDate: ['', Validators.required]
    });
    this.calendarService.getEventById({
      calendarId: 'info@officeassistant.sk',
      id: eventId
    })
      .subscribe(data => {
         // this.eId = data.eventId;
        this.editForm.patchValue(data);
        console.log('data', data);
      });
    this.eId = eventId;
  }

  onSubmit() {
    this.calendarService.editEvent(this.calendarId, this.eId, this.editForm.value)
      .subscribe(data => {
        this.router.navigate(['calendar']);
      });
  }
}
