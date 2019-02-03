import {Pipe, PipeTransform} from '@angular/core';
import {JiraUser} from "../../entities/jira.user";

@Pipe({
  name: 'userPipe'
})
export class UserPipe implements PipeTransform {

  transform(value: JiraUser[], userInput: string): JiraUser[] {
    if (value === undefined || userInput === '' || userInput === undefined || userInput == null) return value;
    return value.filter(x => x.name.includes(userInput.toLowerCase()));
  }
}
