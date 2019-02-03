import {Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Issue} from '../../entities/issue';
import {DashboardService} from '../../services/dashboard.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  public issues;
  public issue: Record<keyof Issue, any>;


  constructor(private http: HttpClient,
              private dashboardService: DashboardService) {

    this.dashboardService.getDashboardIssue()
      .subscribe(issues => {
        this.issue = issues;
        console.log(issues);
        console.log(issues.asignee);
      });
  }

  // test() {
  //   return this.http.get('https://jsonplaceholder.typicode.com/todos/1').subscribe(a => console.log(a));
  // }

  ngOnInit() {
  }

}
