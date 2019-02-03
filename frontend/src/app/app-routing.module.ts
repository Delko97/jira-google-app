import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterModule, Routes} from '@angular/router';
import {LoginComponent} from './components/login/login.component';
import {DashboardComponent} from './components/dashboard/dashboard.component';
import {CreateIssueComponent} from './components/create-issue/create-issue.component';
import {AuthGuard} from './auth/auth.guard';
import {CalendarComponent} from './components/calendar/calendar.component';
import {EditEventCalendarComponent} from './components/edit-event-calendar/edit-event-calendar.component';

const routes: Routes = [
  {path: '', component: LoginComponent},
  {path: 'dashboard', component: DashboardComponent, canActivate: [AuthGuard]},
  {path: 'login', component: LoginComponent},
  {path: 'create-issue', component: CreateIssueComponent, canActivate: [AuthGuard]},
  {path: 'calendar', component: CalendarComponent},
  {path: 'calendar/edit/:calndarId/:eventId', component: EditEventCalendarComponent}
];

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forRoot(routes)
  ],
  exports: [RouterModule],
  declarations: []
})
export class AppRoutingModule {
}
