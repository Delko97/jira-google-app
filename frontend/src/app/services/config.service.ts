import {Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ConfigService {

  private readonly REST_API_URL = 'http://localhost:8081/rest/user/peter.domonkos/firstissue';
  private readonly CALENDAR_URL = 'http://localhost:8081/rest/calendar';

  constructor() {
  }

  public getRestApiUrl(): string {
    return this.REST_API_URL;
  }
  public getCalendarUrl(): string {
    return this.CALENDAR_URL;
  }
}
