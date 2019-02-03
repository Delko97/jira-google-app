export class JiraProject {
  expand: string;
  self: string;
  id: number;
  key: string;
  name: string;
  avatarUrls: string;
  projectTypeKey: string;

  constructor(
    expand: string,
    self: string,
    id: number,
    key: string,
    name: string,
    avatarUrls: string,
    projectTypeKey: string,
  ) {
  }
}
