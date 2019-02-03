import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpHeaders, HttpResponse} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {JiraProject} from './entities/jira.project';
import {JiraIssue} from './entities/jira.issue';
import {JiraUser} from "./entities/jira.user";
import {Wrapper} from "./entities/create.issue.model";
import {catchError, map} from "rxjs/operators";

const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json'})
};

@Injectable({
  providedIn: 'root'
})
export class JiraService {

  private url = 'http://localhost:8081/rest';   //local domino 81

  constructor(
    private http: HttpClient
  ) {
  }

  postFile(file: File, issueId: string): Observable<Object> {
    if (file === undefined) file = null;
    const formdata: FormData = new FormData();
    formdata.append('file', file);
    return this.http.post('http://localhost:8081/rest/file/' + issueId, formdata, {
      reportProgress: true,
      responseType: 'text'
    })
  }

  postFields(data: Wrapper[]): Observable<Object> {
    return this.http.post('http://127.0.0.1:8081/rest/create', data, httpOptions);
  }

  getUsers(): Observable<JiraUser[]> {
    console.log('getUsers');
    return this.http.get<JiraUser[]>(this.url + '/users');
  }

  getProjects(): Observable<any> {
    console.log('getProjects:');
    return this.http.get<any>(this.url + '/projects')
  }

  getUser(): Observable<any> {
    console.log('getUser:');
    return this.http.get<any>(this.url + '/user/peter.domonkos/issues');
  }

  getProjectsForIssueType(issueID: number): Observable<JiraProject[]> {
    console.log('getProjectsForIssueType:', issueID);
    return this.http.get<JiraProject[]>(`${this.url}/projects/${issueID}`)
  }

  getIssueTypes(): Observable<JiraIssue[]> {
    console.log('getIssueTypes:');
    return this.http.get<JiraIssue[]>(this.url + '/issuetypes')
  }

  getIssueTypesForProject(projectKey: string): Observable<JiraIssue[]> {
    console.log('getIssueTypesForProject:', projectKey);
    return this.http.get<JiraIssue[]>(`${this.url}/issuetypes/${projectKey}`);
  }

  getFields(issueID: string, projectKey: string): Observable<any> {
    console.log('getFields:', issueID, projectKey);
    return this.http.get(`http://127.0.0.1:8081/rest/metadatalist/${issueID}/${projectKey}`);
  }

}

