import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DeleteEventCalendarComponent } from './delete-event-calendar.component';

describe('DeleteEventCalendarComponent', () => {
  let component: DeleteEventCalendarComponent;
  let fixture: ComponentFixture<DeleteEventCalendarComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DeleteEventCalendarComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DeleteEventCalendarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
