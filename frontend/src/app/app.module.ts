import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HTTP_INTERCEPTORS, HttpClient, HttpClientModule} from '@angular/common/http';

import {AppComponent} from './app.component';
import {NgbModalModule, NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {NavbarComponent} from './components/navbar/navbar.component';
import {LoginComponent} from './components/login/login.component';
import {DashboardComponent} from './components/dashboard/dashboard.component';
import {AppRoutingModule} from './app-routing.module';
import {CreateIssueComponent} from './components/create-issue/create-issue.component';
import {JwtInterceptor} from './auth/jwt.interceptor';
import {ErrorInterceptor} from './auth/error.interceptor';
import {ServiceWorkerModule} from '@angular/service-worker';
import {environment} from '../environments/environment';
import {TranslateLoader, TranslateModule, TranslateService} from '@ngx-translate/core';
import {TranslateHttpLoader} from '@ngx-translate/http-loader';
import {LanguageComponent} from './components/language/language.component';
import {Languages} from './entities/language.constants';
import { CalendarModule, DateAdapter } from 'angular-calendar';
import {adapterFactory} from 'angular-calendar/date-adapters/date-fns';
import { CalendarHeaderComponent } from './components/calendar-header/calendar-header.component';
import { CalendarComponent } from './components/calendar/calendar.component';
import {FlatpickrModule} from 'angularx-flatpickr';
import {CommonModule} from '@angular/common';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import { AddEventCalendarComponent } from './components/add-event-calendar/add-event-calendar.component';
import { EditEventCalendarComponent } from './components/edit-event-calendar/edit-event-calendar.component';
import { DeleteEventCalendarComponent } from './components/delete-event-calendar/delete-event-calendar.component';
import {ActivatedRoute} from '@angular/router';
import {MatButtonModule, MatSelect} from '@angular/material';
import {MatSelectModule} from '@angular/material/select';
import {OwlDateTimeModule, OwlNativeDateTimeModule} from 'ng-pick-datetime';
//import { BsDatepickerModule } from 'ngx-bootstrap/datepicker';
//import { TimepickerModule } from 'ngx-bootstrap/timepicker';
import {MatChipsModule, MatIconModule,} from '@angular/material';
import {UserPipe} from './components/create-issue/user.pipe'
import {MatAutocompleteModule} from '@angular/material/autocomplete';
import {UserInputComponent} from "./components/create-issue/user-input/user-input.component";

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    LoginComponent,
    DashboardComponent,
    CreateIssueComponent,
    UserInputComponent,
    LanguageComponent,
    UserPipe,
    CalendarHeaderComponent,
    CalendarComponent,
    AddEventCalendarComponent,
    EditEventCalendarComponent,
    DeleteEventCalendarComponent
  ],
  imports: [
    BrowserModule,
    NgbModule.forRoot(),
    AppRoutingModule,
    ReactiveFormsModule,
    FormsModule,
    CommonModule,
    HttpClientModule,
    BrowserAnimationsModule,
    ServiceWorkerModule.register('ngsw-worker.js', {enabled: environment.production}),
    NgbModalModule,
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: HttpLoaderFactory,
        deps: [HttpClient]
      }
    }),
    MatChipsModule,
    MatIconModule,
    MatAutocompleteModule,
    MatSelectModule,
    CalendarModule.forRoot({
      provide: DateAdapter,
      useFactory: adapterFactory
    }),
    FlatpickrModule.forRoot(),
    MatButtonModule,
    OwlDateTimeModule,
    OwlNativeDateTimeModule
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: JwtInterceptor,
      multi: true
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: ErrorInterceptor,
      multi: true
    },
  ],
  exports: [UserPipe],
  bootstrap: [AppComponent]
})
export class AppModule {
  constructor(private translateService: TranslateService) {
    this.translateService.setDefaultLang('en');
    const browserLang = this.translateService.getBrowserLang();
    if (Languages[browserLang]) {
      this.translateService.use(browserLang);
    } else {
      this.translateService.use('en');
    }
  }
}

export function HttpLoaderFactory(http: HttpClient) {
  return new TranslateHttpLoader(http);
}
