import {Component, OnInit} from '@angular/core';
import {Wrapper} from '../../entities/create.issue.model';
import {JiraProject} from '../../entities/jira.project';
import {JiraIssue} from '../../entities/jira.issue';
import {JiraService} from '../../create-issue.service';
import {FormBuilder, FormControl, FormGroup} from '@angular/forms';
import {JiraUser} from '../../entities/jira.user';
import {first, map} from 'rxjs/operators';
import {catchError, finalize} from '../../../../node_modules/rxjs/internal/operators';
import {empty} from 'rxjs';
import {HttpErrorResponse} from '@angular/common/http';
import {e} from '../../../../node_modules/@angular/core/src/render3';

@Component({
  selector: 'app-create-issue',
  templateUrl: './create-issue.component.html',
  styleUrls: ['./create-issue.component.scss']
})

export class CreateIssueComponent implements OnInit {

  fields: Wrapper[];
  projects: JiraProject[];
  issueTypes: JiraIssue[];
  headerForm: FormGroup;
  users: JiraUser[];
  attachments: File[] = [];
  errors: string[] = [];
  message: string;
  messageAttachments: string;

  constructor(private formBuilder: FormBuilder,
              private jiraService: JiraService) {
    this.headerForm = this.formBuilder.group({
      project: new FormControl(''),
      issuetype: new FormControl('')
    });
  }

  OnSubmit() {
    this.jiraService.postFields(this.fields)
      .subscribe(
        res => {
          let data = res as Resp;
          if (data.status == 201 || data.status == 200) {
            this.message = data.entity;
            setTimeout(() => {
              this.message = undefined;
            }, 7000);
            this.attachments.forEach(x =>
              this.jiraService.postFile(x, data.entity).subscribe(respond => {
                console.log('File res: ', respond);
                console.log(JSON.parse(respond.toString()).status);
                console.log(respond['status']);

                if (JSON.parse(respond.toString()).status == 201 || JSON.parse(respond.toString()).status == 200) {

                } else {
                  this.addErrors(JSON.parse(JSON.parse(respond.toString()).entity).errors)
                }
              }, null, () => {
                this.messageAttachments = 'Attachments added';
                setTimeout(() => {
                  this.messageAttachments = undefined;
                }, 7000);
              })
            );
          } else {
            this.addErrors(JSON.parse(data.entity).errors);
          }
        },
        (error: HttpErrorResponse) => {
          console.log(error.message);
          console.log(error.error);
        });
  }

  ngOnInit() {
    this.jiraService.getUsers().subscribe(res => {
      console.log('Users: ', res);
      this.users = res;
    });
    this.jiraService.getProjects().subscribe(res => {
        console.log('Projects: ', res);
        this.projects = res;
      }
    );
    this.jiraService.getIssueTypes().subscribe(res => {
        console.log('Issues: ', res);
        this.issueTypes = res;
      }
    );
    this.onChanges();
  }

  onChanges(): void {
    this.headerForm.get('project').valueChanges.subscribe(val => {
      if (this.headerForm.value.issuetype.id === undefined) {
        console.log('first call');
        this.jiraService.getIssueTypesForProject(val.key).subscribe(res => {
          this.issueTypes = res;
          console.log('issueTypes:  ', this.issueTypes);
        });
      } else if (this.fields === undefined) {
        console.log('second call');
        console.log(val.key);
        this.jiraService.getFields(this.headerForm.value.issuetype.id, val.key).subscribe(res => {
          this.fields = res;
          console.log('fields:  ', this.fields);
        });
      } else {
        console.log('Nulling call');
        this.headerForm.value.issuetype = undefined;
        this.fields = undefined;
        this.jiraService.getIssueTypesForProject(val.key).subscribe(res => {
          this.issueTypes = res;
          console.log('issueTypes:  ', this.issueTypes);
        });
      }
    });
    this.headerForm.get('issuetype').valueChanges.subscribe(val => {
      if (this.headerForm.value.project.id === undefined) {
        console.log('first call');
        this.jiraService.getProjectsForIssueType(val.id).subscribe(res => {
          this.projects = res;
          console.log('projects:  ', this.projects);
        });
      } else if (this.fields === undefined) {
        console.log('second call');
        this.jiraService.getFields(val.id, this.headerForm.value.project.key).subscribe(res => {
          this.fields = res;
          console.log('fields:  ', this.fields);
        });
      } else {
        console.log('Nulling call');
        this.headerForm.value.project = undefined;
        this.fields = undefined;
        this.jiraService.getProjectsForIssueType(val.id).subscribe(res => {
          this.projects = res;
          console.log('projects:  ', this.projects);
        });
      }
    });
  }

  removeFile(index: number) {
    this.attachments.splice(index, 1);
  }

  selectFile(event) {
    this.attachments.push(event.target.files[0]);
  }

  addErrors(obj: any) {
    for (const x in obj) {
      this.errors.push(x + '->' + obj[x]);
    }
    setTimeout(() => {
      this.errors = [];
    }, 7000);
  }
}

export interface Resp {
  status: number;
  statusType: string;
  entity: any;
  metadata: any;
}
