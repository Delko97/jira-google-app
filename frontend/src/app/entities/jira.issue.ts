export class JiraIssue{

  constructor(
    public id?: string,
    public self?: string,
    public description?: string,
    public iconUrl?: string,
    public name?: string,
    public subtask?: string,
    public avatarID?: string,
  ){
  }
}
