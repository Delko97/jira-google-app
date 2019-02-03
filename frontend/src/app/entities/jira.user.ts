export class JiraUser {
  key?: string;
  name?: string;
  displayName?: string;
  active?: boolean;
  emailAddress?: string;
  accountId?: string;
  avatar?: string;

  constructor(
    key?: string,
    name?: string,
    displayName?: string,
    active?: boolean,
    emailAddress?: string,
    accountId?: string
  ) {
  }
}
