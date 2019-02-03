import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {CalendarService} from '../../services/calendar.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-add-event-calendar',
  templateUrl: './add-event-calendar.component.html',
  styleUrls: ['./add-event-calendar.component.scss']
})
export class AddEventCalendarComponent implements OnInit {

  constructor(private formBuilder: FormBuilder, private calendarService: CalendarService, private router: Router) { }

  addForm: FormGroup;

  ngOnInit() {

    this.addForm = this.formBuilder.group({
      id: [],
      title: ['', Validators.required],
      description: ['', Validators.required],
      location: [],
      name: ['', Validators.required],
      startDate: ['', Validators.required],
      endDate: ['', Validators.required]
    });
  }
  onSubmit() {
    this.calendarService.addEvent(this.addForm.value)
      .subscribe(data => {
        this.router.navigate(['calendar']);
      });
  }

}
