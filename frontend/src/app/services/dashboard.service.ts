import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {ConfigService} from './config.service';
import {Observable} from 'rxjs/index';
import {Issue, mapResponseToIssue} from '../entities/issue';
import {map} from 'rxjs/operators';

const httpOptions = {
  headers: new HttpHeaders({
    'Access-Control-Allow-Origin': '*',
    'Content-Type': 'application/json',
    'Authorization': 'Basic ' + btoa('peter.domonkos@student.tuke.sk:2910Pepek1995')
  })
};

@Injectable({
  providedIn: 'root'
})
export class DashboardService {
  private readonly issue_id = 'issue/ZI-3';

  constructor(private httpClient: HttpClient,
              private configService: ConfigService) {

  }

  public getDashboardIssue(): Observable<Record<keyof Issue, any>> {
    return this.httpClient.get<any>(this.configService.getRestApiUrl(), httpOptions).pipe(
      map(response => mapResponseToIssue(response))
    );
  }
}
