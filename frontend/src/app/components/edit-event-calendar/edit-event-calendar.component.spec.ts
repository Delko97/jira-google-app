import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EditEventCalendarComponent } from './edit-event-calendar.component';

describe('EditEventCalendarComponent', () => {
  let component: EditEventCalendarComponent;
  let fixture: ComponentFixture<EditEventCalendarComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EditEventCalendarComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EditEventCalendarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
