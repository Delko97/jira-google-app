import {Component, Input, OnInit} from '@angular/core';
import {Wrapper} from "../../../entities/create.issue.model";
import {JiraUser} from "../../../entities/jira.user";


@Component({
  selector: 'app-user-input',
  templateUrl: './user-input.component.html',
  styleUrls: ['./user-input.component.scss']
})

export class UserInputComponent implements OnInit {

  @Input() field: Wrapper;
  @Input() users: JiraUser[];

  constructor() {
  }

  ngOnInit() {
  }


}
